package com.stevenlagoy.presidency.politics.branches

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Chamber
import com.stevenlagoy.presidency.politics.Party

class LegislativeBranch(
    val title: String,
    val chambers: Set<Chamber>,
    override val partiesPresent: Set<Party> = setOf(),
    override val pastElectionResults: MutableList<ElectionResult> = mutableListOf()
) : GovernmentBranch() {

    val upperChamber: Chamber? get() = chambers.find { it.isUpperChamber }

    fun isPartyMajority(party: Party): Boolean = chambers.all { it.isPartyMajority(party) }

    override val partyControlFactors: List<(party: Party) -> Double> = listOf(
        // Each chamber
        { party -> 0.8 *
            (chambers.fold(0.0) { acc, it -> acc + it.getPartyControl(party) } / chambers.size)
        },
        // Bonus for upper chamber
        { party -> 0.2 *
            (upperChamber?.getPartyControl(party) ?: 0.0)
        }
    )

    override fun toJson() = JSONObject(title, mapOf(
        "title" to title,
        "chambers" to chambers.map { it.toJson() },
    ))

    override fun fromJson(json: JSONObject) = this.apply {

    }

}
