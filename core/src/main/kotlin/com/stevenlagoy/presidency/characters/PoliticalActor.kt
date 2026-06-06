package com.stevenlagoy.presidency.characters

import com.stevenlagoy.jsonic.JSONObject
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
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialProfile
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.PoliticalAlignment
import java.time.LocalDate
import kotlin.math.E
import kotlin.math.pow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
open class PoliticalActor(
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
    val experiences: MutableList<Experience>,
    val education: Education,
    val skills: Skills,
    val roles: MutableList<Role>,
    val personality: Personality,
    var alignment: PoliticalAlignment,
    val issuePositions: IssuePositionMap,
    var partyAffiliation: Party? = null,
    var candidacy: Candidacy? = null
) : Citizen(
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
) {

    companion object {
        const val MIN_AGE = 18
    }

    val ageMod get() = 100 * E.pow(-1 * ((age - 55) / 30.0).pow(2))

    val conviction: Double
        get() = 0.5 // TODO Evaluate conviction based on positions and alignment

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        (json.get("roles") as List<*>).forEach { role -> roles.add(role as Role) }
        education.fromJson(json.get("education") as JSONObject)
        alignment.fromJson(json.get("alignment") as JSONObject)
        partyAffiliation = ENGINE.POLITICS_MANAGER.PARTY_MANAGER.matchParty(json.get("party_affiliation_id", String::class.java)).get()
        skills.fromJson(json.get("skills") as JSONObject)
        personality.fromJson(json.get("personality") as JSONObject)
        (json.get("experiences") as List<*>).forEach { experience -> experiences.add(experience as Experience) }
        issuePositions.fromJson(json.get("issue_positions") as JSONObject)
        candidacy = Candidacy(ENGINE, json.get("candidacy") as JSONObject)
    }

    override fun toJson() = JSONObject(id.toString(), listOf(
        JSONObject("roles", roles),
        JSONObject("education", education.toJson()),
        JSONObject("alignment", alignment.toJson()),
        JSONObject("party_affiliation_id", partyAffiliation?.name),
        JSONObject("skills", skills.toJson()),
        JSONObject("personality", personality.toJson()),
        JSONObject("experiences", experiences),
        JSONObject("issue_positions", issuePositions.toJson()),
        JSONObject("candidacy", candidacy?.toJson()),
    ))

}
