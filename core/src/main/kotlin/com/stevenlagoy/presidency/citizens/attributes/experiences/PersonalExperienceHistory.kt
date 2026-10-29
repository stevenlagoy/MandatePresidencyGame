package com.stevenlagoy.presidency.citizens.attributes.experiences

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.util.plus
import java.time.LocalDate
import java.util.function.IntFunction

/**
* History of one person's experiences.
*
* Wraps a collection of experiences and provides specialized query and ordering functions for personal experiences.
*
* @property experiences Collection of experiences in one person's history.
* @property skillsDeveloped Total skills developed over all experiences.
* @property occupiedYears Total number of years spent having experiences.
*
* @see PersonalExperience
*/
class PersonalExperienceHistory(
    engine: Engine,
    experiences: Collection<PersonalExperience> = setOf()
) : EngineBound(engine), Collection<PersonalExperience> by experiences, JSONSerializable<PersonalExperienceHistory> {

    constructor(engine: Engine, json: JSONObject) : this(engine) { fromJson(json) }

    val experiences: MutableSet<PersonalExperience> = experiences.toMutableSet()

    val skillsDeveloped: Triple<Double, Double, Double>
        get() = experiences.fold(Triple(0.0, 0.0, 0.0)) { acc, next -> acc + next.skillsDeveloped }

    val occupiedYears: Double
        get() = experiences.fold(0.0) { acc, next -> acc + next.tenureYears }

    /**
     * Get all experiences in this history which are isComplete before the given date.
     * @return Set of experiences which are isComplete before the given date
     */
    fun getExperiencesBefore(date: LocalDate): Set<PersonalExperience> {
        return experiences.filter { it.isComplete && it.endDate!!.isBefore(date) }.toSet() // isComplete == true means that endDate is non-null
    }

    /**
     * Get all experiences in this history which are isComplete after the given date.
     * @return Set of experiences which are isComplete after the given date
     */
    fun getExperiencesAfter(date: LocalDate): Set<PersonalExperience> {
        return experiences.filter { it.isComplete && it.startDate.isAfter(date) }.toSet()
    }

    fun getNearestExperienceBefore(date: LocalDate): PersonalExperience? = getExperiencesBefore(date).maxOrNull()

    fun getNearestExperienceAfter(date: LocalDate): PersonalExperience? = getExperiencesAfter(date).minOrNull()

    fun getNearestEndDateBefore(date: LocalDate): LocalDate? = getNearestExperienceBefore(date)?.endDate

    fun getNearestStartDateAfter(date: LocalDate): LocalDate? = getNearestExperienceAfter(date)?.startDate

    @Deprecated("'fun <T : Any!> toArray(generator: IntFunction<Array<(out) T!>!>!): Array<(out) T!>!' is deprecated. This declaration is redundant in Kotlin and might be removed soon.")
    override fun <T> toArray(generator: IntFunction<Array<out T?>?>): Array<out T?>? = super.toArray(generator)

    override fun toJson() = JSONObject(hashCode().toString(), experiences.map { it.toJson() })

    override fun fromJson(json: JSONObject) = apply {
        experiences.clear().also { experiences.addAll(json.requireArray().filterIsInstance<JSONObject>().map { PersonalExperience(engine, it) }) }
    }
}
