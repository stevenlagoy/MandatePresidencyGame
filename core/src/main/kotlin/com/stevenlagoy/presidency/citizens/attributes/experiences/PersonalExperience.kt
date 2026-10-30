package com.stevenlagoy.presidency.citizens.attributes.experiences

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.util.daysBetween
import com.stevenlagoy.presidency.util.daysInYear
import com.stevenlagoy.presidency.util.times
import java.time.LocalDate

/**
 * Personal experience is one individual occupational or educational experience which one person has.
 *
 * Personal experiences should not be shared between citizens.
 *
 * @property type            What type of experience this is
 * @property startDate       The date on which this experience began
 * @property endDate         The date on which this experience ended, which may be `null` for an ongoing experience
 * @property isComplete      Flag indicating whether this experience was completed, whatever that means for
 *                           an experience of [type]. If `true`, [endDate] must not be `null`
 * @property tenureYears     Number of years spent having this experience (so far, if it is ongoing).
 * @property skillsDeveloped Skills developed while having this experience (so far, if ongoing).
 *
 * @see PersonalExperienceHistory
 * @see PersonalExperienceType
 * @see PersonalExperienceManager
 */
class PersonalExperience(
    engine: Engine,
    type: PersonalExperienceType? = null,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    isComplete: Boolean = false,
) : EngineBound(engine), Comparable<PersonalExperience>, JSONSerializable<PersonalExperience> {

    /**
     * Constructor from a JSONObject representation of a `PersonalExerperience`.
     * @see [fromJson]
     */
    constructor(engine: Engine, json: JSONObject) : this(engine) { fromJson(json) }

    lateinit var type: PersonalExperienceType
        private set

    lateinit var startDate: LocalDate
        internal set

    var endDate: LocalDate? = endDate
        set(value) {
            require(value == null || value.isAfter(startDate)) {
                "end date must be null, or must be after start date"
            }
            field = value
        }

    @get:JvmName("isCompleted")
    var isComplete: Boolean = isComplete
        get() = endDate != null && field
        set(value) {
            // If value true, endDate must be non-null
            require(endDate != null || !field) { "completed experiences must have an endDate" }
            field = value
        }

    val tenureYears: Double
        get() = daysBetween(startDate, endDate ?: engine.TIME_MANAGER.currentDate.toLocalDate()) * 1.0 / daysInYear

    val skillsDeveloped: Triple<Double, Double, Double>
        get() = type.yearlySkills * tenureYears

    init {
        require(startDate == null || !startDate.isAfter(engine.TIME_MANAGER.currentDate.toLocalDate())) {
            "start date must not be in the future"
        }
        if (type != null) this.type = type
        if (startDate != null) this.startDate = startDate
    }

    override fun compareTo(other: PersonalExperience): Int {
        return compareValuesBy(this, other, { it.startDate }, { it.endDate })
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("type", engine.getManager(PersonalExperienceTypeManager::class.java).keyOf(this.type)),
        JSONObject("startDate", startDate.toString()),
        JSONObject("endDate", endDate.toString()),
        JSONObject("isComplete", isComplete),
    ))

    override fun fromJson(json: JSONObject) = apply {
        type = engine
            .getManager(PersonalExperienceTypeManager::class.java)
            .requireByKey(json.requireString("type"))
        startDate = LocalDate.parse(json.requireString("startDate", "start_date", "start"))
        endDate = LocalDate.parse(json.requireString("endDate", "end_date", "end_date"))
        isComplete = json.requireBoolean(
            "isComplete", "isCompleted", "is_complete", "is_completed", "complete", "completed"
        )
    }
}
