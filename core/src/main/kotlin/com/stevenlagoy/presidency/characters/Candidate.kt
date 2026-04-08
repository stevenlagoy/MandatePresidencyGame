package com.stevenlagoy.presidency.characters

import com.stevenlagoy.presidency.characters.attributes.CharacterAppearance
import com.stevenlagoy.presidency.characters.attributes.Education
import com.stevenlagoy.presidency.characters.attributes.Experience
import com.stevenlagoy.presidency.characters.attributes.Personality
import com.stevenlagoy.presidency.characters.attributes.Role
import com.stevenlagoy.presidency.characters.attributes.Skills
import com.stevenlagoy.presidency.characters.attributes.finances.BalanceSheet
import com.stevenlagoy.presidency.characters.attributes.finances.CashAccount
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.Position
import java.time.LocalDate

open class Candidate(
    managers: Engine.Managers,
    demographics: Demographics,
    name: PersonalName,
    origin: Municipality,
    location: Municipality,
    residence: Municipality,
    birthday: LocalDate,
    appearance: CharacterAppearance,
    balanceSheet: BalanceSheet,
    cashAccount: CashAccount,
    education: Education,
    alignments: IntArray,
    partyAlignment: Party,
    experiences: List<Experience>,
    skills: Skills,
    positions: List<Position>,
    personality: Personality,
    roles: Set<Role>,
) : PoliticalActor(
    managers,
    demographics,
    name,
    origin,
    location,
    residence,
    birthday,
    appearance,
    balanceSheet,
    cashAccount,
    education,
    alignments,
    partyAlignment,
    skills,
    personality,
    experiences,
    positions,
    roles,
) {

    companion object {
        const val MIN_AGE = 35
    }


}
