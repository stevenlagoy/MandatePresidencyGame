package com.stevenlagoy.presidency.citizens.attributes.finances

import com.stevenlagoy.presidency.core.Engine
import com.stevenlagoy.presidency.core.EngineBound
import com.stevenlagoy.presidency.economy.Debt
import com.stevenlagoy.presidency.economy.FinancialEntity
import com.stevenlagoy.presidency.economy.Loan
import com.stevenlagoy.presidency.util.clamp
import com.stevenlagoy.presidency.util.daysBetween
import com.stevenlagoy.presidency.util.daysInWeek
import com.stevenlagoy.presidency.util.daysInYear
import java.time.LocalDate
import kotlin.div
import kotlin.math.max
import kotlin.times

/**
 * A credit score is a three-digit number from 300 to 850 which predicts how likely a borrower is
 * to repay borrowed money. This is used by financial institutions to evaluate financial reliability.
 */
class CreditScore(
    engine: Engine,
    val entity: FinancialEntity,
) : EngineBound(engine) {

    companion object {
        const val minimum = 300.0
        const val maximum = 850.0
        const val monthsOfCreditConsidered = 24 // FICO 10T uses 2 years, FICO 8 uses just 1
        const val yearsOfPaymentsConsidered = 7
        const val latePaymentGraceDays = 30

        /**
         * Calculate FICO score from credit details.
         *
         * @param paymentHistory Map of scheduled payment dates to the number of days late the actual payment was.
         */
        fun FICOScore(engine: Engine, paymentHistory: Map<LocalDate, Int>, averageMonthlyCreditUtilization: Double, lengthOfHistory: Double, newCredit: Double, creditMix: Double): Double {
            // Payment History
            fun yearsAgo(date: LocalDate): Double = engine.TIME_MANAGER.daysAgo(date).toDouble() / daysInYear
            val filteredPaymentHistory = paymentHistory.filter { (date, daysLate) -> yearsAgo(date) <= yearsOfPaymentsConsidered && daysLate > latePaymentGraceDays }
            val totalOnTimePayments = paymentHistory.count { (_, daysLate) -> daysLate <= latePaymentGraceDays }
            val scaledPaymmentHistory = filteredPaymentHistory.map { (date, daysLate) -> (1.0 / yearsOfPaymentsConsidered) * yearsAgo(date) * daysLate }
            val paymentAverageDaysLate = scaledPaymmentHistory.sum() / totalOnTimePayments // Average days late that payments can be expected
            val paymentHistoryMetric = 5.0 / (paymentAverageDaysLate + 5)

            // Credit Utilization


            return (
                (paymentHistoryMetric * 0.35) +
                (creditUtilization * 0.30) +
                (lengthOfHistory * 0.15) +
                (newCredit * 0.10) +
                (creditMix * 0.10)
            )
        }
    }

    /** List of all scheduled payments and how many days late they were (negative value means paid in advance of due date). */
    val paymentHistory: Map<LocalDate, Int> get() {
        val allPayments = entity.financialInstruments.filterIsInstance<Loan>().flatMap { it.paymentHistory }
        return allPayments.associate { payment -> payment.scheduledDate to payment.daysLate }
    }

    /** Map of months in the past to how much of the credit limit was used for that month. */
    val averageMonthlyCreditUtilization: Double get() {
        val allDebts = entity.balanceSheet.liabilities.filterIsInstance<Debt>()
        val debtsMonthly: MutableMap<Int, Double> = mutableMapOf() // Keys are months ago
        for (debt in allDebts) {
            val monthsAgo = daysBetween(debt.dateIncurred, engine.TIME_MANAGER.currentDate.toLocalDate()) / daysInWeek * 4 // TODO: assuming a month is 4 weeks
            if (monthsAgo !in 0..monthsOfCreditConsidered) continue
            if (monthsAgo !in debtsMonthly) debtsMonthly[monthsAgo] = 0.0
            debtsMonthly[monthsAgo] = debtsMonthly[monthsAgo]!! + debt.value
        }
        return (entity.monthlyCreditLimit * debtsMonthly.size) / debtsMonthly.values.sum() // monthly credit limit / avg monthly credit utilization
    }


    fun evaluate(): Double {
        var creditScore = 0.0
        val today = engine.TIME_MANAGER.currentDate

        // Payment history
        val allLoans = entity.financialInstruments.filterIsInstance<Loan>()
        val totalPayments = allLoans.sumOf { loan -> loan.paymentHistory.count() }
        val latePayments = allLoans.sumOf { loan -> loan.paymentHistory.count { !it.onTime } }
        val lateRatio = totalPayments / latePayments
        creditScore += (lateRatio * maximum * 0.35)

        // Amounts owed and utilization
        val allDebts = entity.balanceSheet.liabilities.filterIsInstance<Debt>()
        val totalDebt = allDebts.sumOf { max(it.value, 0.0) }
        // Consider the last 6 months of credit
        val debtsMonthly: MutableMap<Int, Double> = mutableMapOf() // Keys are months ago, given as negative values (-6 is 6 months ago)
        for (debt in allDebts) {
            val monthsAgo = daysBetween(debt.dateIncurred, today.toLocalDate()) / daysInWeek * 4 // TODO: assuming a month is 4 weeks
            if (monthsAgo > monthsOfCreditConsidered) continue
            if (-monthsAgo !in debtsMonthly) debtsMonthly[-monthsAgo] = 0.0
            debtsMonthly[-monthsAgo] = debt.value + debtsMonthly[-monthsAgo]!!
        }
        val avgUtilization = (entity.monthlyCreditLimit * debtsMonthly.size) / debtsMonthly.values.sum() // Monthly credit limit / monthly credit utilization
        // Turn utilization into a score (~30% is best)

        // Length of credit history
        val earliestHistoryDate = entity.financialInstruments.minByOrNull { it.date }?.date
        val elapsed = if (earliestHistoryDate != null) daysBetween(today.toLocalDate(), earliestHistoryDate) else 0
        // Turn elapsed into a double

        // Credit mix


        // New credit applications


        return clamp(creditScore, minimum, maximum)
    }



}
