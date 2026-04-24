package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic
import kotlin.collections.mapOf

class CharacterAppearance(
    var visualAge: Int,
) : Jsonic<CharacterAppearance>
{

    override fun toJson() = JSONObject(hashCode().toString(), mapOf(
        "visual_age" to visualAge,
    ))

    override fun fromJson(json: JSONObject): CharacterAppearance {
        this.visualAge = (json.get("visual_age") as String).toInt()
        return this
    }
}
