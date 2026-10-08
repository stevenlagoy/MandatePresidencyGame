package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.core.Engine

class Debt(
    engine: Engine,
    value: Double,
) : Liability(engine, LiabilityType.Debt, value)
