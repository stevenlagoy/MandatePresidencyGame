package com.stevenlagoy.presidency.politics.government

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.characters.attributes.finances.BalanceSheet
import com.stevenlagoy.presidency.characters.attributes.finances.CashAccount
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialEntity
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.HasPartyPresence
import com.stevenlagoy.presidency.politics.ElectionResult
import com.stevenlagoy.presidency.politics.Party

/**
 * @property pastElectionResults Elections for positions not covered by any branch. Use branch electoral history for other purposes.
 */
class Government(
    engine: Engine,
    val name: String = "",
    val executiveBranch: ExecutiveBranch = ExecutiveBranch(),
    val legislativeBranch: LegislativeBranch = LegislativeBranch(),
    val judicialBranch: JudicialBranch = JudicialBranch(),
    val pastElectionResults: MutableList<ElectionResult> = mutableListOf(),
    override val balanceSheet: BalanceSheet = BalanceSheet(engine),
    override val cashAccount: CashAccount = CashAccount(engine),
) : FinancialEntity, HasPartyPresence, EngineBound(engine), JSONSerializable<Government> {

    override val partiesPresent: Set<Party>
        get() = (executiveBranch.partiesPresent + legislativeBranch.partiesPresent + judicialBranch.partiesPresent).toMutableSet()

    override val partyCloutFactors: Set<(party: Party) -> Double> = setOf(
        // Executive branch
        { party -> 0.35 * executiveBranch.getPartyControl(party) },
        // Legislative branch
        { party -> 0.30 * legislativeBranch.getPartyControl(party) },
        // Judicial branch
        { party -> 0.1 * judicialBranch.getPartyControl(party) },
        // Trifecta
        { party -> 0.25 * (
            if (executiveBranch.getPartyInControl() == party && legislativeBranch.chambers.all { it.isPartyMajority(party) }) 1.0 else 0.0
        )},
    )

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override fun toJson() = JSONObject(name, listOf(
        JSONObject("title", name),
        JSONObject("executiveBranch", executiveBranch.toJson()),
        JSONObject("legislativeBranch", legislativeBranch.toJson()),
        JSONObject("judicialBranch", judicialBranch.toJson()),
    ))

    override fun fromJson(json: JSONObject) = apply {

    }

}
