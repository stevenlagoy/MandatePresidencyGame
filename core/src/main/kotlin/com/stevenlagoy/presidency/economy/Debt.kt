package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.core.Engine
import java.time.LocalDate

class Debt(
    engine: Engine,
    value: Double,
    val dateIncurred: LocalDate,
) : Liability(engine, LiabilityType.Debt, value)
