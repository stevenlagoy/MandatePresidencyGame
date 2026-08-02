package com.stevenlagoy.presidency.politics.election

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable

class Poll : JSONSerializable<Poll> {

    constructor(json: JSONObject) {
        fromJson(json)
    }

    override fun toJson() = JSONObject(hashCode().toString())

    override fun fromJson(json: JSONObject) = apply {}

}
