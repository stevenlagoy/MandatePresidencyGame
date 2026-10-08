package com.stevenlagoy.presidency.citizens.attributes.finances

import com.stevenlagoy.presidency.economy.Liability

class Debt(
    value: Double,
) : Liability(LiabilityType.Debt, value)
