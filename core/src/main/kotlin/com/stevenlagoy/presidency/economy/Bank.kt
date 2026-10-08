package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.citizens.attributes.finances.BalanceSheet
import com.stevenlagoy.presidency.citizens.attributes.finances.CashAccount
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound

/**
 * A bank is a financial entity which handles money by accepting deposits, managing savings and
 * checking accounts, issuing loans, and exchanging currency.
 */
class Bank(
    engine: Engine,
    val name: String,
    override val balanceSheet: BalanceSheet,
    override val cashAccount: CashAccount
) : FinancialEntity, EngineBound(engine) {

    val loansIssued: Set<Loan> = setOf()



}
