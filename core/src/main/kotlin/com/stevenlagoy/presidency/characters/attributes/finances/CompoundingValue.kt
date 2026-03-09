package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.core.TimeManager
import java.time.LocalDate
import java.time.Period

class CompoundingValue(
    val principal: Double,
    val termUntil: LocalDate,
    val compoundingPeriod: Period,
    val rate: Double,
    val timeManager: TimeManager,
) {
    var current = principal
        get() {
            compound()
            return field
        }

    var nextPaymentDate: LocalDate? = timeManager.currentDate.toLocalDate()
        private set

    fun compound() {
        if (timeManager.currentDate.toLocalDate() < nextPaymentDate) return
        nextPaymentDate = nextPaymentDate?.plus(compoundingPeriod)
        val totalCurrent = principal + current
        val interest = totalCurrent * rate
        current += interest
        compound() // Handle possibility of multiple periods having passed
    }
}
