package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.citizens.attributes.finances.FundType
import java.time.LocalDate

class Transaction(
    val receiver: FinancialEntity,
    val sender: FinancialEntity,
    val amount: Double,
    val date: LocalDate,
) {

    fun execute(): Boolean {
        if (sender.cashAccount.withdraw(FundType.DISCRETIONARY, amount) < 0) return false
        receiver.cashAccount.deposit(FundType.DISCRETIONARY, amount)
        return true
    }
}
