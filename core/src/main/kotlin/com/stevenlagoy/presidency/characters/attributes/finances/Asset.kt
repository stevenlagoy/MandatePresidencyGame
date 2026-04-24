package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.TimeManager
import java.time.LocalDate
import java.time.Period

abstract class Asset(
    val timeManager: TimeManager,
    val assetType: AssetType,
    bookValue: Double = 0.0,
    var marketValue: Double? = null,
    val depreciationPerAnnum: Double? = null,
    val depreciationPeriod: Period? = null,
) {
    var nextDepreciationDate: LocalDate? = if (depreciationPeriod == null) null else timeManager.currentDate.toLocalDate().plus(depreciationPeriod)
        private set

    val deprecationPerPeriod: Double? = if (depreciationPerAnnum == null || depreciationPeriod == null) null else depreciationPerAnnum * (Period.ofYears(1).toTotalMonths() / depreciationPeriod.toTotalMonths()).toDouble()

    var bookValue = bookValue
        get() = depreciateBookValue()

    fun depreciateBookValue(): Double {
        if (nextDepreciationDate != null && timeManager.currentDate.toLocalDate() >= nextDepreciationDate) {
            nextDepreciationDate = nextDepreciationDate?.plus(depreciationPeriod)
            val depreciationAmount = (depreciationPerAnnum ?: 0.0) * bookValue
            bookValue -= depreciationAmount
            depreciateBookValue() // Handle possibility of multiple periods having passed
        }
        return bookValue
    }

}
