package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

class Railway(
    ENGINE: Engine,
    name: String,
    connections: List<Municipality>,
) : Route(ENGINE, name, connections) {

    constructor(ENGINE: Engine, json: JSONObject) : this(ENGINE, "", listOf())

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name", name),
        JSONObject("connections", connections.map { it.uniqueName })
    ))

    override fun fromJson(json: JSONObject?): Railway? {
        TODO("Not yet implemented")
    }


}
