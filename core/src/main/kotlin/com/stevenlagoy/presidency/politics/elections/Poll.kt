package com.stevenlagoy.presidency.politics.elections

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable

class Poll : JSONSerializable<Poll> {

    constructor(json: JSONObject) {
        fromJson(json)
    }

    override fun toJson() = JSONObject(hashCode().toString())

    override fun fromJson(json: JSONObject) = apply {}

}
