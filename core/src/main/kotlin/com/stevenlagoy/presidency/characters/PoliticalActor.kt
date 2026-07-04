package com.stevenlagoy.presidency.characters

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.characters.attributes.*
import com.stevenlagoy.presidency.characters.attributes.experiences.ExperienceHistory
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialProfile
import com.stevenlagoy.presidency.characters.attributes.names.PersonalName
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.linearvalue.Modifier
import com.stevenlagoy.presidency.demographics.Demographics
import com.stevenlagoy.presidency.map.Municipality
import com.stevenlagoy.presidency.politics.Party
import com.stevenlagoy.presidency.politics.PoliticalAlignment
import java.time.LocalDate
import kotlin.math.E
import kotlin.math.pow
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
open class PoliticalActor(
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
    val experiences: ExperienceHistory,
    val skills: Skills,
    val personality: Personality,
    var alignment: PoliticalAlignment,
    val issuePositions: IssuePositionMap,
    var partyAffiliation: Party? = null,
    var candidacy: Candidacy? = null
) : Citizen(
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
) {

    val ageMod get() = 100 * E.pow(-1 * ((age - 55) / 30.0).pow(2))

    val conviction: Double
        get() = 0.5 // TODO Evaluate conviction based on positions and alignment

    init {
        listOf(skills.component1(), skills.component2(), skills.component3()).forEach {
            it.multiplicativeModifiers.add(Modifier(1.0) { 100 * E.pow(-1 * ((age - 55) / 30.0).pow(2)) })
        }
    }

    override fun fromJson(json: JSONObject) = this.apply {
        super.fromJson(json)
        alignment.fromJson(json.requireJson("alignment") as JSONObject)
        partyAffiliation = engine.POLITICS_MANAGER.PARTY_MANAGER.matchParty(json.requireString("partyAffiliation", "party_affiliation_id")).get()
        skills.fromJson(json.requireJson("skills"))
        personality.fromJson(json.requireJson("personality"))
        ExperienceHistory(engine, json.requireJson("experiences"))
        issuePositions.fromJson(json.requireJson("issuePositions", "positions", "issue_positions"))
        candidacy = Candidacy(engine, json.requireJson("candidacy"))
    }

    override fun toJson() = JSONObject(id.toString(), listOf(
        JSONObject("alignment", alignment.toJson()),
        JSONObject("partyAffiliation", partyAffiliation?.name),
        JSONObject("skills", skills.toJson()),
        JSONObject("personality", personality.toJson()),
        JSONObject("experiences", experiences.toJson()),
        JSONObject("issuePositions", issuePositions.toJson()),
        JSONObject("candidacy", candidacy?.toJson()),
    ))

    companion object {
        const val MIN_AGE = 18
    }
}
