package com.stevenlagoy.presidency.citizens.attributes.finances

class Corporation(
    override val balanceSheet: BalanceSheet,
    override val cashAccount: CashAccount
) : FinancialEntity
