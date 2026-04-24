package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

class PoliticalAlignment(
    conservativeProgressive: Double,
    authoritarianLibertarian: Double,
    economicLeftRight: Double,
    collectivistIndividualist: Double,
    isolationistInterventionist: Double,
) : Jsonic<PoliticalAlignment> {

    override fun toJson(): JSONObject? {
        TODO("Not yet implemented")
    }

    override fun fromJson(json: JSONObject?): PoliticalAlignment? {
        TODO("Not yet implemented")
    }
}
