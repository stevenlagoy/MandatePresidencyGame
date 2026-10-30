package com.stevenlagoy.presidency.citizens.attributes.experiences

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.core.Manager
import com.stevenlagoy.presidency.util.normalize
import kotlin.jvm.optionals.getOrNull
import kotlin.text.toDouble

/*
TODO: Should include values for determining when something is "complete". Some experiences are isComplete
as soon as they are begun, and some take several years. Need to come up with a consistent idea for what
"isComplete" means: for degrees or certificates, there's an obvious answer, but there is not for other
occupations. Should allow specifying a number of years after which most people complete the experience,
as well as a standard deviation or function which determines how many people complete the experience after
each number of years.
IE for a bachelor's degree:
1 year: <8%
2 years: <8%
3 years: 6%
4 years: 52.7%
5 years: 19.8%
6 or more years: 19.9%

Completion should be considered to "take effect" on the end date. An in-progress experience is not considered
to be complete.
 */

/**
 * One type of educational or occupational experience which a Citizen can have.
 *
 * A personal experience type defines parameters of a personal experience. This includes the title associated
 * with the experience, how long it may last, who is eligable for the experience, and how it impacts the
 * Citizen who has an experience of that type.
 *
 * @property name              Name identifying this experience type.
 * @property track             Track this experience type can be found on, like Education, Judicial, Business, etc.
 * @property capacity          How much of a PoliticalActor's working ability a certain experience requires.
 *                             The sum of any concurrent experiences must be 1.0 or less.
 * @property minTenureYears    Minimum number of years a person may have this experience.
 * @property avgTenureYears    Average number of years people who have this experience spend doing it.
 * @property maxTenureYears    Maximum number of years a person may continually have this experience.
 * @property isRepeatable      Whether multiple experiences of this type may be present in a Citizen's
 *                             experience history, even if the first instance was isComplete.
 * @property prerequisites     Prior types of experiences required or essentially required for this experience
 *                             type to be applicable.
 * @property prerequisiteLogic Logic for how prior experiences fulfil prerequisite requirements. (Dependency Injection)
 * @property baseChance        Prior likelihood for an experience of this type to be present in a person's history.
 * @property minAge            Minimum age a Citizen must be to begin an experience of this type.
 * @property maxAge            Maximum age a Citizen may be while undergoing an expereince of this type.
 * @property yearlySkills      Yearly additive impact on a Citizen's skills while undergoing an experience of this type.
 * @property description       Description for this type, broadly describing associated responsibilities or learning.
 * @property connections       Associative array of experience types and the impact that beginning an
 *                             experience of this type has upon that experience being selected in the future.
 *                             Impacts are additive among experience types' influences.
 *
 * @see PersonalExperienceTypeManager
 * @see PersonalExperience
 */
