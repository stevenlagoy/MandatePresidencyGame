package com.stevenlagoy.presidency.politics

import com.stevenlagoy.presidency.characters.PoliticalActor

class Campaign(
    var campaigners: List<PoliticalActor> = listOf(),
    var affiliatedParty: Party? = null
) : Issue(
    "Campaign of " + campaigners.map { it.name.commonName }.joinToString(", "),
    campaigners.joinToString(", ") { it.name.commonName } + " are running with the " + affiliatedParty?.name + " party.",
    setOf()
)
