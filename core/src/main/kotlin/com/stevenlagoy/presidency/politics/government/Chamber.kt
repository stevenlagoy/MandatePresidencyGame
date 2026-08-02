package com.stevenlagoy.presidency.politics.government

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.map.HasPolitics
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.election.Election

class Chamber(
    chamberName: String,
    memberTitle: String,
    federalLevel: FederalLevel,
    isUpperChamber: Boolean = false,
    termLength: Int,
    var nextElection: Election,
    pastElectionResults: MutableList<ElectionResult>,
    seats: Int,
    var members: MutableList<PoliticalActor>,
) : HasPolitics {

    var chamberName = chamberName
        internal set

    var memberTitle = memberTitle
        internal set

    var federalLevel = federalLevel
        internal set

    var isUpperChamber = isUpperChamber
        internal set

    var termLength = termLength
        internal set

    override var pastElectionResults = pastElectionResults
        internal set

    var seats = seats // May be vacancies, so members.size does not always match number of seats
        internal set

    override val partiesPresent: MutableSet<Party>
        get() = members.mapNotNull { it.partyAffiliation }.toMutableSet()

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

    fun replaceMember(oldMember: PoliticalActor, newMember: PoliticalActor) {
        members.remove(oldMember)
        members.add(newMember)
    }

    fun isPartyMajority(party: Party): Boolean = members.count { it.partyAffiliation == party } > (seats / 2.0)

    override fun toJson() = JSONObject(chamberName, listOf(
        JSONObject("chamberName", chamberName),
        JSONObject("memberTitle", memberTitle),
        JSONObject("federalLevel", federalLevel.toString()),
        JSONObject("isUpperChamber", isUpperChamber),
        JSONObject("termLength", termLength),
        JSONObject("seats", seats),
        JSONObject("members", members.map { it.toString() }),
    ))

    override fun fromJson(json: JSONObject) = apply {
        chamberName = json.requireString("chamberName", "chamber_name")
        memberTitle = json.requireString("memberTitle", "member_title")
        federalLevel = FederalLevel.valueOf(json.requireString("federalLevel", "federal_level"))
        isUpperChamber = json.requireBoolean("isUpperChamber")
        termLength = json.requireInt("termLength")
        pastElectionResults.clear()
        pastElectionResults.addAll(json.requireArray("pastElectionResults", "past_election_results").filterIsInstance<JSONObject>().map {
            ElectionResult(it)
        })
        seats = json.requireInt("seats")
    }
}
