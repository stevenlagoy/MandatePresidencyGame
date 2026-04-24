package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Party

interface HasPolitics {
    val partiesPresent: MutableSet<Party>
    val pastElectionResults: MutableList<ElectionResult>
    val partyControlFactors: List<(party: Party) -> Double>
    fun getPartyControl(): Map<Party, Double> = partiesPresent.associateWith { party -> partyControlFactors.sumOf { it(party) } }
    fun getPartyControl(party: Party): Double = getPartyControl()[party] ?: 0.0
    fun getElectionResults(years: IntRange) = pastElectionResults.filter { it.electionDate.year in years }
    fun getElectionResult(year: Int) = getElectionResults(year..year).firstOrNull()
}
