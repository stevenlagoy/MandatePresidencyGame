package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

class Seaport(
    engine: Engine,
    name: String = "",
    location: Municipality? = null,
    connections: List<Municipality> = listOf(),
) : Route(engine, name, connections) {

    var location: Municipality? = location
        internal set

    constructor(engine: Engine, json: JSONObject) : this (engine) {
        fromJson(json)
    }

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name", name),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        location = engine.MAP_MANAGER.matchMunicipality(json.requireString("location")).orElseThrow { IllegalArgumentException("Could not find location for seaport $name") }
    }

}
