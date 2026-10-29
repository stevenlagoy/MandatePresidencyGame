package com.stevenlagoy.presidency.economy

import com.stevenlagoy.jsonic.JSONObject
import com.stevenlagoy.jsonic.JSONSerializable
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound

/**
 * A liability is a financial debt or oblication owed to another party.
 */
abstract class Liability(
    engine: Engine,
    var liabilityType: LiabilityType,
    var value: Double = 0.0,
) : EngineBound(engine), JSONSerializable<Liability> {

    override fun toJson() = JSONObject(hashCode().toString(), listOf(
        JSONObject("liabilityType", liabilityType.toString()),
        JSONObject("value", value)
    ))

    override fun fromJson(json: JSONObject) = apply {
        liabilityType = LiabilityType.valueOf(json.requireString("liabilityType").uppercase())
        value = json.requireDouble("value")
    }

    /** LiabilityType describes the general category of a liability. */
    enum class LiabilityType {
        /** A short-term amount owed in exchange for goods which were received on credit. */
        AccountPayable,
        /** A cost incurred by day-to-day operations. */
        Expense,
        /** A monetary amount received in advance for a product or service not yet delivered. Also called deferred revenue. */
        UnearnedRevenue,
        /** A mandatory fee collected by a government on payments for public services. */
        Tax,
        /** An amount owed to a lender which must be repaid over time with interest. */
        Debt,
        /** A present duty or responsibility to transfer economic resources to another party in the future. */
        Obligation,
    }
}
