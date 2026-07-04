package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.util.*
import java.time.LocalDate
import java.util.*

class ExperienceHistory(
    engine: Engine,
    val experiences: TreeSet<ExperienceEntry> = TreeSet(),
) : JSONSerializable<ExperienceHistory>, EngineBound(engine) {

    class ExperienceEntry(
        engine: Engine,
        _experience: Experience? = null,
        startDate: LocalDate = engine.TIME_MANAGER.currentDate.toLocalDate(),
        var endDate: LocalDate? = null,
    ) : Comparable<ExperienceEntry>, JSONSerializable<ExperienceEntry>, EngineBound(engine) {

        lateinit var experience: Experience
            internal set

        var startDate: LocalDate = startDate
            internal set

        val tenureYears: Double
            get() {
                endDate?.let { return daysBetween(startDate, it).toDouble() / daysInYear }
                return engine.TIME_MANAGER.yearsAgo(startDate).toDouble() / yearDuration
            }

        val skillsDeveloped: Triple<Double, Double, Double>
            get() = experience.yearlySkills * tenureYears

        init {
            if (_experience == null) experience = _experience
        }

        constructor(engine: Engine, experience: Experience, startDate: LocalDate, tenureYears: Int) : this(
            engine,
            experience,
            startDate,
            startDate.plusYears(tenureYears.toLong())
        )

        constructor(engine: Engine, json: JSONObject) : this(engine) {
            fromJson(json)
        }

        override fun compareTo(other: ExperienceEntry): Int {
            return this.startDate.compareTo(other.startDate) * 2 + (this.endDate?.compareTo(other.endDate ?: engine.TIME_MANAGER.currentDate.toLocalDate()) ?: 0)
        }

        override fun toJson() = JSONObject(experience.label, listOf(
            JSONObject("experience", experience.name),
            JSONObject("startDate", startDate.toString()),
            JSONObject("endDate", endDate?.toString()),
        ))

        override fun fromJson(json: JSONObject) = this.apply {
            TODO()
        }
    }

    constructor (ENGINE: Engine, experiences: Collection<ExperienceEntry>) : this(ENGINE, TreeSet<ExperienceEntry>(experiences))

    constructor(ENGINE: Engine, json: JSONObject) : this(
        ENGINE,
        TreeSet(json.requireArray("experiences").map { ExperienceEntry(ENGINE, it as JSONObject) }),
    )

    fun add(experience: Experience, startDate: LocalDate, endDate: LocalDate?) {
        experiences.add(ExperienceEntry(engine, experience, startDate, endDate))
    }

    fun add(experience: Experience, startDate: LocalDate, tenureYears: Int) {
        experiences.add(ExperienceEntry(engine, experience, startDate, tenureYears))
    }

    val totalSkillsDeveloped: Triple<Double, Double, Double>
        get() = experiences.fold(Triple(0.0, 0.0, 0.0)) { acc, next -> acc + next.skillsDeveloped }

    val totalOccupiedYears: Double
        get() {
            if (experiences.isEmpty()) return 0.0
            val earliestDate = experiences.first().startDate
            val latestDate = experiences.last().endDate ?: engine.TIME_MANAGER.currentDate.toLocalDate()
            val totalYears = daysBetween(earliestDate, latestDate).toDouble() / daysInYear
            val totalOccupiedYears = experiences.fold(0.0) { acc, experience -> acc + experience.tenureYears }
            return totalOccupiedYears
        }

    fun getExperiencesBefore(date: LocalDate) = experiences.filter { it.startDate.isBefore(date) }

    fun getExperiencesBefore(experienceEntry: ExperienceEntry) = getExperiencesBefore(experienceEntry.startDate)

    fun getExperiencesAfter(date: LocalDate) = experiences.filter { it.startDate.isAfter(date) }

    override fun toJson() = JSONObject(this.javaClass.simpleName, experiences.map { it.toJson() })

    override fun fromJson(json: JSONObject): ExperienceHistory {
        TODO("Not yet implemented")
    }
}
