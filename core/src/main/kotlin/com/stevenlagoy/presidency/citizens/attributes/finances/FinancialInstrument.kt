package com.stevenlagoy.presidency.citizens.attributes.finances

open class FinancialInstrument(
    val issuer: FinancialEntity,
    val holder: FinancialEntity,
    val faceValue: Double,
)
