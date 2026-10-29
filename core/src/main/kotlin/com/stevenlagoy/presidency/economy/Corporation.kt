package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.citizens.attributes.finances.BalanceSheet
import com.stevenlagoy.presidency.citizens.attributes.finances.CashAccount
import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound

/**
 * A corporation is a legal entity which can own property, sign contracts, borrow money, sue,
 * and be sued.
 */
class Corporation(
    engine: Engine,
    val name: String,
    override val balanceSheet: BalanceSheet,
    override val cashAccount: CashAccount
) : FinancialEntity, EngineBound(engine)
