package com.stevenlagoy.presidency.characters.attributes.finances

import com.stevenlagoy.presidency.core.TimeManager
import java.time.LocalDate
import java.time.Period
import kotlin.math.pow

// Amortized loan
class Loan(
    val lender: FinancialEntity,
    val borrower: FinancialEntity,
    val principal: Double,
    val interestRate: Double, // APR
    val term: Period = Period.ofMonths(36), // Should not include days
    val paymentFrequency: Period = Period.ofMonths(1), // Should not include days
    val collateral: Asset? = null,
    val timeManager: TimeManager
) : FinancialInstrument(lender, borrower, principal) {

    val totalInstallments: Double = (term.toTotalMonths() / paymentFrequency.toTotalMonths()).toDouble()

    val periodicInterestRate = interestRate * paymentFrequency.toTotalMonths() / 12

    private val r = periodicInterestRate / 100

    val amortizedPayment = (r * (r + 1).pow(totalInstallments)) / ((1 + r).pow(totalInstallments) - 1)

    val totalLoanPayment: Double = amortizedPayment * totalInstallments

    fun disburse(date: LocalDate): Boolean {
        return Transaction(to=borrower, from=lender, principal, date).execute()
            && lender.balanceSheet.assets.add(Credit(timeManager,AssetType.LoanReceivable, totalLoanPayment))
            && borrower.balanceSheet.liabilities.add(Debt(totalLoanPayment))
    }

}
