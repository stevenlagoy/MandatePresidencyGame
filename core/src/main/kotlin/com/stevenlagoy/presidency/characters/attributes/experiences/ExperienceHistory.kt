package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.util.*
import java.time.LocalDate
import java.util.*

class ExperienceHistory(
    protected val ENGINE: Engine,
    val experiences: TreeSet<ExperienceEntry> = TreeSet(),
) : Jsonic<ExperienceHistory> {

    class ExperienceEntry(
        protected val ENGINE: Engine,
        val experience: Experience,
        val startDate: LocalDate,
        var endDate: LocalDate?,
    ) : Comparable<ExperienceEntry>, Jsonic<ExperienceEntry> {

        constructor(ENGINE: Engine, experience: Experience, startDate: LocalDate, tenureYears: Int) : this(
            ENGINE,
            experience,
            startDate,
            startDate.plusYears(tenureYears.toLong())
        )

        constructor(ENGINE: Engine, json: JSONObject) : this(
            ENGINE,
            ENGINE.CHARACTER_MANAGER.EXPERIENCE_MANAGER.matchExperience(json.get("experience", String::class.java)).get(),
            LocalDate.parse(json.get("startDate", String::class.java)),
            json.get("endDate", String::class.java)?.let { LocalDate.parse(it) }
        )

        val tenureYears: Double
            get() {
                endDate?.let { return daysBetween(startDate, it).toDouble() / daysInYear }
                return ENGINE.TIME_MANAGER.yearsAgo(startDate).toDouble() / yearDuration
            }

        val skillsDeveloped: Triple<Double, Double, Double>
            get() = experience.yearlySkills * tenureYears

        override fun compareTo(other: ExperienceEntry): Int {
            return this.startDate.compareTo(other.startDate) * 2 + (this.endDate?.compareTo(other.endDate ?: ENGINE.TIME_MANAGER.currentDate.toLocalDate()) ?: 0)
        }

        override fun toJson() = JSONObject(experience.label, listOf(
            JSONObject("experience", experience.name),
            JSONObject("startDate", startDate.toString()),
            JSONObject("endDate", endDate?.toString()),
        ))

        override fun fromJson(json: JSONObject) = this.apply {
        }
    }

    constructor (ENGINE: Engine, experiences: Collection<ExperienceEntry>) : this(ENGINE, TreeSet<ExperienceEntry>(experiences))

    constructor(ENGINE: Engine, json: JSONObject) : this(
        ENGINE,
        TreeSet(json.get("experiences", List::class.java).map { ExperienceEntry(ENGINE, it as JSONObject) }),
    )

    fun add(experience: Experience, startDate: LocalDate, endDate: LocalDate?) {
        experiences.add(ExperienceEntry(ENGINE, experience, startDate, endDate))
    }

    fun add(experience: Experience, startDate: LocalDate, tenureYears: Int) {
        experiences.add(ExperienceEntry(ENGINE, experience, startDate, tenureYears))
    }

    val totalSkillsDeveloped: Triple<Double, Double, Double>
        get() = experiences.fold(Triple(0.0, 0.0, 0.0)) { acc, next -> acc + next.skillsDeveloped }

    val totalOccupiedYears: Double
        get() {
            if (experiences.isEmpty()) return 0.0
            val earliestDate = experiences.first().startDate
            val latestDate = experiences.last().endDate ?: ENGINE.TIME_MANAGER.currentDate.toLocalDate()
            val totalYears = daysBetween(earliestDate, latestDate).toDouble() / daysInYear
            val totalOccupiedYears = experiences.fold(0.0) { acc, experience -> acc + experience.tenureYears }
            return totalOccupiedYears
        }

    fun getExperiencesBefore(date: LocalDate) = experiences.filter { it.startDate.isBefore(date) }

    fun getExperiencesBefore(experienceEntry: ExperienceEntry) = getExperiencesBefore(experienceEntry.startDate)

    fun getExperiencesAfter(date: LocalDate) = experiences.filter { it.startDate.isAfter(date) }

    override fun toJson() = JSONObject(this.javaClass.simpleName, experiences.map { it.toJson() })

    override fun fromJson(json: JSONObject?): ExperienceHistory? {
        TODO("Not yet implemented")
    }
}
