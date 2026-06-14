package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Government
import com.stevenlagoy.presidency.politics.Party

class County(
    ENGINE: Engine,
    override val FIPS: String,
    val state: State,
    val color: Int = 0x000000,
    override var fullName: String = "",
    override var commonName: String = "",
    override var uniqueName: String = fullName,
    override var population: Int = 0,
    override var squareMileage: Double = 0.0,
    override var descriptors: Set<Descriptor> = emptySet(),
    override var demographics: Map<Bloc, Double> = emptyMap(),
    override val government: Government? = null,
    override val partiesPresent: MutableSet<Party> = mutableSetOf(),
    override val pastElectionResults: MutableList<ElectionResult> = mutableListOf(),
    override var capital: Municipality? = null,
    val municipalities: MutableSet<Municipality> = mutableSetOf(),
) : MapEntity(ENGINE), HasFIPS, HasPolitics {

    enum class CountyType {
        COUNTY,
        PARISH,
        BOROUGH,
        PLANNING_REGION,
        CONSOLIDATED_CITY_COUNTY,
    }

    constructor(engine: Engine, state: State, json: JSONObject) : this(engine, json.get("FIPS").toString(), state) {
        fromJson(json)
    }

    val countySeat = capital

    var mapRegion: RegionData? = null
        internal set

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
