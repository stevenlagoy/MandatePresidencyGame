package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.Government

/**
 * MapEntity is a geographically-located area of the map which can be identified by a name and some basic characteristics, like population and area.
 */
abstract class MapEntity(val MANAGERS: Engine.Managers): Jsonic<MapEntity> {

    /** Full name of this map entity, possibly including a qualifier like 'State of', 'County', or 'Commonwealth of'. */
    abstract var fullName: String
    /** Common name of this map entity, not including qualifiers like 'State of', 'County', or 'Commonwealth of'.*/
    abstract var commonName: String
    /** Uniquely-identifying name of this map entity, listing its county and state when applicable. */
    abstract var uniqueName: String
    /** Total population of the geographical area of this map entity. */
    abstract var population: Int
    /** Total area in square miles, both land and water, of the geographical area of this map entity. */
    abstract var squareMileage: Double
    /** Set of descriptors of which this map entity is a member. */
    abstract var descriptors: Set<Descriptor>
    /** Demographics of this map entity, mapping a Bloc to a percentage of the population. */
    abstract var demographics: Map<Bloc, Double>
    /** Municipality which serves as the capital of this map entity. Other names may be used for the same concept, like county seats. */
    abstract var capital: Municipality?
    /** Government of this map entity, consisting of executive, legislative, and judicial branches. */
    abstract val government: Government?

    init {
        require(population > 0) { "Population cannot be less than 0"}
        require(squareMileage > 0.0) { "Area cannot be less than 0.0"}
    }

    /** Get the percentage of people living in this map entity identifying with the given bloc. */
    fun getDemographicPercentage(bloc: Bloc) = demographics[bloc] ?: 0.0

    /** Get the number of people living in this map entity who identify with the given bloc. */
    fun getDemographicPopulation(bloc: Bloc) = (getDemographicPercentage(bloc) * population).toInt()

    // JSONIC -------------------------------------------------------------------------------------

    override fun toJson() = JSONObject(uniqueName, listOf(
        JSONObject("full_name",      fullName),
        JSONObject("common_name",    commonName),
        JSONObject("unique_name",    uniqueName),
        JSONObject("population",     population),
        JSONObject("square_mileage", squareMileage),
        JSONObject("descriptors",    descriptors.map { it.name }.toList()),
        JSONObject("demographics",   emptyMap<String, Any>()),
        JSONObject("capital",        capital?.uniqueName),
        JSONObject("government",     government?.toJson())
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        fullName      = json.get("full_name") as String
        commonName    = json.get("common_name") as String
        uniqueName    = json.get("unique_name") as String
        population    = json.get("population") as Int
        squareMileage = json.get("square_mileage") as Double
        descriptors   = json.get("descriptors") as Set<Descriptor> // From MapManager
        demographics  = emptyMap() // From DemographicsManager
        capital       = MANAGERS.MAP_MANAGER.getMunicipalityByUniqueName((json.get("capital") as JSONObject).asString)
    }

    override fun toString() = """[
        fullName: $fullName,
        commonName: $commonName,
        uniqueName: $uniqueName,
        population: $population,
        squareMileage: $squareMileage,
        descriptors: $descriptors,
        demographics: $demographics,
        capital: ${capital?.uniqueName},
    ]""".trimIndent()

}
