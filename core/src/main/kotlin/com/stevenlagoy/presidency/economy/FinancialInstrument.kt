package com.stevenlagoy.presidency.economy

open class FinancialInstrument(
    val issuer: FinancialEntity,
    val holder: FinancialEntity,
    val faceValue: Double,
)
