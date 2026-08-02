package com.stevenlagoy.presidency.politics.election

import com.stevenlagoy.presidency.map.MapEntity
import com.stevenlagoy.presidency.politics.Campaign
import com.stevenlagoy.presidency.politics.Party

class ElectionResult(
    val election: Election,
    val area: MapEntity,
    val tally: Map<Ballot, Int>,
) {
    val totalVotes: Int get() = tally.values.sum()

    fun getMarginForParty(party: Party): Double {
        val forParty = election.tabulationRule.tabulate(tally)
            .filter { (it.issue.issue as? Campaign)?.affiliatedParty == party }
            .sumOf { it.percentage }
        return forParty / (1.0 - forParty)
    }
}
