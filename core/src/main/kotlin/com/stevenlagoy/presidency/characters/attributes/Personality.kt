package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable

class Personality : JSONSerializable<Personality> {

    override fun fromJson(json: JSONObject): Personality {
        return this
    }

    override fun toJson() = JSONObject()

}
