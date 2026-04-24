package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.presidency.map.MapEntity
import com.stevenlagoy.presidency.map.Nation
import com.stevenlagoy.presidency.politics.Government
import com.stevenlagoy.presidency.politics.Issue
import com.stevenlagoy.presidency.politics.PartyGoverningBody
import java.time.LocalDateTime

open class Vote {
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

    var polls: MutableList<Poll> = mutableListOf<Poll>()
    open var issues: List<Issue> = emptyList()

    var results: Map<VotingReturn, Int> = emptyMap()

}
