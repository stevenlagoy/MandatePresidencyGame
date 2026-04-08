package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Chamber
import com.stevenlagoy.presidency.politics.Party

class State (
    managers: Engine.Managers,
    override val FIPS: String,
    override var fullName: String = "",
    override var commonName: String = "",
    override var uniqueName: String = fullName,
    var abbreviation: String = commonName.substring(0..2),
    override var population: Int = 0,
    override var squareMileage: Double = 0.0,
    var nickname: String? = null,
    var motto: String? = null,
    override var descriptors: Set<Descriptor> = emptySet(),
    override var demographics: Map<Bloc, Double> = emptyMap(),
    override val legislature: Set<Chamber>? = null,
    override val partiesPresent: Set<Party> = setOf(),
    override val pastElectionResults: MutableList<ElectionResult> = mutableListOf(),
    override var capital: Municipality? = null,
    var counties: Set<County>? = null,
    var senators: Pair<PoliticalActor?, PoliticalActor?>? = null,
    var representatives: MutableSet<PoliticalActor>? = null,
    var governor: PoliticalActor? = null,
    var lieutenantGovernor: PoliticalActor? = null,
) : MapEntity(managers), HasFIPS, HasPolitics
{
    constructor(json: JSONObject) : this(json.get("FIPS") as String) { fromJson(json) }

    val nation = Nation

    override val partyControlFactors: List<(party: Party) -> Double> = listOf(
        // Each legislature seat
        { party -> 1.0 *
            (legislature?.fold(0.0) { acc, it -> acc + it.members.size } ?: 0.0)
        },
        // Legislative house each majority
        { party -> 15.0 *
            (legislature?.fold(0.0) { acc, it -> acc + if (it.isPartyControlled(party)) 1.0 else 0.0 } ?: 0.0)
        },
        // Upper house majority
        { party -> 10.0 * if (upperHouse != null && upperHouse!!.isPartyControlled(party)) 1.0 else 0.0 },
        // Overall legislature majority
        { party -> 15.0 *
            (if (legislature != null && legislature.count { it -> it.isPartyControlled(party)} > legislature.size / 2 ) 1.0 else 0.0)
        },
        // Governor alignment
        { party -> 25.0 * if (governor?.partyAlignment == party) 1.0 else 0.0 },
        // Lieutenant Governor alignment
        { party -> 8.0 * if (lieutenantGovernor?.partyAlignment == party) 1.0 else 0.0 },
        // Trifecta
        { party -> 20.0 *
            if(
                governor?.partyAlignment == party &&
                legislature != null && legislature.count { it -> it.isPartyControlled(party)} > legislature.size / 2
            ) 1.0 else 0.0
        },
        // Each senator
        { party -> 10.0 *
            arrayOf(senators?.first, senators?.second).fold(0.0) { acc, it -> acc + if (it?.partyAlignment == party) 1.0 else 0.0 }
        },
        // Both senators
        { party -> 8.0 *
            if(senators?.first?.partyAlignment == party && senators?.second?.partyAlignment == party) 1.0 else 0.0
        },
        // Each representative
        { party -> 2.0 *
            (representatives?.fold(0.0) { acc, it -> acc + if (it.partyAlignment == party) 1.0 else 0.0} ?: 0.0)
        },
        // Majority of representatives
        { party -> 8.0 *
            if (representatives != null && representatives!!.fold(0) { acc, it -> acc + if (it.partyAlignment == party) 1 else 0} > representatives!!.size / 2) 1.0 else 0.0
        },
        // President represents state
        { party -> 8.0 * if (nation.president.birthplaceMunicipality == this && nation.president.partyAlignment == party) 1.0 else 0.0},
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

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        counties = (json.get("counties") as List<String>).map { MapManager().matchCounty(it) }
        abbreviation = json.get("abbreviation") as String
        nickname = json.get("nickname") as String
        motto = json.get("motto") as String
        senators = null // From CharacterManager
        representatives = null // From CharacterManager
        governor = null // From CharacterManager
        lieutenantGovernor = null // From CharacterManager
    }

    override fun toJson() = JSONObject(uniqueName, mapOf(
        "FIPS" to FIPS,
        "full_name" to fullName,
        "common_name" to commonName,
        "population" to population,
        "square_mileage" to squareMileage,
        "descriptors" to descriptors,
        "demographics" to demographics,
        "legislature" to legislature,
        "parties_present" to partiesPresent,
        "past_elections" to pastElectionResults,
        "capital" to capital?.fullName,
        "counties" to counties?.map { it.FIPS },
        "abbreviation" to abbreviation,
        "nickname" to nickname,
        "motto" to motto,
        "senators" to senators,
        "representatives" to representatives,
        "governor" to governor,
        "lieutenant_governor" to lieutenantGovernor,
    ))
}
