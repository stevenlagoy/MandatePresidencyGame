package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.demographics.Bloc
import kotlin.math.roundToInt

/**
 * MapEntity is a geographically-located area of the map which can be identified by a name and some basic characteristics, like population and area.
 */
abstract class MapEntity(
    engine: Engine,
    name: String,
    squareMileage: Double,
    /** Total population of the geographical area of this map entity. */
    var population: Int,
    demographics: Map<Bloc, Double>,
    /** Set of descriptors of which this map entity is a member. */
    var descriptors: Set<Descriptor>,
    region: RegionData?,
): Jsonic<MapEntity>, EngineBound(engine) {
    init {
        require(population >= 0) { "Population must be non-negative" }
        require(squareMileage >= 0.0) { "Area must be non-negative" }
    }

    /** Uniquely-identifying name of this map entity, listing its county and state when applicable. */
    open var name: String = name
        internal set

    /** Total area in square miles, both land and water, of the geographical area of this map entity. */
    var squareMileage: Double = squareMileage
        internal set

    /** Demographics of this map entity, mapping a Bloc to a percentage of the population. */
    var demographics: Map<Bloc, Double> = demographics
        internal set

    var region: RegionData? = region
        internal set

    fun isPixelWithin(x: Long, y: Long): Boolean {
        return false // TODO
    }

    /** Get the percentage of people living in this map entity identifying with the given bloc. */
    fun getDemographicPercentage(bloc: Bloc) = demographics[bloc] ?: 0.0

    /** Get the number of people living in this map entity who identify with the given bloc. */
    fun getDemographicPopulation(bloc: Bloc) = (getDemographicPercentage(bloc) * population).roundToInt()

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name",           name),
        JSONObject("population",     population),
        JSONObject("square_mileage", squareMileage),
        JSONObject("descriptors",    descriptors.map { it.name }.toList()),
        JSONObject("demographics",   demographics),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        try {
            name          = json.get("name", String::class.java)
            population    = json.get("population", Number::class.java).toInt()
            demographics   = emptyMap() // From DemographicsManager
            squareMileage = json.get("square_mileage", Number::class.java).toDouble()
            descriptors   = if (json.get("descriptors") != null)
                json.get("descriptors", List::class.java).map { engine.MAP_MANAGER.matchDescriptor(it as String) }
                    .filter { it.isPresent }.map { it.get() }.toSet()
                else emptySet()
        } catch (e: Exception) {
            println(json)
        }
    }
}
