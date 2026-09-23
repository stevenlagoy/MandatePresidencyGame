package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.politics.Party

interface HasPartyPresence {
    val partiesPresent: Set<Party>
    val partyCloutFactors: Set<(Party) -> Double>

    fun getPartyClout(): Map<Party, Double> = partiesPresent.associateWith { party -> partyCloutFactors.sumOf { it(party) } }
    fun getPartyClout(party: Party): Double = partyCloutFactors.sumOf { it(party) }
    fun getPartyInControl() = partiesPresent.maxBy { getPartyClout(it) }
}
