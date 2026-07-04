package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.politics.IssuePosition

class IssuePositionMap(
    val positions: MutableMap<IssuePosition, StanceValue>
) : JSONSerializable<IssuePositionMap> {

    data class StanceValue(
        var trueStance: IssuePosition,
        var salience: Double,
        var publicStance: IssuePosition,
    ) : JSONSerializable<StanceValue> {

        override fun toJson() = JSONObject(hashCode().toString(), listOf(
            JSONObject("trueStance", trueStance.title),
            JSONObject("salience", salience),
            JSONObject("publicStance", publicStance.title),
        ))

        override fun fromJson(json: JSONObject) = apply {
            // TODO politics manager should help
        }
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("positions", positions.map { JSONObject(it.key.title, it.value.toJson()) })
    ))

    override fun fromJson(json: JSONObject) = apply {
        
    }
}
