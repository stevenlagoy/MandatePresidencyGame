package com.stevenlagoy.presidency.characters.attributes.experiences

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality
import java.time.LocalDate
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
    val ENGINE: Engine,
    val label: String,
    val name: String,
    val track: String,
    val tier: ExperienceTier,
    val capacity: Double,
    val minTenure: Double,
    val avgTenure: Double,
    val maxTenure: Double,
    val prerequisites: List<Experience>,
    val prerequisiteLogic: PrerequisiteLogic,
    val minAge: Int,
    val yearlySkills: Triple<Double, Double, Double>,
    val description: String,
    val connections: MutableMap<Experience?, Double>
) {
    constructor(ENGINE: Engine, json: JSONObject) : this(ENGINE,
        json.key,
        json.get("name", String::class.java),
        json.get("track", String::class.java),
        when (json.get("tier", String::class.java)) {
            "entry"    -> ExperienceTier.ENTRY
            "mid"      -> ExperienceTier.MID
            "high"     -> ExperienceTier.HIGH
            "terminal" -> ExperienceTier.TERMINAL
            else       -> ExperienceTier.ENTRY
        },
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
        json.get("prerequisites", List::class.java).map { ENGINE.CHARACTER_MANAGER.EXPERIENCE_MANAGER.matchExperience(it as String).let { opt ->
            if (opt.isEmpty) {
                println(it)
            }
            opt.get()
        } },
        when (json.get("prerequisiteLogic", String::class.java)) {
            "any" -> PrerequisiteLogic.ANY
            "all" -> PrerequisiteLogic.ALL
            else  -> PrerequisiteLogic.ANY
        },
        json.get("min_age", Number::class.java).toInt(),
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
            ENGINE.CHARACTER_MANAGER.EXPERIENCE_MANAGER.matchExperience((it as JSONObject).key).getOrNull() to (it.value as Number).toDouble()
        }.toMutableMap()
    )

    fun prerequisitsMet(priors: Collection<Experience>) = prerequisiteLogic.evaluate(priors, prerequisites)
}

/*

academic
civic
labor
military
legal
judicial
legislative
executive
business
media

label
track
tier
capacity
tenure_years: min, typical, max
prerequisites
prerequisite_logic
min_age
yearly_skills: legislative, executive, judicial
description
connections
overlaps

 */
