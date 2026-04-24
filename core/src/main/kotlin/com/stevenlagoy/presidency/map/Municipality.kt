package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Government
import com.stevenlagoy.presidency.politics.Party
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Municipality(
    MANAGERS: Engine.Managers,
    val state: State,
    override val FIPS: String,
    override var fullName: String = "",
    override var commonName: String = "",
    override var uniqueName: String = "",
    override var population: Int = 0,
    override var squareMileage: Double = 0.0,
    override var descriptors: Set<Descriptor> = emptySet(),
    override var demographics: Map<Bloc, Double> = emptyMap(),
    override val government: Government? = null,
    override val partiesPresent: MutableSet<Party> = mutableSetOf(),
    override val pastElectionResults: MutableList<ElectionResult> = mutableListOf(),
) : MapEntity(MANAGERS), HasFIPS, HasPolitics
{
    constructor(MANAGERS: Engine.Managers, state: State, json: JSONObject) : this(MANAGERS, state, json.get("FIPS").toString()) {
        fromJson(json)
    }

    val nation: Nation = Nation

    override var capital: Municipality? = this

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

    override fun getPartyControl(): Map<Party, Double> {
        return mapOf()
    }

    override fun toJson() = JSONObject(uniqueName, super.toJson().asList + listOf(
        JSONObject("FIPS", FIPS),
    ))

    override fun fromJson(json: JSONObject) = this.apply {

    }

}
