package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.characters.attributes.finances.FinancialProfile
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.politics.FederalLevel
import java.time.LocalDate
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Candidacy(
    ENGINE: Engine,
    electionId: Uuid,
    status: CandidacyStatus,
    declarationDate: LocalDate,
    withdrawalDate: LocalDate?,
    campaignFinance: FinancialProfile,
) : Jsonic<Candidacy> {

    constructor(ENGINE: Engine, json: JSONObject) : this(
        ENGINE,
        json.get("election_id") as Uuid,
        CandidacyStatus.valueOf(json.get("status") as String),
        LocalDate.parse(json.get("declaration_date") as String),
        LocalDate.parse(json.get("withdrawal_date") as String),
        FinancialProfile(ENGINE, json.get("campaign_finance") as JSONObject),
    )

    override fun toJson(): JSONObject? {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject?): Candidacy? {
        TODO("Not yet implemented")
    }
}
