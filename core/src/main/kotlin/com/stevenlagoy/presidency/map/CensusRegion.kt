package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc

class CensusRegion(
    ENGINE: Engine,
    name: String = "",
    squareMileage: Double = 0.0,
    population: Int = 0,
    demographics: Map<Bloc, Double> = emptyMap(),
    descriptors: Set<Descriptor> = emptySet(),
    region: RegionData? = null,
    censusDivisions: Set<CensusDivision> = emptySet(),
) : MapEntity(
    ENGINE,
    name,
    squareMileage,
    population,
    demographics,
    descriptors,
    region,
) {
    var censusDivisions: Set<CensusDivision> = censusDivisions
        internal set

    val states = censusDivisions.flatMap { it.states }.toSet()

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override fun toJson(): JSONObject = super.toJson().merge(
        JSONObject("censusDivisions", censusDivisions.map { it.name })
    )

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        censusDivisions = json.get("censusDivisions", List::class.java).map {
            val division = CensusDivision(engine, it as JSONObject)
            engine.MAP_MANAGER.censusDivisions.add(division)
            division
        }.toSet()
    }
}
