package com.stevenlagoy.presidency.characters.attributes.finances

class Bank(
    override val balanceSheet: BalanceSheet,
    override val cashAccount: CashAccount
) : FinancialEntity
