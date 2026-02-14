package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.characters.PoliticalActorKT
import com.stevenlagoy.presidency.demographics.BlocKT
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Legislature
import com.stevenlagoy.presidency.politics.Party

class State (
    val FIPS: String,
    override val fullName: String,
    override val commonName: String,
    override var population: Int,
    override val squareMileage: Double,
    override val descriptors: Set<Descriptor>,
    override val demographics: Map<BlocKT, Double>,
    val capital: Municipality? = null,
    val counties: Set<County>? = null,
    val abbreviation: String = commonName.substring(0..2),
    val nickname: String? = null,
    val motto: String? = null,
    val senators: Pair<PoliticalActorKT?, PoliticalActorKT?>? = null,
    val representatives: MutableSet<PoliticalActorKT>? = null,
    val legislature: Set<Legislature>? = null,
    var governor: PoliticalActorKT? = null,
    var lieutenantGovernor: PoliticalActorKT? = null,
    val partiesPresent: Set<Party> = setOf(),
    val pastElections: MutableList<ElectionResult> = mutableListOf(),
) : MapEntity()
{
    val nation: NationJava = NationJava.getInstance()

    val upperHouse: Legislature? = legislature?.find { it.isUpperHouse }

    fun getElectionResults(years: IntRange) = pastElections.filter { it.electionDate.year in years }
    fun getElectionResult(year: Int = 2024) = getElectionResults(year..year).firstOrNull()

    val partyControl: Map<Party, Double> get() {

        val controlFactors = mapOf<String, Double>(
            "EACH_LEGISLATURE_SEAT" to 1.0, // Seats held by party in the legislature
            "HOUSE_MAJORITY" to 15.0, // Majority in any state house
            "UPPER_HOUSE_MAJORITY" to 10.0, // Majority in the upper state house (added to HOUSE_MAJORITY)
            "OVERALL_LEGISLATURE_MAJORITY" to 15.0, // Majority of the legislature overall (added to UPPER_HOUSE_MAJORITY and ...HOUSE_MAJORITY)
            "GOVERNOR" to 25.0, // Governor's affiliated party
            "LIEUTENANT_GOVERNOR" to 5.0, // Lieutenant Governor's affiliated party (some states have same ticket, some separate, for some the position does not exist)
            "TRIFECTA" to 20.0, // Trifecta of Governor, Upper House, and Lower House (added to GOVERNOR and OVERALL_LEGISLATURE_MAJORITY)
            "EACH_SENATOR" to 10.0, // Each federal senator's affiliated party
            "BOTH_SENATORS" to 8.0, // Both federal senators' affiliated party (added to 2 * EACH_SENATOR)
            "EACH_REPRESENTATIVE" to 2.0, // Each federal representative's affiliated party
            "MAJORITY_REPRESENTATIVES" to 7.0, // Majority of federal representatives' affiliated party (added to ...EACH_REPRESENTATIVE)
            "PRESIDENT_FROM_STATE" to 4.0, // President is from State and affiliated with party
            "LAST_ELECTION_MARGIN" to 30.0, // Margin of party victory in recent election (2024)
            "LAST_4_ELECTIONS_MARGIN" to 15.0, // Margin of party victory in last 4 elections (including and since 2012)
            "LAST_12_ELECTIONS_MARGIN" to 5.0, // Margin of party victory in last 12 elections (including and since 1980)
        )

        return partiesPresent.associateWith { party ->
            var control: Double = 0.0
            var trifecta = true
            fun PoliticalActorKT.isAligned(): Boolean = partyAlignment == party
            legislature?.forEach {
                control += it.members.count { member -> member.isAligned() } * controlFactors["EACH_LEGISLATURE_SEAT"]!!
                if(it.isPartyControlled(party)) control += controlFactors["EACH_LEGISLATURE_MAJORITY"]!!
                if(it.isUpperHouse && it.isPartyControlled(party)) control += controlFactors["UPPER_HOUSE_MAJORITY"]!!
            }
            if (legislature != null && legislature.count { it.isPartyControlled(party) } >= legislature.size) control += controlFactors["OVERALL_LEGISLATURE_MAJORITY"]!!
            else trifecta = false
            if (governor?.isAligned()!!) control += controlFactors["GOVERNOR"]!!
            else trifecta = false
            if (lieutenantGovernor?.isAligned()!!) control += controlFactors["LIEUTENANT_GOVERNOR"]!!
            if (trifecta) control += controlFactors["TRIFECTA"]!!

            val numSenators = senators?.toList()?.count { it?.isAligned()!! } ?: 0
            control += numSenators * controlFactors["EACH_SENATOR"]!!
            if (numSenators == 2) controlFactors["BOTH_SENATORS"]!!

            val numRepresentatives = representatives?.count { it.isAligned() } ?: 0
            control += numRepresentatives * controlFactors["EACH_REPRESENTATIVE"]!!
            if (numRepresentatives > representatives?.size!! / 2) control += controlFactors["MAJORITY_REPRESENTATIVES"]!!

            if (nation.getPresident().isAligned() && nation.getPresident().residenceMunicipality.inside(this)) control += controlFactors["PRESIDENT_FROM_STATE"]!!

            // TODO these should not be magic numbers but derived from TimeManager
            control += getElectionResult(2024)?.marginFor(party) * controlFactors["LAST_ELECTION_MARGIN"]!!
            control += getElectionResults(2012..2024).forEach { it.marginFor(party) }.sum() / 4 * controlFactors["LAST_4_ELECTIONS_MARGIN"]!!
            control += getElectionResults(1980..2024).forEach { it.marginFor(party) }.sum() / 12 * controlFactors["LAST_12_ELECTIONS_MARGIN"]!!

            control
        }
    }

}
