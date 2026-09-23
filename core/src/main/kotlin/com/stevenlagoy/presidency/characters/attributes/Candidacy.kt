package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialProfile
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import java.time.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Candidacy(
    engine: Engine,
    var electionId: Uuid = Uuid.random(),
    var status: CandidacyStatus = CandidacyStatus.DECLARED,
    declarationDate: LocalDate = engine.TIME_MANAGER.currentDate.toLocalDate(),
    var withdrawalDate: LocalDate? = null,
    campaignFinance: FinancialProfile = FinancialProfile(engine),
) : JSONSerializable<Candidacy>, EngineBound(engine) {

    var declarationDate = declarationDate
        internal set

    var campaignFinance = campaignFinance
        internal set

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    override fun toJson() = JSONObject("$electionId", listOf(
        JSONObject("electionId", electionId.toString()),
        JSONObject("status", status.toString()),
        JSONObject("declarationDate", declarationDate.toString()),
        JSONObject("withdrawalDate", withdrawalDate?.toString()),
        JSONObject("campaignFinance", campaignFinance.toString()),
    ))

    override fun fromJson(json: JSONObject) = apply {
        electionId      = Uuid.parse(json.requireString("electionId"))
        status          = CandidacyStatus.valueOf(json.requireString("status").uppercase().replace(Regex("[^A-Z0-9]"), "_"))
        declarationDate = LocalDate.parse(json.requireString("declarationDate"))
        withdrawalDate  = LocalDate.parse(json.requireString("withdrawalDate"))
        campaignFinance = FinancialProfile(engine, json.requireJson("campaignFinance"))
    }

    enum class CandidacyStatus {
        DECLARED,
        ACTIVE,
        WITHDRAWN,
        LOST,
        WON,
    }
}
