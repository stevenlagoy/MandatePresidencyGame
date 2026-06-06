package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.Jsonic

data class PoliticalAlignment(
    var diplomacy:   Double = 0.0, // Globalism      -- Isolationism
    var society:     Double = 0.0, // Individualism  -- Collectivism
    var governance:  Double = 0.0, // Libertarianism -- Authoritarianism
    var economy:     Double = 0.0, // Socialism      -- Capitalism
    var tradition:   Double = 0.0, // Progressivism  -- Conservatism
    var religion:    Double = 0.0, // Secularism     -- Moralism
    var sovereignty: Double = 0.0, // Federalism     -- Unitarianism
    var conflict:    Double = 0.0, // Pacifism       -- Militarism
) : Jsonic<PoliticalAlignment> {

    init {
        assert(diplomacy   in -1.0..1.0)
        assert(society     in -1.0..1.0)
        assert(governance  in -1.0..1.0)
        assert(economy     in -1.0..1.0)
        assert(tradition   in -1.0..1.0)
        assert(religion    in -1.0..1.0)
        assert(sovereignty in -1.0..1.0)
        assert(conflict    in -1.0..1.0)
    }

    /** Turn the 8 alignment axes into one axis: `(left/progressive/lib--right/conservative/auth)` */
    fun toOneAxis() = listOf(diplomacy, society, governance, economy, tradition, religion, sovereignty, conflict).average()

    /** Turn the 8 alignment axes into the traditional two: `(left--right, auth--lib)` */
    fun toTwoAxes(): Pair<Double, Double> {
        val leftRight = listOf(economy, tradition, religion, conflict).average()
        val authLib = listOf(diplomacy, society, governance, sovereignty).average()
        return Pair(leftRight, authLib)
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("diplomacy",   diplomacy),
        JSONObject("society",     society),
        JSONObject("government",  governance),
        JSONObject("economy",     economy),
        JSONObject("tradition",   tradition),
        JSONObject("religion",    religion),
        JSONObject("sovereignty", sovereignty),
        JSONObject("conflict",    conflict),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        diplomacy   = json.get("diplomacy")   as Double
        society     = json.get("society")     as Double
        governance  = json.get("government")  as Double
        economy     = json.get("economy")     as Double
        tradition   = json.get("tradition")   as Double
        religion    = json.get("religion")    as Double
        sovereignty = json.get("sovereignty") as Double
        conflict    = json.get("conflict")    as Double
    }
}
