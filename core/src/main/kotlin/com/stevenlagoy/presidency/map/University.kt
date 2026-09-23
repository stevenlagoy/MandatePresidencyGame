package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.entities.Place

class University(
    engine: Engine,
    _location: Place? = null,
    fullName: String = "",
    commonName: String = "",
    graduationSize: Int = 0,
) : EngineBound(engine), JSONSerializable<University> {

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    lateinit var location: Place
        internal set

    var fullName: String = fullName
        internal set

    var commonName: String = commonName
        internal set

    var graduationSize: Int = graduationSize
        internal set

    init {
        if (_location != null) location = _location
    }

    override fun toJson() = JSONObject(fullName, listOf(
        JSONObject("location", location.qualifiedName),
        JSONObject("fullName", fullName),
        JSONObject("commonName", commonName),
        JSONObject("graduationSize", graduationSize),
    ))

    override fun fromJson(json: JSONObject) = apply {
        location = engine.MAP_MANAGER.requirePlace(json.requireString("location"))
        fullName = json.requireString("fullName", "full_name", "commonName", "common_name", "name")
        commonName = json.requireString("commonName", "common_name", "fullName", "full_name", "name")
        graduationSize = json.findNumber("graduation_size") { 0 }!!.toInt()
    }
}
