package com.stevenlagoy.presidency.map.travel.route

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.map.Municipality

class Roadway(
    engine: Engine,
    name: String = "",
    code: String = "",
    designation: RoadwayDesignation = RoadwayDesignation.LOCAL_ROADWAY,
    connections: List<Municipality> = listOf(),
) : Route(engine, name, connections) {

    var code: String = code
        internal set

    var designation: RoadwayDesignation = designation
        internal set

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    enum class RoadwayDesignation(val speed: Double) {
        US_HIGHWAY_MAJOR(65.0),
        US_HIGHWAY_MINOR(60.0),
        INTERSTATE_MAJOR(75.0),
        INTERSTATE_PRIMARY(70.0),
        INTERSTATE_AUXILIARY(65.0),
        STATE_HIGHWAY(70.0),
        EXPRESSWAY(65.0),
        LOCAL_ROADWAY(55.0),
        STREET(35.0),
    }

    override fun toJson() = JSONObject(code, listOf(
        JSONObject("name", name),
        JSONObject("code", code),
        JSONObject("designation", designation.name),
        JSONObject("connections", connections.map { it.fullName })
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        code = json.requireString("code")
        designation = RoadwayDesignation.valueOf(json.requireString("designation").uppercase().replace(Regex("[^A-Z]"), "_"))
    }
}
