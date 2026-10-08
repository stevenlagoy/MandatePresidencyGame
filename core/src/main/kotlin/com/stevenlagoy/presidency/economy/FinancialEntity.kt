package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.citizens.attributes.finances.BalanceSheet
import com.stevenlagoy.presidency.citizens.attributes.finances.CashAccount

interface FinancialEntity {
    val balanceSheet: BalanceSheet
    val cashAccount: CashAccount
}
