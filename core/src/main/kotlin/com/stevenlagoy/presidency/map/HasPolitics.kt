package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.election.Election

interface HasPolitics : JSONSerializable<HasPolitics> {
    val partiesPresent: MutableSet<Party>
    val pastElections: MutableSet<Election>
    val partyControlFactors: Set<(party: Party) -> Double>
    fun getPartyControl(): Map<Party, Double> = partiesPresent.associateWith { party -> partyControlFactors.sumOf { it(party) } }
    fun getPartyControl(party: Party): Double = getPartyControl()[party] ?: 0.0
    fun getPartyInControl(): Party = partiesPresent.maxBy { getPartyControl(it) }
    fun getElections(years: IntRange) = pastElections.filter { it.pollsOpenDate.year in years }
    fun getElection(year: Int) = getElections(year..year).firstOrNull()

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("partiesPresent", partiesPresent.map { it.name }),
        JSONObject("pastElectionResults", pastElections.map { it.toJson() }),
    ))

    override fun fromJson(json: JSONObject) = apply {

    }
}
