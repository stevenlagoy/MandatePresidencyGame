package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.core.TimeManager

class Credit(
    timeManager: TimeManager, // This should not be necessary here - TODO chase and remove this dependency
    owner: FinancialEntity,
    value: Double,
) : Asset(timeManager, owner, value) {
}
