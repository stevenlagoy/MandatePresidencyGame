package com.stevenlagoy.presidency.citizens.attributes.finances

class Bank(
    override val balanceSheet: BalanceSheet,
    override val cashAccount: CashAccount
) : FinancialEntity
