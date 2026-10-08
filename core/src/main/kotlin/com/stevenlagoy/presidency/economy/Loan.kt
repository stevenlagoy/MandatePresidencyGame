package com.stevenlagoy.presidency.economy

import com.stevenlagoy.presidency.core.Engine
import java.time.LocalDate
import java.time.Period
import kotlin.math.pow

/**
 * An amortized loan is a sum of money borrowed from a lender with an agreement to repay the amount,
 * plus added interest incurred on that amount, in regular, fixed installments.
 */
class Loan(
    engine: Engine,
    val lender: FinancialEntity,
    val borrower: FinancialEntity,
    /** The initial amount transferred from the lender to the borrower. */
    val principal: Double,
    /** The Annual Percentage Rate is a total yearly cost of borrowing money. */
    val interestRate: Double,
    /** The term is the length of time over which the borrower must repay the loan. */
    val term: Period = Period.ofMonths(36), // Should not include days
    /** The period of time between scheduled payments from the borrower to the lender. */
    val paymentFrequency: Period = Period.ofMonths(1), // Should not include days
    /**
     * A valuable asset that a borrower pledges to a lender in order to secure a loan. If the
     * borrower fails to repay the debt or defaults on the loan, the lender has the legal right to
     * seize and sell the collateral asset to recover their money.
     */
    val collateral: Asset? = null,
) : FinancialInstrument(engine, lender, borrower, principal) {

    val totalInstallments: Double = (term.toTotalMonths() / paymentFrequency.toTotalMonths()).toDouble()

    val periodicInterestRate = interestRate * paymentFrequency.toTotalMonths() / 12

    private val r = periodicInterestRate / 100

    val amortizedPayment = (r * (r + 1).pow(totalInstallments)) / ((1 + r).pow(totalInstallments) - 1)

    val totalLoanPayment: Double = amortizedPayment * totalInstallments

    fun disburse(date: LocalDate): Boolean {
        return Transaction(engine, receiver=borrower, sender=lender, amount=principal, date).execute()
            && lender.balanceSheet.assets.add(Credit(engine, Asset.AssetType.LoanReceivable, totalLoanPayment))
            && borrower.balanceSheet.liabilities.add(Debt(totalLoanPayment))
    }

}
