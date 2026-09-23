package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound

class FinancialProfile(
    engine: Engine,
    cashAccount: CashAccount = CashAccount(engine),
    balanceSheet: BalanceSheet = BalanceSheet(engine)
) : JSONSerializable<FinancialProfile>, EngineBound(engine) {

    var cashAccount = cashAccount
        internal set

    var balanceSheet = balanceSheet
        internal set

    constructor(engine: Engine, json: JSONObject) : this(
        engine,
        CashAccount(engine, json.requireJson("cash_account")),
        BalanceSheet(engine, json.requireJson("balance_sheet"))
    )

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("cashAccount", cashAccount.toJson()),
        JSONObject("balanceSheet", balanceSheet.toJson())
    ))

    override fun fromJson(json: JSONObject) = this.apply {

    }
}
