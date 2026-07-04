package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.characters.attributes.Goal
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import java.time.LocalDate

class BalanceSheet(
    engine: Engine,
    var discretionaryFunds: Double = 0.0,
    var dedicatedFunds: MutableMap<Goal, Double> = mutableMapOf(),
    var assets: MutableList<Asset> = mutableListOf(),
    var liabilities: MutableList<Liability> = mutableListOf(),
    var records: MutableList<AccountRecord> = mutableListOf(),
) : JSONSerializable<BalanceSheet>, EngineBound(engine) {

    val totalAssets: Double
        get() = discretionaryFunds + dedicatedFunds.values.sum() + assets.sumOf { it.marketValue ?: 0.0 }

    val totalLiabilities: Double
        get() = liabilities.sumOf { it.value }

    val netWorth: Double
        get() = totalAssets - totalLiabilities

    constructor(engine: Engine, json: JSONObject) : this(engine) {
        fromJson(json)
    }

    fun getNetChange(since: LocalDate): Double {
        val earliestRecordSince = records
            .filter { it.date >= since }
            .maxByOrNull { it.date }!!
        return netWorth - earliestRecordSince.netWorth
    }

    fun createRecord() {
        records.add(AccountRecord(
            engine.TIME_MANAGER.currentDate.toLocalDate(),
            totalAssets,
            totalLiabilities
        ))
    }

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("discretionaryFunds", discretionaryFunds),
        JSONObject("dedicatedFunds", dedicatedFunds),
        JSONObject("assets", assets.map { it.toJson() }),
        JSONObject("liabilities", liabilities.map { it.toJson() }),

    ))

    override fun fromJson(json: JSONObject) = apply {
    }

    data class AccountRecord(
        val date: LocalDate,
        val totalAssets: Double,
        val totalLiabilities: Double
    ) { val netWorth = totalAssets - totalLiabilities }
}
