package com.stevenlagoy.presidency.characters.attributes.finances

class Corporation(
    override val balanceSheet: BalanceSheet,
    override val cashAccount: CashAccount
) : FinancialEntity {
}
