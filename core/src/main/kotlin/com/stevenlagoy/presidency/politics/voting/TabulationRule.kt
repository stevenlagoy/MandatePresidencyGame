package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.presidency.politics.Issue
import kotlin.collections.iterator

abstract class TabulationRule {
    abstract fun tabulate(results: Map<VotingReturn, Int>): List<IssueResult>

    data class IssueResult(
        val issue: Issue,
        val percentage: Double
    )

    open class Approval(val maximumApprovals: Int? = null) : TabulationRule() {
        override fun tabulate(results: Map<VotingReturn, Int>): List<IssueResult> {
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
    class FirstPreference : Approval(1)

    open class RankedChoice : TabulationRule() {
        override fun tabulate(results: Map<VotingReturn, Int>): List<IssueResult> {
            val eliminated = mutableSetOf<Issue>()
            val allIssues = results.keys.firstOrNull()?.`return`?.keys ?: return emptyList()
            val rankedResult = mutableListOf<IssueResult>()
            val totalResults = results.values.sum().toDouble()
            while (eliminated.size < allIssues.size) {
                val topPreferences = mutableMapOf<Issue, Int>()
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

    open class Score(val scoreRange: IntRange = 0..5) : TabulationRule() {
        override fun tabulate(results: Map<VotingReturn, Int>): List<IssueResult> {
            val totalIssueScores = mutableMapOf<Issue, Double>()
            for ((result, occurrence) in results) {
                for ((issue, score) in result.`return`) {
                    totalIssueScores.merge(issue, score * occurrence.toDouble(), Double::plus)
                }
            }
            val totalResults = results.values.sum().toDouble()
            return totalIssueScores
                .map { (issue, score) -> IssueResult(issue, score / totalResults) }
                .sortedByDescending { it.percentage }
        }
    }
    class STAR(val numberInRunoff: Int = 2) : Score() {
        override fun tabulate(results: Map<VotingReturn, Int>): List<IssueResult> {
            val finalists = super.tabulate(results)
                .take(numberInRunoff)
                .associate { it.issue to 0 }
                .toMutableMap()

            for ((result, occurrence) in results) {
                var bestIssue: Issue? = null
                var bestScore = Int.MIN_VALUE

                for (issue in finalists.keys) {
                    val score = result.`return`[issue] ?: 0
                    if (score > bestScore) {
                        bestScore = score
                        bestIssue = issue
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
