package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.map.MapEntity
import com.stevenlagoy.presidency.map.Nation
import com.stevenlagoy.presidency.politics.Government
import com.stevenlagoy.presidency.politics.Issue
import com.stevenlagoy.presidency.politics.PartyGoverningBody
import java.time.LocalDateTime

open class Vote(
    engine: Engine
) : JSONSerializable<Vote>, EngineBound(engine) {
    var allowEarlyVoting: Boolean = true
    var earlyVotingBeginDate: LocalDateTime? = null
    var earlyVotingEndDate: LocalDateTime? = null
    var pollsOpenDate: LocalDateTime = LocalDateTime.now()
    var pollsCloseDate: LocalDateTime = pollsOpenDate.plusHours(12)
    var allowMailInVotes: Boolean = true
    var mailInReceiptEnd: LocalDateTime? = null
    var allowOverseasVotes: Boolean = true
    var overseasReceiptEnd: LocalDateTime? = null

    var constituency: MapEntity = Nation

    var managerialGovernment: Government? = null
    var managerialParty: PartyGoverningBody? = null

    var voterAccessRule: VoterAccessRule = VoterAccessRule.Open()
    var tabulationRule: TabulationRule = TabulationRule.FirstPreference()
    var resolutionRule: ResolutionRule = ResolutionRule.FirstPastThePost()
    var votingMethod: VotingMethod = VotingMethod.Primary()

    var polls: MutableList<Poll> = mutableListOf()
    open var issues: List<Issue> = emptyList()

    var results: Map<VotingReturn, Int> = emptyMap()

    override fun fromJson(json: JSONObject) = apply {
        allowEarlyVoting = json.requireBoolean("allowEarlyVoting")
        earlyVotingBeginDate = LocalDateTime.parse(json.requireString("earlyVotingBeginDate"))
        earlyVotingEndDate = LocalDateTime.parse(json.requireString("earlyVotingEndDate"))
        pollsOpenDate = LocalDateTime.parse(json.requireString("pollsOpenDate"))
        pollsCloseDate = LocalDateTime.parse(json.requireString("pollsCloseDate"))
        allowMailInVotes = json.requireBoolean("allowMailInVoting")
        mailInReceiptEnd = LocalDateTime.parse(json.requireString("mailInReceiptEnd"))
        allowOverseasVotes = json.requireBoolean("allowOverseasVoting")
        overseasReceiptEnd = LocalDateTime.parse(json.requireString("overseasReceiptEnd"))
        constituency = engine.MAP_MANAGER.matchMapEntity(json.requireString("constituency")).get()
        // TODO
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("allowEarlyVoting", allowEarlyVoting),
        JSONObject("earlyVotingBeginDate", earlyVotingBeginDate.toString()),
        JSONObject("earlyVotingEndDate", earlyVotingEndDate.toString()),
        JSONObject("pollsOpenDate", pollsOpenDate.toString()),
        JSONObject("pollsCloseDate", pollsCloseDate.toString()),
        JSONObject("allowMailInVotes", allowMailInVotes),
        JSONObject("mailInReceiptEnd", mailInReceiptEnd.toString()),
        JSONObject("allowOverseasVotes", allowOverseasVotes),
        JSONObject("overseasReceiptEnd", overseasReceiptEnd.toString()),
        JSONObject("constituency", constituency.name),
        JSONObject("managerialGovernment", managerialGovernment.toString()),
        JSONObject("managerialParty", managerialParty.toString()),
        JSONObject("voterAccessRule", voterAccessRule.toString()),
        JSONObject("tabulationRule", tabulationRule.toString()),
        JSONObject("resolutionRule", resolutionRule.toString()),
        JSONObject("votingMethod", votingMethod.toString()),
        JSONObject("polls", polls.map { it.toJson() }),
        JSONObject("issues", issues.map { it.toJson() }),
        JSONObject("results") // TODO
    ))
}
