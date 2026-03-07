package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.presidency.politics.Issue

abstract class ResolutionRule {

    abstract fun resolve(tabulated: List<TabulationRule.IssueResult>): List<Issue>

    open class Plurality(val topN: Int) : ResolutionRule() {
        override fun resolve(tabulated: List<TabulationRule.IssueResult>): List<Issue> = tabulated.take(topN).map { it.issue }
    }
    class FirstPastThePost : Plurality(1)

    open class Threshold(
        val qualifyingThreshold: Double = 0.15,
        val winnerTakeAllThreshold: Double = 0.5
    ) : ResolutionRule()
    {
        init {
            if (qualifyingThreshold < 0.0) throw IllegalArgumentException("Qualify threshold cannot be negative")
            if (qualifyingThreshold > 1.0) throw IllegalArgumentException("Qualify threshold cannot be greater than 100% (1.0)")
            if (winnerTakeAllThreshold < 0.5) throw IllegalArgumentException("Winner-Take-All threshold cannot be less than 50% (0.5)")
        }

        override fun resolve(tabulated: List<TabulationRule.IssueResult>): List<Issue> {
            val winner = tabulated.find { it.percentage > winnerTakeAllThreshold }
            if (winner != null) return listOf(winner.issue)
            return tabulated.filter { it.percentage > qualifyingThreshold }.map { it.issue }
        }
    }
    class Majority : Threshold(0.5)

}
