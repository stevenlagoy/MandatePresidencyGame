package com.stevenlagoy.presidency.core

import java.time.LocalDate
import java.time.Period

class CompoundingValue(
    /** Initial amount of the value. */
    val principal: Double,
    val termUntil: LocalDate,
    /** Period of time between compounds. */
    val compoundingPeriod: Period,
    /** Compound rate, as a percentage of the current amount. */
    val rate: Double,
    val timeManager: TimeManager,
) {
    /** Current value including interests. */
    var current = principal
        get() {
            compound()
            return field
        }

    var nextCompoundDate: LocalDate? = timeManager.currentDate.toLocalDate()
        private set

    fun compound() {
        if (timeManager.currentDate.toLocalDate() < nextCompoundDate) return
        nextCompoundDate = nextCompoundDate?.plus(compoundingPeriod)
        val totalCurrent = principal + current
        val interest = totalCurrent * rate
        current += interest
        compound() // Handle possibility of multiple periods having passed
    }
}
