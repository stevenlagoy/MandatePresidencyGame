package com.stevenlagoy.presidency.citizens.attributes.finances

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.TimeManager

class Credit(
    timeManager: TimeManager,
    assetType: AssetType,
    value: Double,
) : Asset(timeManager, assetType, value) {

    override fun toJson(): JSONObject {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject): Asset {
        TODO("Not yet implemented")
    }

}
