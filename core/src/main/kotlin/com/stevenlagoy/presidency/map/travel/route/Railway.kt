package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

class Railway(
    engine: Engine,
    name: String,
    connections: List<Municipality>,
) : Route(engine, name, connections) {

    constructor(engine: Engine, json: JSONObject) : this(engine, "", listOf())

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name", name),
        JSONObject("connections", connections.map { it.fullName })
    ))

    override fun fromJson(json: JSONObject?): Railway? {
        TODO("Not yet implemented")
    }


}
