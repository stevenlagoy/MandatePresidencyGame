package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable

open class Issue(
    var title: String,
    var description: String,
    var positions: Set<IssuePosition>,
    var subissues: Set<Issue>? = null,
) : JSONSerializable<Issue> {

    override fun toJson() = JSONObject(title, listOf(
        JSONObject("title", title),
        JSONObject("description", description),
        JSONObject("positions", positions.map { it.toJson() }),
        JSONObject("subissues", subissues?.map { it.title })
    ))

    override fun fromJson(json: JSONObject) = apply {
        title = json.requireString("title")
        description = json.requireString("description")
        // TODO
    }

}
