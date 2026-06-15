package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

class Seaport(
    engine: Engine,
    name: String,
    val location: Municipality,
    connections: List<Municipality>,
) : Route(engine, name, connections) {

    constructor(engine: Engine, json: JSONObject) : this (
        engine,
        json.get("name", String::class.java),
        engine.MAP_MANAGER.matchMunicipality(json.get("location", String::class.java)).get(),
        (json.get("connections", List::class.java) as List<String>).map { engine.MAP_MANAGER.matchMunicipality(it).orElse(null) },
    )

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name", name),
    ))

    override fun fromJson(json: JSONObject?): Route? {
        TODO("Not yet implemented")
    }

}
