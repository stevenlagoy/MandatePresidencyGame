package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

class Seaport(
    ENGINE: Engine,
    name: String,
    val location: Municipality,
    connections: List<Municipality>,
) : Route(ENGINE, name, connections) {

    constructor(ENGINE: Engine, json: JSONObject) : this (
        ENGINE,
        json.get("name", String::class.java),
        ENGINE.MAP_MANAGER.matchMunicipalityByName(json.get("location", String::class.java)).get(),
        (json.get("connections", List::class.java) as List<String>).map { ENGINE.MAP_MANAGER.matchMunicipalityByName(it).orElse(null) },
    )

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name", name),
    ))

    override fun fromJson(json: JSONObject?): Route? {
        TODO("Not yet implemented")
    }

}
