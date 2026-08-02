package com.stevenlagoy.presidency.politics.government

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.election.Election

class ExecutiveBranch(
    val executives: MutableList<PoliticalActor> = mutableListOf(),
    var chiefExecutive: PoliticalActor? = null,
    val chiefExecutiveTitle: String? = null,
    var deputyExecutive: PoliticalActor? = null,
    val deputyExecutiveTitle: String? = null,
    override val pastElections: MutableSet<Election> = mutableSetOf()
) : GovernmentBranch() {

    override val partiesPresent: MutableSet<Party>
        get() = setOfNotNull(chiefExecutive?.partyAffiliation, deputyExecutive?.partyAffiliation).toMutableSet()

    override val partyControlFactors: Set<(party: Party) -> Double> = setOf(
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

    override fun toJson() = super.toJson().merge(
        JSONObject("executives", executives.map { it.name.indexedName }),
        JSONObject("chiefExecutive", chiefExecutive?.name?.indexedName),
        JSONObject("chiefExecutiveTitle", chiefExecutiveTitle),
        JSONObject("deputyExecutive", deputyExecutive?.name?.indexedName),
        JSONObject("deputyExecutiveTitle", deputyExecutiveTitle),
    )

    override fun fromJson(json: JSONObject) = super.fromJson(json).apply {
        // TODO
    }
}
