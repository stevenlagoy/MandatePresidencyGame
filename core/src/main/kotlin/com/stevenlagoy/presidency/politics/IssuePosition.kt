package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound

class IssuePosition (
    engine: Engine,
    var issue: Issue,
    var title: String,
    var description: String = title,
    var alignment: IntArray = IntArray(2),
) : JSONSerializable<IssuePosition>, EngineBound(engine) {

    override fun toJson() = JSONObject(title, listOf(
        JSONObject("issue", issue.title),
        JSONObject("title", title),
        JSONObject("description", description),
        JSONObject("alignment", alignment.toList()),
    ))

    override fun fromJson(json: JSONObject) = apply  {
        issue = engine.POLITICS_MANAGER.ISSUE_MANAGER.matchIssue(json.requireString("issue")).get()
        title = json.requireString("title")
        description = json.requireString("description")
        alignment = IntArray(2).also { it[0] = (json.requireArray("alignment")[0] as Int); it[1] = (json.requireArray("alignment")[1] as Int) }
    }

}
