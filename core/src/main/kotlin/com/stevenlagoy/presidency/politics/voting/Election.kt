package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.politics.GovernmentPosition
import com.stevenlagoy.presidency.politics.Issue

class Election(
    enigne: Engine,
    _targetPosition: GovernmentPosition? = null
) : Vote(enigne) {

    lateinit var targetPosition: GovernmentPosition

    init {
        if (_targetPosition != null) targetPosition = _targetPosition
    }

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override var issues: List<Issue> = emptyList()

    var isPartisan: Boolean = false

    var encumbent: PoliticalActor? = null

    override fun toJson(): JSONObject {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject) = super.fromJson(json).apply {
        TODO()
    }
}
