package com.stevenlagoy.presidency.characters

import com.stevenlagoy.presidency.characters.attributes.*
import com.stevenlagoy.presidency.characters.attributes.experiences.ExperienceHistory
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialProfile
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.PoliticalAlignment
import java.time.LocalDate

class PlayerCharacter(
    engine: Engine,
    sex: Sex,
    birthday: LocalDate,
    demographics: Demographics,
    family: Family,
    appearance: CharacterAppearance,
    name: PersonalName,
    origin: Municipality,
    location: Municipality,
    residence: Municipality,
    financialProfile: FinancialProfile?,
    alignment: PoliticalAlignment,
    partyAffiliation: Party?,
    skills: Skills,
    personality: Personality,
    experiences: ExperienceHistory,
    issuePositions: IssuePositionMap,
    candidacy: Candidacy?
) : PoliticalActor(
    engine,
    sex,
    birthday,
    demographics,
    family,
    appearance,
    name,
    origin,
    location,
    residence,
    financialProfile,
    experiences,
    skills,
    personality,
    alignment,
    issuePositions,
    partyAffiliation,
    candidacy,
)
