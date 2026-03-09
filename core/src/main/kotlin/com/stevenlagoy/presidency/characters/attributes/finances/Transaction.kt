package com.stevenlagoy.presidency.characters.attributes.finances

import java.time.LocalDate

class Transaction(
    val to: FinancialEntity,
    val from: FinancialEntity,
    val amount: Double,
    val date: LocalDate,
) {

    fun execute(): Boolean {
        if (from.cashAccount.withdraw(FundType.DISCRETIONARY, amount) < 0) return false
        to.cashAccount.deposit(FundType.DISCRETIONARY, amount)
        return true
    }
}
