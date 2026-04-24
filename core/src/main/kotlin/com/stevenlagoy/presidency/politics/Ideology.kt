package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

data class Ideology(
    var name: String,
    var description: String,
    var alignment: PoliticalAlignment
) : Jsonic<Ideology> {
    var parties = mutableSetOf<Party>()
    var interestGroups = mutableSetOf<InterestGroup>()

    var relatedIdeologies = listOf<Ideology>()
    var opposingIdeologies = listOf<Ideology>()

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("name", name),
        JSONObject("description", description),
        JSONObject("alignment", alignment),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        name = json.get("name") as String
        description = json.get("description") as String
        alignment.fromJson(json.get("alignment") as JSONObject)
    }

}
