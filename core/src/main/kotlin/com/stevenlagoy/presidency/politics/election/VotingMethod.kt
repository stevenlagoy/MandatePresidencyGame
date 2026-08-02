package com.stevenlagoy.presidency.politics.election

import com.stevenlagoy.jsonic.JSONObject

sealed class VotingMethod {

    fun toJson() = JSONObject("votingMethod", this::class.simpleName!!)

    fun fromJson(json: JSONObject) = when (json.requireString("votingMethod".lowercase())) {
        "primary" -> Primary()
        "caucus"  -> Caucus()
        else      -> Primary()
    }

    class Primary : VotingMethod()

    class Caucus: VotingMethod()
}
