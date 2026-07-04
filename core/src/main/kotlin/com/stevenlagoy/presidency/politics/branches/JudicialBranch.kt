package com.stevenlagoy.presidency.politics.branches

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Party

class JudicialBranch(
    override val partiesPresent: MutableSet<Party> = mutableSetOf(),
    override val pastElectionResults: MutableList<ElectionResult> = mutableListOf(),
    override val partyControlFactors: List<(party: Party) -> Double> = emptyList(),
) : GovernmentBranch() {

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("partiesPresent", partiesPresent.map { it.name }),
        JSONObject("pastElectionResults", pastElectionResults.map { it.toJson() }),
        JSONObject("partyControlFactors") // TODO
    ))

    override fun fromJson(json: JSONObject) = apply {
        // TODO
    }
}
