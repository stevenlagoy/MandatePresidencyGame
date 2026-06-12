package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.politics.GovernmentPosition
import com.stevenlagoy.presidency.politics.Issue

class Election(
    var targetPosition: GovernmentPosition
) : Vote(), Jsonic<Election> {

    constructor(json: JSONObject) : this(json.get("targetPosition") as GovernmentPosition)

    override var issues: List<Issue> = emptyList()

    var isPartisan: Boolean = false

    var encumbent: PoliticalActor? = null

    override fun toJson(): JSONObject? {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject?): Election? {
        TODO("Not yet implemented")
    }
}
