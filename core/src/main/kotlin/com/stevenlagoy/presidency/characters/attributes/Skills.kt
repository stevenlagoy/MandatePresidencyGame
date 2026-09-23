package com.stevenlagoy.presidency.characters.attributes

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.linearvalue.LinearValue

/**
 * Track the skills of a Character including Legislative, Executive, and Judicial skills.
 *
 * @property legislative Legislative skill, representing ability to plan effectively, leverage advantages, and make lasting decisions.
 * @property executive Executive skill, representing ability to make decisions quickly, apply charismatic persuasion, and execute strong or decisive actions.
 * @property judicial Judicial skill, representing ability to inspect facts, reason through problems, apply precedent, and make informed decisions.
 * @property aptitude Overall aptitude, calculated as the sum of all skills base values (before modifiers are applied).
 *
 * @author Steven LaGoy
 */
data class Skills(
    val legislative: LinearValue = LinearValue(0, 100, 50),
    val executive: LinearValue = LinearValue(0, 100, 50),
    val judicial: LinearValue = LinearValue(0, 100, 50),
) : JSONSerializable<Skills>
{

    val aptitude: Double
        get() = legislative.base + executive.base + judicial.base

    constructor(legislative: Int, executive: Int, judicial: Int) : this(
        LinearValue(0, 100, legislative),
        LinearValue(0, 100, executive),
        LinearValue(0, 100, judicial)
    )

    constructor(json: JSONObject) : this() {
        fromJson(json)
    }

    override fun toJson() = JSONObject(this.hashCode().toString(), listOf(
        JSONObject("legislative", legislative.toJson()),
        JSONObject("executive", executive.toJson()),
        JSONObject("judicial", judicial.toJson()),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        legislative.fromJson(json.requireJson("legislative"))
        executive.fromJson(json.requireJson("executive"))
        judicial.fromJson(json.requireJson("judicial"))
    }

}
