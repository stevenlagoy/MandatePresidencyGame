package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound

class CashAccount(
    engine: Engine,
    balances: MutableMap<FundType, Double> = mutableMapOf()
) : JSONSerializable<CashAccount>, EngineBound(engine) {

    var balances = balances
        internal set

    constructor (engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    fun deposit(fundType: FundType, amount: Double): Double {
        balances[fundType] = balances.getOrDefault(fundType, 0.0) + amount
        return balances[fundType]!!
    }

    fun withdraw(fundType: FundType, amount: Double): Double {
        if (balances.containsKey(fundType) && balances[fundType]!! >= amount) {
            balances[fundType] = balances[fundType]!! - amount
            return balances[fundType]!!
        }
        return (balances[fundType] ?: 0.0) - amount
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("balances", balances.map { it.key.toString() to it.value }),
    ))

    override fun fromJson(json: JSONObject) = this.apply {
        // TODO
    }
}
