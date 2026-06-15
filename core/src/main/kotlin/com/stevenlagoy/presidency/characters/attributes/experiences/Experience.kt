package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import kotlin.jvm.optionals.getOrNull

/**
 * @property tier [ExperienceTier] of this experience
 * @property capacity How much of a PoliticalActor's working ability a certain experience requires. The sum of any concurrent experiences must sum to 1.0 or less.
 * @property minTenure Minimum amount of time a PoliticalActor may have this experience.
 * @property avgTenure The average amount of time a PoliticalActor will have this experience.
 * @property maxTenure The maximum amount of time a PoliticalActor may have this experience.
 * @property prerequisites Prior experiences required or essentially required for this experience to be applicable.
 * @property prerequisiteLogic Logic for how prior experiences fulfil prerequisite requirements.
 */
class Experience(
    engine: Engine,
    val label: String,
    val name: String,
    val track: String,
    val capacity: Double,
    val minTenure: Double,
    val avgTenure: Double,
    val maxTenure: Double,
    @get:JvmName("isRepeatable")
    val repeatable: Boolean,
    val prerequisites: List<Experience>,
    val prerequisiteLogic: PrerequisiteLogic,
    val baseChance: Double,
    val minAge: Int,
    val maxAge: Int,
    val yearlySkills: Triple<Double, Double, Double>,
    val description: String,
    val connections: MutableMap<Experience?, Double>
) : EngineBound(engine) {
    constructor(engine: Engine, json: JSONObject) : this(
        engine,
        json.key,
        json.get("name", String::class.java),
        json.get("track", String::class.java),
        (json.get("capacity") as Number).toDouble(),
        (json.get("tenure_years", List::class.java).find {
            (it as JSONObject).key == "min"
        } as JSONObject).asNumber.toDouble(),
        (json.get("tenure_years", List::class.java).find {
            (it as JSONObject).key == "avg"
        } as JSONObject).asNumber.toDouble(),
        (json.get("tenure_years", List::class.java).find {
            (it as JSONObject).key == "max"
        } as JSONObject).asNumber.toDouble(),
        json.get("repeatable") as Boolean,
        json.get("prerequisites", List::class.java).map { engine.CHARACTER_MANAGER.EXPERIENCE_MANAGER.matchExperience(it as String).let { opt ->
            if (opt.isEmpty) {
                println(opt)
            }
            opt.get()
        } },
        when (json.get("prerequisiteLogic", String::class.java)) {
            "any" -> PrerequisiteLogic.ANY
            "all" -> PrerequisiteLogic.ALL
            else  -> PrerequisiteLogic.ANY
        },
        json.get("base_chance", Number::class.java).toDouble(),
        json.get("min_age", Number::class.java).toInt(),
        json.get("max_age", Number::class.java).toInt(),
        Triple(
            (json.get("yearly_skills", List::class.java).find {
                (it as JSONObject).key == "legislative"
            } as JSONObject).asNumber.toDouble(),
            (json.get("yearly_skills", List::class.java).find {
                (it as JSONObject).key == "executive"
            } as JSONObject).asNumber.toDouble(),
            (json.get("yearly_skills", List::class.java).find {
                (it as JSONObject).key == "judicial"
            } as JSONObject).asNumber.toDouble(),
        ),
        json.get("description", String::class.java),
        json.get("connections", List::class.java).associate {
            engine.CHARACTER_MANAGER.EXPERIENCE_MANAGER.matchExperience((it as JSONObject).key).getOrNull() to it.asNumber.toDouble()
        }.toMutableMap()
    )

    fun prerequisitsMet(priors: Collection<Experience>) = prerequisiteLogic.evaluate(priors, prerequisites)
}