class PersonalExperienceType(
    engine: Engine,
    name: String = "",
    track: String = "",
    capacity: Double = 1.0,
    minTenureYears: Double = 0.0,
    avgTenureYears: Double = 0.0,
    maxTenureYears: Double = 120.0,
    isRepeatable: Boolean = true,
    prerequisites: List<PersonalExperienceType> = listOf(),
    prerequisiteLogic: PrerequisiteLogic = PrerequisiteLogic.ANY,
    baseChance: Double = 1.0,
    minAge: Double = 18.0,
    maxAge: Double = 120.0,
    yearlySkills: Triple<Double, Double, Double> = Triple(0.0, 0.0, 0.0),
    description: String = "",
    connections: Map<PersonalExperienceType, Double> = mapOf(),
) : EngineBound(engine), JSONSerializable<PersonalExperienceType> {

    /**
     * Constructor from a JSONObject representation of a `PersonalExerperienceType`.
     * @see [fromJson]
     */
    constructor(engine: Engine, json: JSONObject) : this(engine) { fromJson(json) }

    var name: String = name
        internal set
    var track: String = track
        internal set
    var capacity: Double = capacity
        internal set
    var minTenureYears: Double = minTenureYears
        internal set
    var avgTenureYears: Double = if (avgTenureYears > 0.0) avgTenureYears else (minTenureYears + maxTenureYears) / 2.0
    var maxTenureYears: Double = maxTenureYears
        internal set
    @get:JvmName("isRepeatable")
    var isRepeatable: Boolean = isRepeatable
        internal set
    var prerequisites: List<PersonalExperienceType> = prerequisites
        internal set
    var prerequisiteLogic: PrerequisiteLogic = prerequisiteLogic
        internal set
    var baseChance: Double = baseChance
        internal set
    var minAge: Double = minAge
        internal set
    var maxAge: Double = maxAge
        internal set
    var yearlySkills: Triple<Double, Double, Double> = yearlySkills
        internal set
    var description: String = description
        internal set
    private var unresolvedConnections: MutableMap<String, Double> = mutableMapOf()
    var connections: Map<PersonalExperienceType, Double> = connections
        get() {
            if (
                engine.getManager(PersonalExperienceTypeManager::class.java).state ==
                Manager.ManagerState.INITIALIZING
            ) {
                val resolvedKeys = mutableListOf<String>()
                unresolvedConnections.forEach { unresolvedConnection -> engine
                    .getManager(PersonalExperienceTypeManager::class.java)
                    .matchByKey(unresolvedConnection.key).map {
                        field += it to unresolvedConnection.value
                        resolvedKeys += unresolvedConnection.key
                    }
                }
                resolvedKeys.forEach { unresolvedConnections.remove(it) }
            }
            else assert(unresolvedConnections.isEmpty()) {
                "There were unresolved connections for experience type $name while the" +
                "${PersonalExperienceTypeManager::class.simpleName} was not initializing."
            }
            return field.withDefault { 0.0 }
        }
        internal set

    fun arePrerequisitesMet(priors: Collection<PersonalExperience>): Boolean = prerequisiteLogic.evaluate(priors, prerequisites)

    override fun fromJson(json: JSONObject) = apply {
        name = json.findString("name") { "" }!!
        track = json.findString("track") { "" }!!
        capacity = json.findDouble("capacity") { 0.0 }!!
        json.requireArray("tenureYears", "tenure_years", "tenure").let {
            fun get(vararg keys: String) = it.filterIsInstance<JSONObject>().firstNotNullOf { obj -> obj.findDouble(*keys).getOrNull() }
            minTenureYears = get(
                "min", "minimum", "minYears", "min_years", "minimumYears", "minimum_years",
                "minTenure", "min_tenure", "minTenureYears", "min_tenure_years", "minimumTenure", "minimum_tenure",
                "averageTenureYears", "minumum_tenure_years"
            )
            avgTenureYears = get(
                "avg", "average", "avgYears", "avg_years", "averageYears", "average_years",
                "avgTenure", "avg_tenure", "avgTenureYears", "avg_tenure_years", "averageTenure", "average_tenure",
                "averageTenureYears", "avgumum_tenure_years"
            )
            maxTenureYears = get(
                "max", "maximum", "maxYears", "max_years", "maximumYears", "maximum_years",
                "maxTenure", "max_tenure", "maxTenureYears", "max_tenure_years", "maximumTenure", "maximum_tenure",
                "maximumTenureYears", "maxumum_tenure_years"
            )
        }
        isRepeatable = json.findBoolean(listOf("isRepeatable", "is_repeatable", "repeatable")) { true }!!
        prerequisites = json.findArray("prerequisites") { emptyList<String>() }!!.filterIsInstance<String>().map {
            engine.getManager(PersonalExperienceTypeManager::class.java).requireByKey(it)
        }
        prerequisiteLogic = PrerequisiteLogic.fromString(
            json.requireString("prerequisiteLogic", "prerequisite_logic")
        ) ?: PrerequisiteLogic.ANY
        baseChance = json.requireDouble("base_chance")
        minAge = json.requireDouble("minAge", "min_age", "minumumAge", "minimum_age")
        maxAge = json.requireDouble("maxAge", "max_age", "maximumAge", "maximum_age")
        yearlySkills = json.requireArray("yearlySkills", "yearly_skills").filterIsInstance<JSONObject>().let {
            fun get(vararg keys: String) = it.find { entry -> entry.key in keys }!!.requireNumber().toDouble()
            Triple(
                get("legislative", "leg", "legislativeSkill", "legislative_skill"),
                get("executive", "exec", "executiveSkill", "executive_skill"),
                get("judicial", "jud", "judicialSkill", "judicial_skill"),
            )
        }
        description = json.requireString("description")
        connections = mutableMapOf<PersonalExperienceType, Double>().also {
            json.requireArray("connections").filterIsInstance<JSONObject>().forEach { connection ->
                val found = engine
                    .getManager(PersonalExperienceTypeManager::class.java)
                    .matchByKey(connection.key, String::normalize)
                if (found.isEmpty) unresolvedConnections += connection.key to connection.requireDouble()
                else connections += found.get() to connection.requireDouble()
            }
        }
    }

    override fun toJson(): JSONObject = JSONObject(name.normalize(), listOf(
        JSONObject("name", name),
        JSONObject("track", track),
        JSONObject("capacity", capacity),
        JSONObject("tenure", listOf(
            JSONObject("minTenureYears", minTenureYears),
            JSONObject("avgTenureYears", avgTenureYears),
            JSONObject("maxTenureYears", maxTenureYears),
        )),
        JSONObject("isRepeatable", isRepeatable),
        JSONObject("prerequisites", prerequisites.map {
            it.name.normalize()
        }),
        JSONObject("prerequisiteLogic", prerequisiteLogic.name),
        JSONObject("baseChance", baseChance),
        JSONObject("minAge", minAge),
        JSONObject("maxAge", maxAge),
        JSONObject("yearlySkills", listOf(
            JSONObject("legislative", yearlySkills.first),
            JSONObject("executive", yearlySkills.second),
            JSONObject("judicial", yearlySkills.third),
        )),
        JSONObject("description", description),
        JSONObject("connections", connections.map {
            JSONObject(it.key.name.normalize(), it.value)
        }),
    ))

    enum class PrerequisiteLogic(val evaluate: (Collection<PersonalExperience>, Collection<PersonalExperienceType>) -> Boolean) {
        ANY(fun(
            priors: Collection<PersonalExperience>,
            prerequisites: Collection<PersonalExperienceType>
        ) = prerequisites.isEmpty() || prerequisites.find { it in priors { prior -> prior.type } } != null),
        ALL(fun(
            priors: Collection<PersonalExperience>,
            prerequisites: Collection<PersonalExperienceType>
        ) = prerequisites.isEmpty() || prerequisites.find { it !in priors.map { prior -> prior.type } } == null),
        ;

        companion object {
            fun fromString(name: String) = when (name) {
                "any" -> ANY
                "all" -> ALL
                else -> null
            }
        }
    }
}
