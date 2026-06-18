package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound

class FinancialProfile(
    engine: Engine,
    val cashAccount: CashAccount,
    val balanceSheet: BalanceSheet
) : Jsonic<FinancialProfile>, EngineBound(engine) {

    constructor(engine: Engine, json: JSONObject) : this(
        engine,
        CashAccount(json.get("cash_account") as JSONObject),
        BalanceSheet(engine, json.get("balance_sheet") as JSONObject)
    )

    override fun toJson() = JSONObject()

    override fun fromJson(json: JSONObject) = this.apply {

    }
}
