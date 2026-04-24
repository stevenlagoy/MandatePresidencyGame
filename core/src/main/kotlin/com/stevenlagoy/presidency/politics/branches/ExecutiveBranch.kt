package com.stevenlagoy.presidency.politics.branches

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Party

class ExecutiveBranch(
    val executives: MutableList<PoliticalActor>,
    var chiefExecutive: PoliticalActor?,
    val chiefExecutiveTitle: String?,
    var deputyExecutive: PoliticalActor?,
    val deputyExecutiveTitle: String?,
    override val pastElectionResults: MutableList<ElectionResult> = mutableListOf()
) : GovernmentBranch() {

    override val partiesPresent: MutableSet<Party>
        get() = setOfNotNull(chiefExecutive?.partyAffiliation, deputyExecutive?.partyAffiliation).toMutableSet()

    override val partyControlFactors: List<(party: Party) -> Double> = listOf(
        // Each executive
        { party -> 0.5 *
            (executives.count { it.partyAffiliation == party }.toDouble() / executives.size)
        },
        // Bonus for chief executive
        { party -> 0.4 *
            (if (chiefExecutive?.partyAffiliation == party) 1.0 else 0.0)
        },
        // Bonus for deputy executive
        { party -> 0.1 *
            (if (deputyExecutive?.partyAffiliation == party) 1.0 else 0.0)
        },
    )

    override fun toJson(): JSONObject? {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject?): GovernmentBranch? {
        TODO("Not yet implemented")
    }
}
