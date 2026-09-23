package com.stevenlagoy.presidency.core.linearvalue

import com.stevenlagoy.presidency.util.UnsignedDouble
import com.stevenlagoy.presidency.util.daysBetween
import java.time.LocalDate

fun interface DecayFunction {
    fun evaluate(): Double
}

class LinearDecayFunction(
    private val dailyRate: UnsignedDouble,
    private val startDate: LocalDate,
    private val dateProvider: () -> LocalDate,
) : DecayFunction {
    override fun evaluate(): Double {
        val days = daysBetween(startDate, dateProvider()).toDouble()
        return 1 - (days * dailyRate.value)
    }
}

class QuadraticDecayFunction(
    private val quadraticRate: UnsignedDouble,
    private val linearRate: UnsignedDouble,
    private val startDate: LocalDate,
    private val dateProvider: () -> LocalDate,
) : DecayFunction {
    override fun evaluate(): Double {
        val days = daysBetween(startDate, dateProvider()).toDouble()
        return 1 - (days * days * quadraticRate.value + days * quadraticRate.value)
    }
}

class CustomDecayFunction(private val provider: () -> Double) : DecayFunction {
    override fun evaluate(): Double = provider()
}
