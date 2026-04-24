package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

class FinancialProfile() : Jsonic<FinancialProfile> {

    constructor(json: JSONObject) : this()

    override fun toJson() = JSONObject()

    override fun fromJson(json: JSONObject) = this.apply {

    }
}
