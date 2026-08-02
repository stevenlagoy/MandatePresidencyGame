package com.stevenlagoy.presidency.politics.election

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.Party

/**
 * Voter access rules determine who is able to participate in an election. Some elections are open
 * to any voter regardless of party affiliation (like general elections), while others franchise
 * certain voters based on their affiliation with an operating party, or lack of affiliation with
 * a rival party (like many state primaries).
 */
sealed class VoterAccessRule(
    var franchise: List<Bloc> = emptyList(),
    var votingIsPartyRegistration: Boolean = false,
    var disallowDoubleParticipation: Boolean = false
) {

    /**
     * Open elections allow anyone to vote and do not require any party affiliation. A voter may
     * participate in any number of open elections.
     */
    class Open : VoterAccessRule(
        listOf(Bloc.CITIZENS),
        false,
        false
    )

    /**
     * Partially open elections allow anyone to vote, but do require a registration with the
     * operating party before casting a vote, and also disallow participation in another partially
     * open or partially closed election in the same year.
     */
    class PartiallyOpen : VoterAccessRule(
        listOf(Bloc.CITIZENS),
        true,
        true
    )

    /**
     * Open-unaffiliated elections are open to any members of the operating party as well as any
     * unregistered voter, and do not require registration by unaffiliated voters. Unaffiliated
     * voters may participate in other open elections.
     */
    class OpenUnaffiliated(val party: Party) : VoterAccessRule(
        listOf(Bloc.UNAFFILIATED, Bloc.resolvePartyAffiliation(party)),
        false,
        false
    )

    /**
     * Partially closed elections are open to any members of the operating party as well as any
     * unregistered voter, and do not require registration by unaffiliated voters. Unlike
     * open-unaffiliated elections, partially closed elections do not allow participation in
     * any other similar election in the same year.
     */
    class PartiallyClosed(val party: Party) : VoterAccessRule(
        listOf(Bloc.UNAFFILIATED, Bloc.resolvePartyAffiliation(party)),
        false,
        true
    )

    /**
     * Closed elections are open only to members of the operating party. Those members may
     * participate in any other election.
     */
    class Closed(val party: Party) : VoterAccessRule(
        listOf(Bloc.resolvePartyAffiliation(party)),
        false,
        false
    )

    fun toJson() = JSONObject("voterAccessRule", this::class.simpleName!!)

    fun fromJson(json: JSONObject, party: Party): VoterAccessRule = when (json.requireString("voterAccessRule").lowercase()) {
        "open"             -> Open()
        "partiallyopen"    -> PartiallyOpen()
        "openunaffiliated" -> OpenUnaffiliated(party)
        "partiallyclosed"  -> PartiallyClosed(party)
        "closed"           -> Closed(party)
        else               -> throw IllegalArgumentException("Unknown voter access rule: $json")
    }
}
