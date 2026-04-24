package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.map.HasPolitics
import com.stevenlagoy.presidency.politics.voting.Election

import com.stevenlagoy.jsonic.Jsonic

class Chamber(
    val chamberName: String,
    val memberTitle: String,
    val federalLevel: FederalLevel,
    val isUpperChamber: Boolean = false,
    val termLength: Int,
    var nextElection: Election,
    override val pastElectionResults: MutableList<ElectionResult>,
    val seats: Int, // May be vacancies, so members.size does not always match seats
    var members: MutableList<PoliticalActor>,
) : HasPolitics, Jsonic<Chamber> {

    fun replaceMember(oldMember: PoliticalActor, newMember: PoliticalActor) {
        members.remove(oldMember)
        members.add(newMember)
    }

    override val partiesPresent: MutableSet<Party> get() = members.mapNotNull { it.partyAffiliation }.toMutableSet()

    fun isPartyMajority(party: Party): Boolean = members.count { it.partyAffiliation == party } > (seats / 2.0)

    override val partyControlFactors: List<(party: Party) -> Double> = listOf(
        // Proportion of seats
        { party -> 0.75 *
            (members.count { it.partyAffiliation == party }.toDouble() / seats)
        },
        // Party majority
        { party -> 0.25 *
            if (isPartyMajority(party)) 1.0 else 0.0
        },
    )

    override fun toJson() = JSONObject(chamberName, mapOf(
        "chamber_name" to chamberName,
        "member_title" to memberTitle,
        "federal_level" to federalLevel.toString(),
        "is_upper_chamber" to isUpperChamber,
        "term_length" to termLength,
        "seats" to seats,
        "members" to members.map { it.toString() }
    ))

    override fun fromJson(json: JSONObject) = this.apply {
    }
}
