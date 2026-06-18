package com.stevenlagoy.presidency.politics

import com.stevenlagoy.presidency.characters.PoliticalActor

data class Campaign(
    var campaigners: List<PoliticalActor> = listOf(),
    var affiliatedParty: Party? = null
) : Issue(
    "Campaign of " + campaigners.joinToString(", ") { it.name.commonName },
    campaigners.joinToString(", ") { it.name.commonName } + " are running with the " + affiliatedParty?.name + " party.",
    setOf()
)
