package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable

data class Ideology(
    var name: String,
    var description: String,
    var alignment: PoliticalAlignment
) : JSONSerializable<Ideology> {
    var parties = mutableSetOf<Party>()
    var interestGroups = mutableSetOf<InterestGroup>()

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name", name),
        JSONObject("description", description),
        JSONObject("alignment", alignment),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        name = json.requireString("name")
        description = json.requireString("description")
        alignment.fromJson(json.requireJson("alignment"))
    }

}
