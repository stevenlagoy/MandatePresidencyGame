package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound

class IssuePosition (
    engine: Engine,
    var issue: Issue,
    var title: String = "",
    var description: String = title,
    var alignment: PoliticalAlignment = PoliticalAlignment(),
) : JSONSerializable<IssuePosition>, EngineBound(engine) {

    constructor(engine: Engine, issue: Issue, json: JSONObject) : this(engine, issue) {
        fromJson(json)
    }

    override fun toJson() = JSONObject(title, listOf(
        JSONObject("issue", issue.title),
        JSONObject("title", title),
        JSONObject("description", description),
        alignment.toJson().apply { key = "alignment" },
    ))

    override fun fromJson(json: JSONObject) = apply  {
        issue = engine.POLITICS_MANAGER.ISSUE_MANAGER.matchIssue(json.requireString("issue")).get()
        title = json.requireString("title")
        description = json.requireString("description")
        alignment.fromJson(json.requireJson("alignment"))
    }

}
