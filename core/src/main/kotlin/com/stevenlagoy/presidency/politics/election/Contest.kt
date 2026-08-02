package com.stevenlagoy.presidency.politics.election

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.MapEntity
import com.stevenlagoy.presidency.map.Nation
import com.stevenlagoy.presidency.politics.Issue
import com.stevenlagoy.presidency.politics.IssuePosition
import com.stevenlagoy.presidency.politics.PartyGoverningBody
import com.stevenlagoy.presidency.politics.government.Government
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

open class Contest(
    engine: Engine
) : JSONSerializable<Contest>, EngineBound(engine) {
    var allowEarlyVoting: Boolean = true
    var earlyVotingBeginDate: LocalDateTime? = null
    var earlyVotingEndDate: LocalDateTime? = null
    var pollsOpenDate: LocalDateTime = LocalDateTime.now()
    var pollsCloseDate: LocalDateTime = pollsOpenDate.plusHours(12)
    var allowMailInVotes: Boolean = true
    var mailInReceiptEnd: LocalDateTime? = null
    var allowOverseasVotes: Boolean = true
    var overseasReceiptEnd: LocalDateTime? = null

    open var constituency: MapEntity = Nation

    var managerialGovernment: Government? = null
    var managerialParty: PartyGoverningBody? = null

    var voterAccessRule: VoterAccessRule = VoterAccessRule.Open()
    var tabulationRule: TabulationRule = TabulationRule.FirstPreference()
    var resolutionRule: ResolutionRule = ResolutionRule.FirstPastThePost()
    var votingMethod: VotingMethod = VotingMethod.Primary()

    var polls: MutableSet<Poll> = mutableSetOf()
    open var issues: Set<Issue> = emptySet()
    open var results: Map<Ballot, Int> = emptyMap()

    init {
        require(earlyVotingEndDate == null || earlyVotingEndDate!!.isBefore(pollsOpenDate)) { "Early voting must close before regular polls open" }
    }

    fun isComplete(currentDate: LocalDateTime): Boolean {
        return currentDate.isAfter(pollsCloseDate) && (if (allowMailInVotes) currentDate.isAfter(mailInReceiptEnd) else true) && (if (allowOverseasVotes) currentDate.isAfter(overseasReceiptEnd) else true)
    }

    fun getWinningPositions(): List<IssuePosition> = resolutionRule.resolve(tabulationRule.tabulate(results))

    override fun fromJson(json: JSONObject) = apply {
        allowEarlyVoting = json.requireBoolean("allowEarlyVoting")
        earlyVotingBeginDate = if (allowEarlyVoting) json.findString("earlyVotingBeginDate") { null }?.let(LocalDateTime::parse) else null
        earlyVotingEndDate = if (allowEarlyVoting) json.findString("earlyVotingEndDate") { null }?.let(LocalDateTime::parse) else null
        pollsOpenDate = LocalDateTime.parse(json.requireString("pollsOpenDate"))
        pollsCloseDate = LocalDateTime.parse(json.requireString("pollsCloseDate"))
        allowMailInVotes = json.requireBoolean("allowMailInVoting")
        mailInReceiptEnd = if (allowMailInVotes) json.findString("mailInReceiptEnd") { null }?.let(LocalDateTime::parse) else null
        allowOverseasVotes = json.requireBoolean("allowOverseasVoting")
        overseasReceiptEnd = if (allowOverseasVotes) json.findString("overseasReceiptEnd") { null }?.let(LocalDateTime::parse) else null

        constituency = engine.MAP_MANAGER.matchMapEntity(json.requireString("constituency")).get()

        managerialGovernment = engine.POLITICS_MANAGER.GOVERNMENT_MANAGER.matchGovernment(json.requireString("managerialGovernment")).get()
        managerialParty = engine.POLITICS_MANAGER.PARTY_MANAGER.matchPartyGoverningBody(json.requireString("managerialParty")).get()

        voterAccessRule.fromJson(json, managerialParty!!.party)
        tabulationRule.fromJson(json)
        resolutionRule.fromJson(json)
        votingMethod.fromJson(json)

        polls = json.findJson("polls").get().let { it.requireArray().map { json -> Poll(json as JSONObject) }}.toMutableSet()
        issues = json.findJson("issues").get().let { it.requireArray().map { issue -> engine.POLITICS_MANAGER.ISSUE_MANAGER.matchIssue(issue as String).getOrNull() }}.filterNotNull().toSet()
//        results = json.findJson("results").get().let { it.requireArray().associate { json as JSONObject -> VotingReturn(engine, json.requireString("position")) }}.toSet() // TODO
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOfNotNull(
        JSONObject("allowEarlyVoting", allowEarlyVoting),
        if (allowEarlyVoting) JSONObject("earlyVotingBeginDate", earlyVotingBeginDate.toString()) else null,
        if (allowEarlyVoting) JSONObject("earlyVotingEndDate", earlyVotingEndDate.toString()) else null,
        JSONObject("pollsOpenDate", pollsOpenDate.toString()),
        JSONObject("pollsCloseDate", pollsCloseDate.toString()),
        JSONObject("allowMailInVotes", allowMailInVotes),
        if (allowMailInVotes) JSONObject("mailInReceiptEnd", mailInReceiptEnd.toString()) else null,
        JSONObject("allowOverseasVotes", allowOverseasVotes),
        if (allowOverseasVotes) JSONObject("overseasReceiptEnd", overseasReceiptEnd.toString()) else null,

        JSONObject("constituency", constituency.name),

        if (managerialGovernment != null) JSONObject("managerialGovernment", managerialGovernment.toString()) else null,
        if (managerialParty != null) JSONObject("managerialParty", managerialParty.toString()) else null,

        voterAccessRule.toJson(),
        tabulationRule.toJson(),
        resolutionRule.toJson(),
        votingMethod.toJson(),

        JSONObject("polls", polls.map { it.toJson() }),
        JSONObject("issues", issues.map { it.toJson() }),
//        JSONObject("results", results.map { it.toJson() }), // TODO
    ))
}
