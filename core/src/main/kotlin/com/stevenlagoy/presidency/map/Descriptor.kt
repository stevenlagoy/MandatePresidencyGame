package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.demographics.Bloc

class Descriptor (
    engine: Engine,
    var name: String = "",
    var description: String = "",
    var members: List<MapEntity> = emptyList(),
    var effects: Map<Bloc, Double> = emptyMap(),
) : EngineBound(engine), Jsonic<Descriptor> {

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name", name),
        JSONObject("description", description),
        JSONObject("members", members.map { it.name }),
        JSONObject("effects", effects)
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        name = json.get("name", String::class.java)
        description = json.get("description", String::class.java)
        // TODO
    }
}
