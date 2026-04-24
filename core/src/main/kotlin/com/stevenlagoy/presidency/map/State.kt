package com.stevenlagoy.presidency.map

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.PoliticalActor
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Government
import com.stevenlagoy.presidency.politics.Party

class State (
    MANAGERS: Engine.Managers,
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
    override val government: Government? = null,
    override val partiesPresent: MutableSet<Party> = mutableSetOf(),
    override val pastElectionResults: MutableList<ElectionResult> = mutableListOf(),
    override var capital: Municipality? = null,
    var counties: Set<County>? = null,
    var municipalities: Set<Municipality>? = null,
    var senators: Pair<PoliticalActor?, PoliticalActor?>? = null,
    var representatives: MutableSet<PoliticalActor>? = null,
) : MapEntity(MANAGERS), HasFIPS, HasPolitics
{
    constructor(MANAGERS: Engine.Managers, json: JSONObject) : this(MANAGERS, json.get("FIPS").toString()) { fromJson(json) }

    val nation = Nation

    override val partyControlFactors: List<(party: Party) -> Double> = listOf(
        // Each legislature seat
        // Each senator
        { party -> 10.0 *
            arrayOf(senators?.first, senators?.second).fold(0.0) { acc, it -> acc + if (it?.partyAffiliation == party) 1.0 else 0.0 }
        },
        // Both senators
        { party -> 8.0 *
            if(senators?.first?.partyAffiliation == party && senators?.second?.partyAffiliation == party) 1.0 else 0.0
        },
        // Each representative
        { party -> 2.0 *
            (representatives?.fold(0.0) { acc, it -> acc + if (it.partyAffiliation == party) 1.0 else 0.0} ?: 0.0)
        },
        // Majority of representatives
        { party -> 8.0 *
            if (representatives != null && representatives!!.fold(0) { acc, it -> acc + if (it.partyAffiliation == party) 1 else 0} > representatives!!.size / 2) 1.0 else 0.0
        },
        // President represents state
        { party -> 8.0 * if (nation.government.executiveBranch.chiefExecutive?.origin?.state == this && nation.government.executiveBranch.chiefExecutive?.partyAffiliation == party) 1.0 else 0.0},
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
        counties = null
        abbreviation = json.get("abbreviation") as String
        nickname = json.get("nickname") as String
        motto = json.get("motto") as String
        senators = null // From CharacterManager
        representatives = null // From CharacterManager
    }

    override fun toJson() = JSONObject(uniqueName, mapOf(
        "FIPS" to FIPS,
        "full_name" to fullName,
        "common_name" to commonName,
        "population" to population,
        "square_mileage" to squareMileage,
        "descriptors" to descriptors,
        "demographics" to demographics,
        "government" to government?.toJson(),
        "parties_present" to partiesPresent,
        "past_elections" to pastElectionResults,
        "capital" to capital?.fullName,
        "counties" to counties?.map { it.FIPS },
        "abbreviation" to abbreviation,
        "nickname" to nickname,
        "motto" to motto,
        "senators" to senators,
        "representatives" to representatives,
    ))
}
