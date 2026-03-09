package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.characters.attributes.Goal
import com.stevenlagoy.presidency.core.TimeManager
import java.time.LocalDate

data class BalanceSheet(
    var timeManager: TimeManager
) {
    class AccountRecord(
        val date: LocalDate,
        val totalAssets: Double,
        val totalLiabilities: Double
    ) { val netWorth = totalAssets - totalLiabilities }

    var discretionaryFunds = 0.0
    var dedicatedFunds = mutableMapOf<Goal, Double>()
    var assets = mutableListOf<Asset>()
    val totalAssets: Double
        get() = discretionaryFunds + dedicatedFunds.values.sum() + assets.sumOf { it.value }
    var liabilities = mutableListOf<Liability>()
    val totalLiabilities: Double
        get() = liabilities.sumOf { it.value }
    var records = mutableListOf<AccountRecord>()

    val netWorth: Double
        get() = totalAssets - totalLiabilities

    fun getNetChange(since: LocalDate): Double {
        val earliestRecordSince = records
            .filter { it.date >= since }
            .maxByOrNull { it.date }!!
        return netWorth - earliestRecordSince.netWorth
    }

    fun createRecord() {
        records.add(AccountRecord(
            timeManager.currentDate.toLocalDate(),
            totalAssets,
            totalLiabilities
        ))
    }
}
