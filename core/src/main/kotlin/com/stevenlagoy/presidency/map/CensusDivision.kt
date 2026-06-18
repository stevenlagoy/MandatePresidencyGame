package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc

class CensusDivision(
    ENGINE: Engine,
    name: String = "",
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = emptyMap(),
    descriptors: Set<Descriptor> = emptySet(),
    region: RegionData? = null,
    _censusRegion: CensusRegion? = null,
    states: Set<State> = emptySet(),
) : MapEntity(
    ENGINE,
    name,
    squareMileage,
    population,
    demographics,
    descriptors,
    region
) {
    lateinit var censusRegion: CensusRegion
        internal set

    var states: Set<State> = states
        internal set

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    init {
        if (_censusRegion != null) censusRegion = _censusRegion
    }

    override fun toJson(): JSONObject = super.toJson().merge(
        JSONObject("censusRegion", censusRegion),
        JSONObject("states", states.map { it.fullName })
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        val _censusRegion = engine.MAP_MANAGER.matchCensusRegion(json.get("censusRegion", String::class.java))
        if (_censusRegion.isPresent) censusRegion = _censusRegion.get()
        states = json.get("states", List::class.java).map { engine.MAP_MANAGER.matchState(it as String) }.filter { it.isPresent }.map { it.get() }.toSet()
    }
}
