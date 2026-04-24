package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.core.TimeManager

class Credit(
    timeManager: TimeManager,
    assetType: AssetType,
    value: Double,
) : Asset(timeManager, assetType, value) {
}
