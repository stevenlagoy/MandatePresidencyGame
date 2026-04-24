package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.characters.attributes.finances.BalanceSheet
import com.stevenlagoy.presidency.characters.attributes.finances.CashAccount
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialEntity
import com.stevenlagoy.presidency.map.HasPolitics
import com.stevenlagoy.presidency.politics.branches.ExecutiveBranch
import com.stevenlagoy.presidency.politics.branches.JudicialBranch
import com.stevenlagoy.presidency.politics.branches.LegislativeBranch

/**
 * @property pastElectionResults Elections for positions not covered by any branch. Use branch electoral history for other purposes.
 */
class Government(
    val title: String,
    val executiveBranch: ExecutiveBranch,
    val legislativeBranch: LegislativeBranch,
    val judicialBranch: JudicialBranch,
    override val pastElectionResults: MutableList<ElectionResult>,
    override val balanceSheet: BalanceSheet,
    override val cashAccount: CashAccount,
) : FinancialEntity, HasPolitics, Jsonic<Government>
{
    override val partiesPresent: MutableSet<Party>
        get() = (executiveBranch.partiesPresent + legislativeBranch.partiesPresent + judicialBranch.partiesPresent).toMutableSet()

    override val partyControlFactors: List<(party: Party) -> Double> = listOf(
        // Executive branch
        { party -> 0.35 * executiveBranch.getPartyControl(party) },
        // Legislative branch
        { party -> 0.30 * legislativeBranch.getPartyControl(party) },
        // Judicial branch
        { party -> 0.1 * judicialBranch.getPartyControl(party) },
        // Trifecta
        { party -> 0.25 * (
            if (executiveBranch.chiefExecutive?.partyAffiliation == party && legislativeBranch.chambers.all { it.isPartyMajority(party)}) 1.0 else 0.0
        )},
    )

    override fun toJson() = JSONObject(title, mapOf(
        "title" to title,
        "executive_branch" to executiveBranch.toJson(),
        "legislative_branch" to legislativeBranch.toJson(),
        "judicial_branch" to judicialBranch.toJson(),
    ))

    override fun fromJson(json: JSONObject) = this.apply {}

}
