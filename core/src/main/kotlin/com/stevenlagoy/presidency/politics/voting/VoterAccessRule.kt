package com.stevenlagoy.presidency.politics.voting

import com.stevenlagoy.presidency.demographics.Bloc
import com.stevenlagoy.presidency.politics.Party

abstract class VoterAccessRule(
    var franchise: List<Bloc> = emptyList(),
    var votingIsPartyRegistration: Boolean = false,
    var disallowDoubleParticipation: Boolean = false
) {
    class Open : VoterAccessRule(
        listOf(Bloc.CITIZENS),
        false,
        false
    )

    class PartiallyOpen : VoterAccessRule(
        listOf(Bloc.CITIZENS),
        true,
        true
    )

    class OpenUnaffiliated(val party: Party) : VoterAccessRule(
        listOf(Bloc.UNAFFILIATED, Bloc.resolvePartyAffiliation(party)),
        false,
        false
    )

    class PartiallyClosed(val party: Party) : VoterAccessRule(
        listOf(Bloc.UNAFFILIATED, Bloc.resolvePartyAffiliation(party)),
        false,
        true
    )

    class Closed(val party: Party) : VoterAccessRule(
        listOf(Bloc.resolvePartyAffiliation(party)),
        false,
        false
    )
}
