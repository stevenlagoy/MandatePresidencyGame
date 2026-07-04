package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable

class Poll : JSONSerializable<Poll> {

    override fun toJson() = JSONObject(hashCode().toString())

    override fun fromJson(json: JSONObject) = apply {}

}
