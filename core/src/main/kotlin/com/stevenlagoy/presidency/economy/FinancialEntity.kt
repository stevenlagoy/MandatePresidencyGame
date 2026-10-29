package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.citizens.attributes.finances.BalanceSheet
import com.stevenlagoy.presidency.citizens.attributes.finances.CashAccount

/**
 * A financial entity is any independent entity which may manage items of value.
 */
interface FinancialEntity {
    val balanceSheet: BalanceSheet
    val cashAccount: CashAccount
}
