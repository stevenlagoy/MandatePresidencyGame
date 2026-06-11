package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine

class FinancialProfile(
    protected val ENGINE: Engine,
    val cashAccount: CashAccount,
    val balanceSheet: BalanceSheet
) : Jsonic<FinancialProfile> {

    constructor(ENGINE: Engine, json: JSONObject) : this(
        ENGINE,
        CashAccount(json.get("cash_account") as JSONObject),
        BalanceSheet(ENGINE, json.get("balance_sheet") as JSONObject)
    )

    override fun toJson() = JSONObject()

    override fun fromJson(json: JSONObject) = this.apply {

    }
}
