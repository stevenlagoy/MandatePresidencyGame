package com.stevenlagoy.presidency.politics.election

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.politics.IssuePosition

class Ballot (
    engine: Engine,
    var `return`: Map<IssuePosition, Int> = emptyMap()
) : JSONSerializable<Ballot>, EngineBound(engine) {

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override fun toJson() = JSONObject(hashCode().toString(), `return`.map { JSONObject(it.key.title, it.value) })

    override fun fromJson(json: JSONObject) = apply {
        `return` = json.requireJson().filterIsInstance<JSONObject>().associate {
            engine.POLITICS_MANAGER.ISSUE_MANAGER.matchIssuePosition(it.key).get() to (it.value as Number).toInt()
        }.toMap()
    }
}
