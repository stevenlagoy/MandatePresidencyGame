package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import java.time.LocalDate

/**
 * A financial instrument is a document representing a legal agreement involving monetary value.
 */
open class FinancialInstrument(
    engine: Engine,
    val issuer: FinancialEntity,
    val holder: FinancialEntity,
    val faceValue: Double,
    val date: LocalDate,
) : EngineBound(engine)
