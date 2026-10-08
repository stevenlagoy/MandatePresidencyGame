package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.citizens.attributes.finances.BalanceSheet
import com.stevenlagoy.presidency.citizens.attributes.finances.CashAccount

class Corporation(
    val name: String,
    override val balanceSheet: BalanceSheet,
    override val cashAccount: CashAccount
) : FinancialEntity
