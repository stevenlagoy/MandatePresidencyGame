package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.politics.Campaign
import com.stevenlagoy.presidency.politics.GovernmentPosition
import com.stevenlagoy.presidency.politics.Issue

class Election(
    var targetPosition: GovernmentPosition
) : Vote() {

    override var issues: List<Issue> = emptyList()

    var isPartisan: Boolean = false

    var encumbent: PoliticalActor? = null

}
