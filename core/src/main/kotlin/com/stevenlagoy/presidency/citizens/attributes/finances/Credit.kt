package com.stevenlagoy.presidency.citizens.attributes.finances

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.TimeManager
import com.stevenlagoy.presidency.economy.Asset

class Credit(
    engine: Engine,
    assetType: AssetType,
    value: Double,
) : Asset(engine, assetType, value) {

    override fun toJson(): JSONObject {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject): Asset {
        TODO("Not yet implemented")
    }

}
