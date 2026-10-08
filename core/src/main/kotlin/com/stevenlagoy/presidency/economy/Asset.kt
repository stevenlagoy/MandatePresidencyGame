package com.stevenlagoy.presidency.economy

import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.core.TimeManager
import java.time.LocalDate
import java.time.Period

/**
 * An Asset is anything owned by a financial entity which holds economic or monetary value.
 */
abstract class Asset(
    engine: Engine,
    val assetType: AssetType,
    bookValue: Double = 0.0,
    var marketValue: Double? = null,
    val depreciationPerAnnum: Double? = null,
    val depreciationPeriod: Period? = null,
) : EngineBound(engine), JSONSerializable<Asset> {
    var nextDepreciationDate: LocalDate? = if (depreciationPeriod == null) null else engine.TIME_MANAGER.currentDate.toLocalDate().plus(depreciationPeriod)
        private set

    val deprecationPerPeriod: Double? = if (depreciationPerAnnum == null || depreciationPeriod == null) null else depreciationPerAnnum * (Period.ofYears(1).toTotalMonths() / depreciationPeriod.toTotalMonths()).toDouble()

    var bookValue = bookValue
        get() = depreciateBookValue()

    fun depreciateBookValue(): Double {
        if (nextDepreciationDate != null && engine.TIME_MANAGER.currentDate.toLocalDate() >= nextDepreciationDate) {
            nextDepreciationDate = nextDepreciationDate?.plus(depreciationPeriod)
            val depreciationAmount = (depreciationPerAnnum ?: 0.0) * bookValue
            bookValue -= depreciationAmount
            depreciateBookValue() // Handle possibility of multiple periods having passed
        }
        return bookValue
    }

    /** AssetType describes the general category of an asset. */
    enum class AssetType {
        /** Physical currency and legal tender avalable immediately for transactions. */
        CashAsset,
        /** Also known as an equity or share. Financial instrument representing fractional ownership stake in a corporation. */
        StockHolding,
        /** A financial loan owed by a government or company which receives regular interest payments. */
        BondHolding,
        /** An amount owed by a borrower. */
        LoanReceivable,
        /** Ownership value remaining in a company after subtracting total liabilities from total assets. */
        BusinessEquity,
        /** Financial value owned in a real estate property, or the market value of the property minus outstanding attached liabilities. */
        RealEstateEquity,
        /** An owned physical building constructed on land. Only the building's value is included, and the land should be [RealEstateEquity]. */
        Building,
        /** An owned vehicle, like a car, plane, or boat. */
        Vehicle,
    }
}
