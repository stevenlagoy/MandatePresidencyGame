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
* @property experiences     Collection of experiences in one person's history.
* @property skillsDeveloped Total skills developed over all experiences.
* @property occupiedYears   Total number of years spent having experiences.
*
* @see PersonalExperience
*/
class PersonalExperienceHistory(
    engine: Engine,
    experiences: Collection<PersonalExperience> = setOf()
) : EngineBound(engine), Collection<PersonalExperience> by experiences, JSONSerializable<PersonalExperienceHistory> {

    /**
     * Constructor from a JSONObject representation of a `PersonalExerperienceHistory`, which is an array
     * of JSONObjects representing [PersonalExperience] instances.
     * @see [fromJson]
     */
    constructor(engine: Engine, json: JSONObject) : this(engine) { fromJson(json) }

    val experiences: MutableSet<PersonalExperience> = experiences.toMutableSet()

    val skillsDeveloped: Triple<Double, Double, Double>
        get() = experiences.fold(Triple(0.0, 0.0, 0.0)) { acc, next -> acc + next.skillsDeveloped }

    val occupiedYears: Double
        get() = experiences.fold(0.0) { acc, next -> acc + next.tenureYears }

    /**
     * Get all experiences in this history which have [PersonalExperience.startDate] before the given date.
     * @return Set of experiences which start before the given date
     */
    fun getExperiencesStartingBefore(date: LocalDate): Set<PersonalExperience> {
        return experiences.filter { it.startDate.isBefore(date) }.toSet()
    }

    fun getExperiencesEndingBefore(date: LocalDate): Set<PersonalExperience> {
        return experiences.filter { it.endDate != null && it.endDate!!.isBefore(date) }.toSet()
    }

    fun getExperiencesCompletedBefore(date: LocalDate): Set<PersonalExperience> {
        return experiences.filter { it.isComplete && it.endDate!!.isBefore(date) }.toSet()
    }

    fun getExperiencesOn(date: LocalDate): Set<PersonalExperience> {
        return experiences.filter {
            it.startDate.isBefore(date) && (it.endDate == null || it.endDate!!.isAfter(date))
        }.toSet()
    }

    fun getExperiencesStartingAfter(date: LocalDate): Set<PersonalExperience> {
        return experiences.filter { it.startDate.isAfter(date) }.toSet()
    }

    fun getExperiencesEndingAfter(date: LocalDate): Set<PersonalExperience> {
        return experiences.filter { it.endDate != null && it.endDate!!.isAfter(date) }.toSet()
    }

    fun getExperiencesCompletedAfter(date: LocalDate): Set<PersonalExperience> {
        return experiences.filter { it.isComplete && it.endDate!!.isAfter(date) }.toSet()
    }

    /**
     * Get the most recent experience which started before the given date and is now complete.
     */
    fun getNearestExperienceBefore(date: LocalDate): PersonalExperience? = getExperiencesStartingBefore(date).maxOrNull()

    /**
     * Get the next experience which started after the given date and is now complete.
     */
    fun getNearestExperienceAfter(date: LocalDate): PersonalExperience? = getExperiencesStartingAfter(date).minOrNull()

    /**
     * Get the end date of the most recent experience which started before the given date and is now complete.
     * @see getNearestExperienceBefore
     */
    fun getNearestEndDateBefore(date: LocalDate): LocalDate? = getNearestExperienceBefore(date)?.endDate

    /**
     * Get the start date of the next experience which started after the given date and is now complete.
     * @see getNearestExperienceAfter
     */
    fun getNearestStartDateAfter(date: LocalDate): LocalDate? = getNearestExperienceAfter(date)?.startDate

    @Deprecated("'fun <T : Any!> toArray(generator: IntFunction<Array<(out) T!>!>!): Array<(out) T!>!' is deprecated. This declaration is redundant in Kotlin and might be removed soon.")
    override fun <T> toArray(generator: IntFunction<Array<out T?>?>): Array<out T?>? = super.toArray(generator)

    override fun toJson() = JSONObject(hashCode().toString(), experiences.map { it.toJson() })

    override fun fromJson(json: JSONObject) = apply {
        experiences.clear().also { experiences.addAll(json.requireArray().filterIsInstance<JSONObject>().map { PersonalExperience(engine, it) }) }
    }
}
