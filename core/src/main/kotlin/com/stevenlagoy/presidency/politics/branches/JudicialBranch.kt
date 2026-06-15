package com.stevenlagoy.presidency.politics.branches

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Party

class JudicialBranch(
    override val partiesPresent: MutableSet<Party> = mutableSetOf(),
    override val pastElectionResults: MutableList<ElectionResult> = mutableListOf(),
    override val partyControlFactors: List<(party: Party) -> Double> = emptyList(),
) : GovernmentBranch() {
    override fun toJson(): JSONObject? {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject?): GovernmentBranch? {
        TODO("Not yet implemented")
    }
}
