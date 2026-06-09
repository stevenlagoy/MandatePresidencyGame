package com.stevenlagoy.presidency.characters

import com.stevenlagoy.presidency.characters.attributes.Candidacy
import com.stevenlagoy.presidency.characters.attributes.CharacterAppearance
import com.stevenlagoy.presidency.characters.attributes.Education
import com.stevenlagoy.presidency.characters.attributes.experiences.Experience
import com.stevenlagoy.presidency.characters.attributes.Family
import com.stevenlagoy.presidency.characters.attributes.IssuePositionMap
import com.stevenlagoy.presidency.characters.attributes.Personality
import com.stevenlagoy.presidency.characters.attributes.Role
import com.stevenlagoy.presidency.characters.attributes.Sex
import com.stevenlagoy.presidency.characters.attributes.Skills
import com.stevenlagoy.presidency.characters.attributes.experiences.ExperienceHistory
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialProfile
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.PoliticalAlignment
import java.time.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class PlayerCharacter(
    ENGINE: Engine,
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
    roles: MutableList<Role>,
    education: Education,
    alignment: PoliticalAlignment,
    partyAffiliation: Party?,
    skills: Skills,
    personality: Personality,
    experiences: ExperienceHistory,
    issuePositions: IssuePositionMap,
    candidacy: Candidacy?
) : PoliticalActor(
    ENGINE,
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
    education,
    skills,
    roles,
    personality,
    alignment,
    issuePositions,
    partyAffiliation,
    candidacy,
) {
}
