package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Chamber
import com.stevenlagoy.presidency.politics.Government
import com.stevenlagoy.presidency.politics.Party

class County(
    managers: Engine.Managers,
    override val FIPS: String,
    override var fullName: String = "",
    override var commonName: String = "",
    override var uniqueName: String = fullName,
    val state: State,
    override var population: Int = 0,
    override var squareMileage: Double = 0.0,
    override var descriptors: Set<Descriptor> = emptySet(),
    override var demographics: Map<Bloc, Double> = emptyMap(),
    override val government: Government,
    override val partiesPresent: Set<Party> = setOf(),
    override val pastElectionResults: MutableList<ElectionResult> = mutableListOf(),
    override var capital: Municipality? = null,
    val municipalities: MutableSet<Municipality> = mutableSetOf(),
) : MapEntity(managers), HasFIPS, HasPolitics {

    val countySeat = capital

    override val partyControlFactors: List<(party: Party) -> Double> = listOf(
        // Last election margin
        { party -> 30.0 *
            (getElectionResult(2024)?.getMarginForParty(party) ?: 0.0)
        },
        // Average last 4 elections margin
        { party -> 15.0 *
            (getElectionResults(2012..2024).fold(0.0) { acc, it -> acc + it.getMarginForParty(party)}) / 4
        },
        // Average last 12 elections margin
        { party -> 5.0 *
            (getElectionResults(1976..2024).fold(0.0) { acc, it -> acc + it.getMarginForParty(party)}) / 12
        },
    )

    override fun toJson(): JSONObject {
        val superJson = super.toJson()
        return superJson
    }

    override fun fromJson(json: JSONObject): County {
        return this
    }
}
