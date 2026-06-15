package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

class Roadway(
    engine: Engine,
    name: String,
    val code: String,
    val designation: RoadwayDesignation,
    connections: List<Municipality>,
) : Route(engine, name, connections) {

    constructor(engine: Engine, json: JSONObject) : this(
        engine,
        json.get("name", String::class.java),
        json.get("code", String::class.java),
        engine.MAP_MANAGER.ROUTE_MANAGER.matchRoadwayDesignation(json.get("designation", String::class.java)).get(),
        json.get("connections", List::class.java).map { engine.MAP_MANAGER.matchMunicipality(it as String).orElse(null) },
    )

    data class RoadwayDesignation(
        val name: String, // US_highway_major, US_highway_minor, interstate_major, interstate_primary, interstate_auxiliary, state_highway, expressway, local_roadway, street
        val speed: Double,
    )

    override fun toJson() = JSONObject(code, listOf(
        JSONObject("name", name),
        JSONObject("code", code),
        JSONObject("designation", designation.name),
        JSONObject("connections", connections.map { it.fullName })
    ))

    override fun fromJson(json: JSONObject) = this.apply {

    }
}
