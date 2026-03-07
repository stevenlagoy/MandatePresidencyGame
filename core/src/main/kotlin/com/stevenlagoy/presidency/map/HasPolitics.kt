package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Legislature
import com.stevenlagoy.presidency.politics.Party

interface HasPolitics {
    val legislature: Set<Legislature>?
    val upperHouse: Legislature? get() = legislature?.find { it.isUpperHouse }
    val partiesPresent: Set<Party>
    val pastElections: MutableList<ElectionResult>
    val partyControlFactors: List<(party: Party) -> Double>
    fun getPartyControl(): Map<Party, Double> {
        return partiesPresent.associateWith { party -> partyControlFactors.sumOf { it(party) } }
    }
    fun getPartyControl(party: Party): Double = getPartyControl()[party] ?: 0.0
    fun getElectionResults(years: IntRange) = pastElections.filter { it.electionDate.year in years }
    fun getElectionResult(year: Int) = getElectionResults(year..year).firstOrNull()
}
