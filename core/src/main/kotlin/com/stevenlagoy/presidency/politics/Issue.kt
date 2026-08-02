package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import kotlin.jvm.optionals.getOrNull

open class Issue(
    engine: Engine,
    var title: String,
    var description: String,
    var positions: Set<IssuePosition>,
    var subissues: Set<Issue>? = null,
) : JSONSerializable<Issue>, EngineBound(engine) {

    override fun toJson() = JSONObject(title, listOf(
        JSONObject("title", title),
        JSONObject("description", description),
        JSONObject("positions", positions.map { it.toJson() }),
        JSONObject("subissues", subissues?.map { it.title })
    ))

    override fun fromJson(json: JSONObject) = apply {
        title = json.requireString("title")
        description = json.requireString("description")
        positions = json.requireJson("positions").requireArray().map { IssuePosition(engine, this, it as JSONObject) }.toSet()
        subissues = json.findJson("subissues").get().let { it.requireArray().mapNotNull { issue -> engine.POLITICS_MANAGER.ISSUE_MANAGER.matchIssue(issue as String).getOrNull() }}.toSet()
    }

}
