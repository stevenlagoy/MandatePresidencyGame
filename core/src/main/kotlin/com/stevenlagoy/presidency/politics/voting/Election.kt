package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.presidency.characters.PoliticalActor

class Election : Vote() {

    override var issues: List<Campaign> = emptyList()

    var targetPosition: GovernmentPosition

    var isPartisan: Boolean = false

    var encumbent: PoliticalActor? = null

}
