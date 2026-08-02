package com.stevenlagoy.presidency.politics.election

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.politics.Issue
import com.stevenlagoy.presidency.politics.IssuePosition
import com.stevenlagoy.presidency.politics.election.TabulationRule.Approval
import com.stevenlagoy.presidency.politics.election.TabulationRule.FirstPreference
import com.stevenlagoy.presidency.politics.election.TabulationRule.RankedChoice
import com.stevenlagoy.presidency.politics.election.TabulationRule.STAR
import com.stevenlagoy.presidency.politics.election.TabulationRule.Score
import com.stevenlagoy.presidency.util.replaceAllRegex

/**
 * Resolution rules determine the final set of winners of an election following tabulation. They
 * do not determine how voters cast a ballot, rather serving to take the results and produce some
 * valid outcome from the results.
 */
sealed class ResolutionRule {

    abstract fun getName(): String

    abstract fun resolve(tabulated: List<TabulationRule.IssueResult>): List<IssuePosition>

    fun toJson() = JSONObject("tabulationRule", getName())

    fun fromJson(json: JSONObject): ResolutionRule {
        val ruleString = json.requireString("resolutionRule")
        val type = ruleString.split(Regex("\\s+")).first()
        val params = ruleString.split(Regex("\\s+")).drop(1).map { it.replaceAllRegex("\\(\\)", "") }
        return when (type) {
            "plurality"        -> Plurality(params[0].toInt())
            "firstpastthepost" -> FirstPastThePost()
            "threshold"        -> Threshold(params[0].toDouble(), params[1].toDouble())
            "majority"         -> Majority()
            else               -> FirstPastThePost()
        }
    }

    /**
     * Plurality resolves an election by selecting the top N results as winners. Together, these
     * winners' votes total to a plurality of the votes, and are the smallest such set to do this.
     */
    open class Plurality(val topN: Int = 1) : ResolutionRule() {
        override fun getName() = "${this::class.simpleName!!} ($topN)"

        override fun resolve(tabulated: List<TabulationRule.IssueResult>): List<IssuePosition> = tabulated.take(topN).map { it.issue }
    }

    /**
     * First past the post is a type of plurality in which only one winner is selected. This winner
     * must have recieved a true plurality of the votes.
     */
    class FirstPastThePost : Plurality(1)

    /**
     * Threshold resolves an election by selecting all possibilities which exceed some qualifying
     * threshold as winners. In some cases, a winner-take-all threshold may award the entire
     * election to the possibility which achieves the most votes.
     */
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

        override fun getName() = "${this::class.simpleName!!} ($qualifyingThreshold) ($winnerTakeAllThreshold)"

        override fun resolve(tabulated: List<TabulationRule.IssueResult>): List<IssuePosition> {
            val winner = tabulated.find { it.percentage > winnerTakeAllThreshold }
            if (winner != null) return listOf(winner.issue)
            return tabulated.filter { it.percentage > qualifyingThreshold }.map { it.issue }
        }
    }

    /**
     * Majority is a form of threshold resolution in which the option with a simple majority of the
     * votes is selected as the winner. One winner is usually selected, though two may be selected
     * in the case of a perfect tie.
     */
    class Majority : Threshold(0.5, 0.5)

}
