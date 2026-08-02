package com.stevenlagoy.presidency.politics.government

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.election.Election

class JudicialBranch(
    override val partiesPresent: MutableSet<Party> = mutableSetOf(),
    override val pastElections: MutableSet<Election> = mutableSetOf(),
    override val partyControlFactors: Set<(party: Party) -> Double> = emptySet(),
) : GovernmentBranch() {

    var courts: List<Court> = listOf()
        internal set

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
    ))

    override fun fromJson(json: JSONObject) = super.fromJson(json).apply {
        // TODO
    }
}
