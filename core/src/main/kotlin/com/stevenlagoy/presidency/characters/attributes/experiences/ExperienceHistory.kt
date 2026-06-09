package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.util.plus
import com.stevenlagoy.presidency.util.times
import java.time.temporal.ChronoUnit
import com.stevenlagoy.presidency.util.yearDuration
import java.time.LocalDate
import java.util.TreeSet

class ExperienceHistory(
    val ENGINE: Engine,
    val experiences: TreeSet<ExperienceEntry> = TreeSet(),
) : Jsonic<ExperienceHistory> {

    class ExperienceEntry(
        val ENGINE: Engine,
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
                endDate?.let { return ChronoUnit.DAYS.between(endDate, it).toDouble() / yearDuration }
                return ENGINE.TIME_MANAGER.yearsAgo(startDate).toDouble() / yearDuration
            }

        val skillsDeveloped: Triple<Double, Double, Double>
            get() = experience.yearlySkills * tenureYears

        override fun compareTo(other: ExperienceEntry): Int {
            return this.startDate.compareTo(other.startDate) * 2 + (this.endDate?.compareTo(other.endDate) ?: 0)
        }

        override fun toJson() = JSONObject(experience.name, listOf(
            JSONObject("experience", experience.name),
            JSONObject("startDate", startDate.toString()),
            JSONObject("endDate", endDate?.toString()),
        ))

        override fun fromJson(json: JSONObject) = this.apply {
        }
    }

    constructor(ENGINE: Engine, json: JSONObject) : this(
        ENGINE,
        TreeSet(json.get("experiences", List::class.java).map { ExperienceEntry(ENGINE, it as JSONObject) }),
    )

    fun add(experience: Experience, startDate: LocalDate, endDate: LocalDate) {
        experiences.add(ExperienceEntry(ENGINE, experience, startDate, endDate))
    }

    fun add(experience: Experience, startDate: LocalDate, tenureYears: Int) {
        experiences.add(ExperienceEntry(ENGINE, experience, startDate, tenureYears))
    }

    val totalSkillsDeveloped: Triple<Double, Double, Double>
        get() = experiences.fold(Triple(0.0, 0.0, 0.0)) { acc, next -> acc + next.skillsDeveloped }

    override fun toJson() = JSONObject(this.javaClass.simpleName, experiences.map { it.toJson() })

    override fun fromJson(json: JSONObject?): ExperienceHistory? {
        TODO("Not yet implemented")
    }
}
