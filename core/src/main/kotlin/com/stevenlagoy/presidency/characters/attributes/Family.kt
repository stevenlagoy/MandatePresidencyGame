package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import com.stevenlagoy.presidency.characters.Citizen

data class Family(
    val parents: MutableSet<Citizen>,
    var spouse: Citizen?,
    val children: MutableSet<Citizen>,
) : Jsonic<Family> {
    override fun fromJson(json: JSONObject): Family {
        return this
    }

    override fun toJson(): JSONObject {
        return JSONObject()
    }
}
