package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.presidency.politics.Issue

data class VotingReturn (
    var `return`: Map<Issue, Int> = emptyMap()
)
