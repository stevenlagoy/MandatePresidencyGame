package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.citizens.attributes.finances.FundType
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import java.time.LocalDate

class Transaction(
    engine: Engine,
    val receiver: FinancialEntity,
    val sender: FinancialEntity,
    val amount: Double,
    val date: LocalDate,
) : EngineBound(engine) {

    fun execute(): Boolean {
        if (sender.cashAccount.withdraw(FundType.DISCRETIONARY, amount) < 0) return false
        receiver.cashAccount.deposit(FundType.DISCRETIONARY, amount)
        return true
    }
}
