package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.jsonic.JSONObject

data class CashAccount(
    val balances: MutableMap<FundType, Double> = mutableMapOf()
) {
    constructor (json: JSONObject) : this()

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
}
