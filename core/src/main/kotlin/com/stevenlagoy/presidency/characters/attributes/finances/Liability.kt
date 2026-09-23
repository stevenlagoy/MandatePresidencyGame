package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable

abstract class Liability(
    var liabilityType: LiabilityType,
    var value: Double = 0.0,
) : JSONSerializable<Liability> {

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("liabilityType", liabilityType.toString()),
        JSONObject("value", value)
    ))

    override fun fromJson(json: JSONObject) = apply {
        liabilityType = LiabilityType.valueOf(json.requireString("liabilityType").uppercase())
        value = json.requireDouble("value")
    }

    enum class LiabilityType {
        AccountPayable,
        Expense,
        UnearnedRevenue,
        Tax,
        Debt,
        Obligation,
    }
}
