package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.TimeManager
import java.time.LocalDate
import java.time.Period

enum class AssetType {
    CashAsset,
    StockHolding,
    BondHolding,
    LoanReceivable,
    BusinessEquity,
    RealEstate,
    Building,
    Vehicle,
} // This doesn't seem very SOLID

abstract class Asset(
    val timeManager: TimeManager,
    val owner: FinancialEntity,
    bookValue: Double = 0.0,
    var marketValue: Double? = null,
    val depreciationPerAnnum: Double? = null,
    val depreciationPeriod: Period? = Period.ofMonths(1),
) {
    var nextDepreciationDate: LocalDate = timeManager.currentDate.toLocalDate().plus(depreciationPeriod)
        private set

    val deprecationPerPeriod = (depreciationPerAnnum ?: 0.0) * (Period.ofYears(1).toTotalMonths() / (depreciationPeriod?.toTotalMonths() ?: 1L)).toDouble()

    var bookValue: Double = bookValue
        get() {
            depreciate()
            return field
        }

    fun depreciate() {
        if (timeManager.currentDate.toLocalDate() < nextDepreciationDate) return
        nextDepreciationDate = nextDepreciationDate.plus(depreciationPeriod)
        val depreciationAmount = (depreciationPerAnnum ?: 0.0) * bookValue
        bookValue -= depreciationAmount
        depreciate() // Handle possibility of multiple periods having passed
    }

}
