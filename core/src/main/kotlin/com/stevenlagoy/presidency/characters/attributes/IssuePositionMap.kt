package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.politics.IssuePosition

class IssuePositionMap(
    val positions: MutableMap<IssuePosition, StanceValue>
) : Jsonic<IssuePositionMap> {

    class StanceValue(
        trueStance: IssuePosition,
        salience: Double,
        publicStance: IssuePosition,
    )

    override fun toJson(): JSONObject? {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject?): IssuePositionMap? {
        TODO("Not yet implemented")
    }
}
