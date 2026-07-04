package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import kotlin.jvm.optionals.getOrNull

/**
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
        json.requireString("name"),
        json.requireString("track"),
        json.requireDouble("capacity"),
        (json.requireArray("tenure_years").find {
            (it as JSONObject).key == "min"
        } as JSONObject).requireDouble(),
        (json.requireArray("tenure_years").find {
            (it as JSONObject).key == "avg"
        } as JSONObject).requireDouble(),
        (json.requireArray("tenure_years").find {
            (it as JSONObject).key == "max"
        } as JSONObject).requireDouble(),
        json.requireBoolean("repeatable"),
        json.requireArray("prerequisites").map { engine.CHARACTER_MANAGER.EXPERIENCE_MANAGER.matchExperience(it as String).let { opt ->
            if (opt.isEmpty) {
                println(opt)
            }
            opt.get()
        } },
        when (json.requireString("prerequisiteLogic")) {
            "any" -> PrerequisiteLogic.ANY
            "all" -> PrerequisiteLogic.ALL
            else  -> PrerequisiteLogic.ANY
        },
        json.requireDouble("base_chance"),
        json.requireInt("min_age"),
        json.requireInt("max_age"),
        Triple(
            (json.requireArray("yearly_skills").find {
                (it as JSONObject).key == "legislative"
            } as JSONObject).requireDouble(),
            (json.requireArray("yearly_skills").find {
                (it as JSONObject).key == "executive"
            } as JSONObject).requireDouble(),
            (json.requireArray("yearly_skills").find {
                (it as JSONObject).key == "judicial"
            } as JSONObject).requireDouble(),
        ),
        json.requireString("description"),
        json.requireArray("connections").associate {
            engine.CHARACTER_MANAGER.EXPERIENCE_MANAGER.matchExperience((it as JSONObject).key).getOrNull() to it.requireDouble()
        }.toMutableMap()
    )

    fun prerequisitsMet(priors: Collection<Experience>) = prerequisiteLogic.evaluate(priors, prerequisites)

    enum class PrerequisiteLogic(val evaluate: (Collection<Experience>, Collection<Experience>) -> Boolean) {
        ANY(fun(priors: Collection<Experience>, prerequisites: Collection<Experience>) = prerequisites.isEmpty() || prerequisites.find { it in priors } != null),
        ALL(fun(priors: Collection<Experience>, prerequisites: Collection<Experience>) = prerequisites.isEmpty() || prerequisites.find { it !in priors } == null),
    }
}
