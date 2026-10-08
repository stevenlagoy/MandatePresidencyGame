package com.stevenlagoy.presidency.economy

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.presidency.core.Engine

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
