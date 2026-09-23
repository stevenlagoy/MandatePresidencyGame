package com.stevenlagoy.presidency.politics

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.linearvalue.LinearValue

data class PoliticalAlignment(
    val diplomacy:   LinearValue = LinearValue(-100, 100, 0), // Globalism         -- Isolationism
    val society:     LinearValue = LinearValue(-100, 100, 0), // Individualism     -- Collectivism
    val governance:  LinearValue = LinearValue(-100, 100, 0), // Constitutionalism -- Executivism
    val economy:     LinearValue = LinearValue(-100, 100, 0), // Interventionism   -- Market Liberalism
    val tradition:   LinearValue = LinearValue(-100, 100, 0), // Progressivism     -- Conservatism
    val religion:    LinearValue = LinearValue(-100, 100, 0), // Secularism        -- Providentialism
    val sovereignty: LinearValue = LinearValue(-100, 100, 0), // Federalism        -- Centralism
    val conflict:    LinearValue = LinearValue(-100, 100, 0), // Pacifism          -- Militarism
    val solidarity:  LinearValue = LinearValue(-100, 100, 0), // Universalism      -- Particularism
) : JSONSerializable<PoliticalAlignment> {

    /** Turn the 8 alignment axes into one axis: `(left/progressive/lib--right/conservative/auth)` */
    fun toOneAxis() = listOf(diplomacy, society, governance, economy, tradition, religion, sovereignty, conflict, solidarity).map { it.currentValue }.average()

    /** Turn the 8 alignment axes into the traditional two: `(left--right, auth--lib)` */
    fun toTwoAxes(): Pair<Double, Double> {
        val leftRight = listOf(economy, tradition, religion, conflict, solidarity).map { it.currentValue }.average()
        val authLib = listOf(diplomacy, society, governance, sovereignty).map { it.currentValue }.average()
        return Pair(leftRight, authLib)
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("diplomacy",   diplomacy.toJson()),
        JSONObject("society",     society.toJson()),
        JSONObject("government",  governance.toJson()),
        JSONObject("economy",     economy.toJson()),
        JSONObject("tradition",   tradition.toJson()),
        JSONObject("religion",    religion.toJson()),
        JSONObject("sovereignty", sovereignty.toJson()),
        JSONObject("conflict",    conflict.toJson()),
        JSONObject("solidarity",  solidarity.toJson()),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        diplomacy.fromJson(json.requireJson("diplomacy"))
        society.fromJson(json.requireJson("society"))
        governance.fromJson(json.requireJson("government"))
        economy.fromJson(json.requireJson("economy"))
        tradition.fromJson(json.requireJson("tradition"))
        religion.fromJson(json.requireJson("religion"))
        sovereignty.fromJson(json.requireJson("sovereignty"))
        conflict.fromJson(json.requireJson("conflict"))
        solidarity.fromJson(json.requireJson("solidarity"))
    }
}
