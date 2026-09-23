package com.stevenlagoy.presidency.politics.government

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.HasPolitics
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.elections.Election

class Chamber(
    engine: Engine,
    chamberName: String,
    memberTitle: String,
    federalLevel: FederalLevel,
    isUpperChamber: Boolean = false,
    termLength: Int,
    var nextElection: Election,
    pastElections: MutableSet<Election>,
    seats: Int,
    var members: MutableSet<PoliticalActor>,
) : HasPolitics, EngineBound(engine) {

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

    override var pastElections = pastElections
        internal set

    var seats = seats // May be vacancies, so members.size does not always match number of seats
        internal set

    override val partiesPresent: MutableSet<Party>
        get() = members.mapNotNull { it.partyAffiliation }.toMutableSet()

    override val partyControlFactors: Set<(Party) -> Double> = setOf(
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
        pastElections.clear()
        pastElections.addAll(json.requireArray("pastElectionResults", "past_election_results").filterIsInstance<JSONObject>().map {
            Election(engine, it)
        })
        seats = json.requireInt("seats")
    }
}
