package com.stevenlagoy.presidency.politics.branches

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Party

class JudicialBranch(
    override val partiesPresent: MutableSet<Party>,
    override val pastElectionResults: MutableList<ElectionResult>,
    override val partyControlFactors: List<(party: Party) -> Double>
) : GovernmentBranch() {
    override fun toJson(): JSONObject? {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject?): GovernmentBranch? {
        TODO("Not yet implemented")
    }
}
