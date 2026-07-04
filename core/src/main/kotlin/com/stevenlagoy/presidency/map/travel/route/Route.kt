package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.Municipality

abstract class Route(
    engine: Engine,
    name: String,
    connections: List<Municipality>
) : JSONSerializable<Route>, EngineBound(engine) {

    var name = name
        internal set

    open var connections: List<Municipality> = connections
        internal set

    fun connects(to: Municipality) = connections.contains(to)

    override fun fromJson(json: JSONObject) = this.apply {
        name = json.requireString("name", "fullName")
        connections = json.requireArray("connections").mapNotNull { engine.MAP_MANAGER.matchMunicipality(it as String).orElseThrow { IllegalArgumentException("Could not match municipality $it for route $name") } }
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("name", name),
        JSONObject("connections", connections.map { it.qualifiedName })
    ))
}
