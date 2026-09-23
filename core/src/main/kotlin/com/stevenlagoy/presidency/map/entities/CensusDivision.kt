package com.stevenlagoy.presidency.map.entities

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.map.Descriptor
import com.stevenlagoy.presidency.map.MapRegion

class CensusDivision(
    engine: Engine,
    name: String = "",
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = emptyMap(),
    descriptors: Set<Descriptor> = emptySet(),
    region: MapRegion? = null,
    _censusRegion: CensusRegion? = null,
    states: Set<StateEquivalent> = emptySet(),
) : MapEntity(
    engine,
    name,
    squareMileage,
    population,
    demographics,
    descriptors,
    region
) {
    lateinit var censusRegion: CensusRegion
        internal set

    var states: Set<StateEquivalent> = states
        internal set

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    init {
        if (_censusRegion != null) censusRegion = _censusRegion
    }

    fun addState(state: StateEquivalent) {
        states = (states.toMutableSet() + state).toSet()
    }

    override fun toJson(): JSONObject = super.toJson().merge(
        JSONObject("censusRegion", censusRegion.name),
        JSONObject("states", states.map { it.fullName })
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        val _censusRegion = engine.MAP_MANAGER.matchCensusRegion(json.requireString("censusRegion"))
        if (_censusRegion.isPresent) censusRegion = _censusRegion.get()
        states = json.findArray("states") { emptyList<String>() }!!.map { engine.MAP_MANAGER.matchStateEquivalent(it as String) }.filter { it.isPresent }.map { it.get() }.toSet()
    }
}
