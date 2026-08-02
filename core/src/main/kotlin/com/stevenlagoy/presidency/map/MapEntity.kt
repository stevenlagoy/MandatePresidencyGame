package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.demographics.Bloc
import kotlin.math.roundToInt

/**
 * MapEntity is a geographic area identified by a name, with some basic characteristics like
 * population and area.
 */
abstract class MapEntity(
    engine: Engine,
    name: String = "",
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = emptyMap(),
    /** Set of descriptors of which this map entity is a member. */
    descriptors: Set<Descriptor> = emptySet(),
    region: RegionData? = null,
): JSONSerializable<MapEntity>, EngineBound(engine) {

    /** Uniquely identifying name of this map entity. */
    open var name: String = name
        internal set

    /** Total population of the geographical area of this map entity. */
    var population: Int = population
        set(value) {
            require(value >= 0) { "Population must be non-negative" }
            field = value
        }

    /** Total area in square miles, both land and water, of the geographical area of this map entity. */
    var squareMileage: Double = squareMileage
        internal set

    /** Demographics of this map entity, mapping a Bloc to a percentage of the population. */
    var demographics: Map<Bloc, Double> = demographics
        internal set

    var descriptors: Set<Descriptor> = descriptors
        internal set

    var region: RegionData? = region
        internal set

    fun isPixelWithin(x: Long, y: Long): Boolean {
        return false // TODO
    }

    init {
        require(population >= 0) { "Population must be non-negative" }
        require(squareMileage >= 0.0) { "Area must be non-negative" }
    }

    constructor(engine: Engine, json: JSONObject) : this(engine) { fromJson(json) }

    internal fun addDescriptor(newDescriptor: Descriptor) {
        descriptors = descriptors + newDescriptor
    }

    internal fun addDescriptors(newDescriptors: Collection<Descriptor>) {
        descriptors = descriptors + newDescriptors.toSet()
    }

    /** Get the percentage of people living in this map entity identifying with the given bloc. */
    fun getDemographicPercentage(bloc: Bloc): Double = demographics[bloc] ?: 0.0

    /** Get the number of people living in this map entity who identify with the given bloc. */
    fun getDemographicPopulation(bloc: Bloc): Int = (getDemographicPercentage(bloc) * population).roundToInt()

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name",              name),
        JSONObject("squareMileage",     squareMileage),
        JSONObject("population",        population),
        JSONObject("demographics",      demographics),
        JSONObject("descriptors",       descriptors.map { it.name }.toList()),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        name          = json.requireString("name")
        population    = json.findInt("population") { 0 }!!
        demographics  = emptyMap() // From DemographicsManager
        squareMileage = json.findDouble("squareMileage") { 0.0 }!!
        demographics  = emptyMap()
        descriptors   = json.findArray("descriptors") { emptyList<String>() }!!.asSequence().filterIsInstance<String>().map { engine.MAP_MANAGER.matchDescriptor(it) }.filter { it.isPresent }.map { it.get() }.toSet()
    }
}
