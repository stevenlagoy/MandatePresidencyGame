package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.election.Election
import com.stevenlagoy.presidency.politics.government.Government

/**
 * HasGovernment defines a political body which has a government in power, a history of elections,
 * a list of politically relevant parties, and factors by which to evaluate the clout or influence
 * of a party. Parties are tracked separately to the government, as there are many cases of
 * political parties with some amount of influence which are not present in the government
 * (especially third parties).
 */
interface HasGovernment : HasPartyPresence {
    val government: Government
    var capital: Municipality
    override val partiesPresent: MutableSet<Party>
    override val partyCloutFactors: Set<(party: Party) -> Double>
    val elections: MutableSet<Election>

    fun getElections(year: Int): Set<Election> = getElections(year..year)
    fun getElections(years: IntRange): Set<Election> = elections.filter { it.pollsOpenDate.year in years }.toSet()
    fun getElection(year: Int): Election? = getElections(year).firstOrNull()


    fun politicsToJson() = JSONObject("politics", listOf(
        JSONObject("capital", capital.fullName),
        JSONObject("government", government.toJson()),
        JSONObject("electionResults", elections.map { JSONObject(it.name, it.toJson()) }),
        JSONObject("partiesPresent", partiesPresent.map { it.name })
    ))

    fun populateFromJson(json: JSONObject, engine: Engine) {
        if (json.hasKey("government")) government.fromJson(json.requireJson("government"))
        capital = engine.MAP_MANAGER.matchMunicipality(json.findString(listOf("capital", "countySeat", "county_seat")) { "" }!!).get()
        elections.clear()
        json.requireJson("elections").forEach { entry -> elections.add(Election(engine, entry as JSONObject)) }
        partiesPresent.clear()
        json.requireArray("elections").forEach { entry -> partiesPresent.add(engine.POLITICS_MANAGER.PARTY_MANAGER.matchParty(entry as String).get()) }
    }
}
