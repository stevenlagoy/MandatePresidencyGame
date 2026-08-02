package com.stevenlagoy.presidency.politics.election

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.politics.IssuePosition
import com.stevenlagoy.presidency.util.replaceAllRegex

/**
 * Tabulation rules determine the way issues or candidates are compared after an election to
 * allow a winner to be deterimed. Tabulation rules themselves do not select a winner, but do
 * provide an ordering of the possible outcomes which can inform winner selection.
 */
sealed class TabulationRule {
    abstract fun getName(): String
    abstract fun tabulate(results: Map<Ballot, Int>): List<IssueResult>

    fun toJson() = JSONObject("tabulationRule", getName())

    fun fromJson(json: JSONObject): TabulationRule {
        val ruleString = json.requireString("tabulationRule")
        val type = ruleString.split(Regex("\\s+")).first()
        val params = ruleString.split(Regex("\\s+")).drop(1).map { it.replaceAllRegex("\\(\\)", "") }
        return when (type) {
            "approval"        -> Approval(params[0].toIntOrNull())
            "firstpreference" -> FirstPreference()
            "rankedchoice"    -> RankedChoice()
            "score"           -> Score(params[0].split("..").let { (start, end) -> start.toInt()..end.toInt() })
            "star"            -> STAR(params[1].split("..").let { (start, end) -> start.toInt()..end.toInt() }, params[0].toInt())
            else              -> FirstPreference()
        }
    }

    data class IssueResult(
        val issue: IssuePosition,
        val percentage: Double
    )

    /**
     * Approval voting is a rated voting system in which voters approve of some number of
     * candidates, and the candidate with the most approval votes is the winner.
     */
    open class Approval(val maximumApprovals: Int? = null) : TabulationRule() {
        override fun getName() = "${this::class.simpleName!!} ($maximumApprovals)"

        override fun tabulate(results: Map<Ballot, Int>): List<IssueResult> {
            val totals = results
                .asSequence()
                .flatMap { (result, occurrence) ->
                    result.`return`.asSequence()
                        .map { (issue, approval) -> issue to approval * occurrence }
                }
                .groupingBy { it.first }
                .fold(0) { acc, entry -> acc + entry.second }

            val totalApprovals = totals.values.sum().toDouble()
            if (totalApprovals == 0.0) return emptyList()
            return totals
                .entries
                .sortedByDescending { it.value }
                .map { (issue, votes) -> IssueResult(issue, votes / totalApprovals) }
        }
    }

    /**
     * First preference voting is a type of approval voting in which voters select the one
     * candidate they want to win. The candidate who recieves the most votes wins.
     */
    class FirstPreference : Approval(1) {
        override fun getName() = this::class.simpleName!!
    }

    /**
     * Ranked choice voting is an ordered voting system in which voters order their favorite
     * candidates by assigning number values to them in preference order. The candidate with the
     * fewest first place rankings is then eliminated, and those votes are cast to their second
     * preference candidates. This repeats until one candidate has a majority of the highest
     * remaining rankings.
     */
    open class RankedChoice : TabulationRule() {
        override fun getName() = this::class.simpleName!!

        override fun tabulate(results: Map<Ballot, Int>): List<IssueResult> {
            val eliminated = mutableSetOf<IssuePosition>()
            val allIssues = results.keys.firstOrNull()?.`return`?.keys ?: return emptyList()
            val rankedResult = mutableListOf<IssueResult>()
            val totalResults = results.values.sum().toDouble()
            while (eliminated.size < allIssues.size) {
                val topPreferences = mutableMapOf<IssuePosition, Int>()
                results.forEach { (result, occurrence) ->
                    val topKey = result.`return`
                        .asSequence()
                        .filter { it.key !in eliminated }
                        .maxByOrNull { it.value }
                        ?.key ?: return@forEach
                    topPreferences.merge(topKey, occurrence, Int::plus)
                }
                val minPreference = topPreferences.minBy { it.value }
                rankedResult += IssueResult(minPreference.key, minPreference.value / totalResults)
                eliminated += minPreference.key
            }
            return rankedResult
        }
    }

    /**
     * Score voting is a ranged voting system in which voters assign each candidate a score, and
     * the candidate with the highest average score wins.
     */
    open class Score(val scoreRange: IntRange = 0..5) : TabulationRule() {
        override fun getName() = "${this::class.simpleName!!} ($scoreRange)"

        override fun tabulate(results: Map<Ballot, Int>): List<IssueResult> {
            val totalIssueScores = mutableMapOf<IssuePosition, Double>()
            for ((result, occurrence) in results) {
                for ((issuePosition, score) in result.`return`) {
                    totalIssueScores.merge(issuePosition, score * occurrence.toDouble(), Double::plus)
                }
            }
            val totalResults = results.values.sum().toDouble()
            return totalIssueScores
                .map { (issuePosition, score) -> IssueResult(issuePosition, score / totalResults) }
                .sortedByDescending { it.percentage }
        }
    }

    /**
     * STAR voting (Score Then Automatic Runoff) is a variant of score voting in which average
     * scores are used to determine the top candidates. An automatic runoff afterward determines
     * the winner as the finalist who is preferred on more ballots.
     */
    class STAR(scoreRange: IntRange = 0..5, val numberInRunoff: Int = 2) : Score(scoreRange) {
        override fun getName() = "${this::class.simpleName!!} ($numberInRunoff) ($scoreRange)"

        override fun tabulate(results: Map<Ballot, Int>): List<IssueResult> {
            val finalists = super.tabulate(results)
                .take(numberInRunoff)
                .associate { it.issue to 0 }
                .toMutableMap()

            for ((result, occurrence) in results) {
                var bestIssue: IssuePosition? = null
                var bestScore = Int.MIN_VALUE

                for (issuePosition in finalists.keys) {
                    val score = result.`return`[issuePosition] ?: 0
                    if (score > bestScore) {
                        bestScore = score
                        bestIssue = issuePosition
                    }
                }

                bestIssue?.let { finalists[it] = finalists.getValue(it) + occurrence }
            }

            val total = finalists.values.sum().toDouble()

            return finalists
                .map { IssueResult(it.key, it.value / total) }
                .sortedByDescending { it.percentage }
        }
    }
}
