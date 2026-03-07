package com.stevenlagoy.presidency.map

import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Legislature
import com.stevenlagoy.presidency.politics.Party

class Municipality(
    override val FIPS: String,
    override val fullName: String,
    override val commonName: String,
    override var population: Int,
    override val squareMileage: Double,
    override val descriptors: Set<Descriptor>,
    override val demographics: Map<Bloc, Double>,
    override val legislature: Set<Legislature>? = setOf(),
    override val partiesPresent: Set<Party> = setOf(),
    override val pastElections: MutableList<ElectionResult> = mutableListOf(),
    override val capital: Municipality? = null,
) : MapEntity(), HasFIPS, HasPolitics
{
    val nation: Nation = Nation

    override val partyControl: Map<Party, Double> get() {
        val controlFactors
    }

}
