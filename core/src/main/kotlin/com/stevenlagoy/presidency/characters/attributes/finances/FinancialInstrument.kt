package com.stevenlagoy.presidency.characters.attributes.finances

open class FinancialInstrument(
    val issuer: FinancialEntity,
    val holder: FinancialEntity,
    val faceValue: Double,
)
