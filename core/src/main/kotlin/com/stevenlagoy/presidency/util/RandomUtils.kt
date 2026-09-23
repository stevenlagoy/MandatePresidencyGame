@file:JvmName("RandomUtils")
package com.stevenlagoy.presidency.util

import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.sin
import kotlin.random.Random

fun Random.chance(chance: Double): Boolean {
    if (chance < 0.0) return false
    if (chance > 1.0) return true
    return nextPercent() <= chance
}
@JvmOverloads
fun chance(chance: Double, random: Random = Random.Default) = random.chance(chance)

/** Roll a die with the given number of sides and return the result. */
fun Random.rollDie(sides: Int = 6) = nextInt(1, sides)
@JvmOverloads
fun rollDie(sides: Int = 6, random: Random = Random.Default) = random.rollDie(sides)

/** Roll a number of dice with the given number of sides and return the results. */
fun Random.rollDice(dice: Int, sides: Int = 6) = List(dice) { nextInt(sides) }
@JvmOverloads
fun rollDice(dice: Int, sides: Int = 6, random: Random = Random.Default) = random.rollDice(dice, sides)

/** Roll dice with the given numbers of sides and return the results. */
fun Random.rollDice(diceSides: IntArray): List<Int> = List(diceSides.size) { index -> nextInt(diceSides[index]) }
@JvmOverloads
fun rollDice(diceSides: IntArray, random: Random = Random.Default) = random.rollDice(diceSides)

@JvmOverloads
fun nextInt(from: Int, until: Int, random: Random = Random.Default) = random.nextInt(from, until)

/** Return a percentage between 0 and 1. */
fun Random.nextPercent() = nextFloat()
@JvmOverloads @JvmName("randNextPercent")
fun nextPercent(random: Random = Random.Default) = random.nextPercent()

fun Random.nextFloat(range: ClosedRange<Float>) = nextFloat(range.start, range.endInclusive)
@JvmOverloads

fun Random.nextFloat(from: Float = 0.0f, until: Float = 1.0f) = nextFloat() * (until - from) + from
@JvmOverloads @JvmName("randNextFloat")
fun nextFloat(from: Float = 0.0f, until: Float = 1.0f, random: Random = Random.Default) = random.nextFloat(from, until)

/** Select and return one of the given items. */
fun <E> Random.select(vararg items: E) = select(items.toList())

/** Select and return one of the items in the given collection. */
fun <E> Random.select(items: Collection<E>): E? {
    if (items.isEmpty()) return null
    return items.toList()[nextInt(0, items.size)]
}
@JvmOverloads @JvmName("randSelect")
fun <E> select(items: Collection<E>, random: Random = Random.Default) = random.select(items)

@JvmOverloads @JvmName("randSelect")
fun <E> select(items: Array<E>, random: Random = Random.Default) = random.select(items.toList())

/**
 * Select and return an item based on the weights. For each index `i` in each array,
 * `weights[i] / weights.sum()` corresponds to the probability that `items[i]` is selected.
 *
 * @param items   List of selectable items. Must have same length as weights array.
 * @param weights List of weights for each item. Must have same length as items array.
 * @return One element of `items`, or `null` if empty.
 */
fun <E> Random.weightedSelect(items: List<E>, weights: List<Number>) =
    weightedSelect(items.zip(weights) { item, weight -> item to weight }.toMap())
@JvmOverloads
fun <E> weightedSelect(items: List<E>, weights: List<Number>, random: Random = Random.Default) = random.weightedSelect(items, weights)

/**
 * Select and return an item based on the weights. For each element `k` and value `v` in the map,
 * `v / items.values.sum()` corresponds to the probability that `k` is selected.
 *
 * @param items Map of selectable items to number weights
 * @return One key from `items`, or `null` if empty.
 */
fun <E> Random.weightedSelect(items: Map<E, Number>): E? {
    if (items.isEmpty()) return null
    val totalWeight = items.values.sumOf { max(it.toDouble(), 0.0) }
    val randNum = nextDouble(totalWeight)
    var cumulativeWeight = 0.0
    items.forEach { (key, value) ->
        cumulativeWeight += max(value.toDouble(), 0.0)
        if (randNum < cumulativeWeight) return key
    }
    return null
}
@JvmOverloads
fun <E> weightedSelect(items: Map<E, Number>, random: Random = Random.Default) = random.weightedSelect(items)

fun Random.probabilisticCount(probability: Double): Int {
    if (probability < 0.0) return 0
    if (probability > 1.0) return Int.MAX_VALUE
    var count = 0
    while (nextPercent() <= probability) count++
    return count
}
@JvmOverloads
fun probabilisticCount(probability: Double, random: Random = Random.Default) = random.probabilisticCount(probability)

@JvmOverloads
fun skewedDistribution(average: Double, min: Double = 0.0, max: Double = 1.0): (Double) -> Double {
    require(average in min..max) { "Must satisfy min <= average <= max" }
    val concentration = 6.0 // Higher = sharper / taller peak
    // Normalize mode to [0, 1]
    val mode = (average - min) / (max - min)
    // Derive Beta shape parameters from desired mode
    val alpha = mode * (concentration - 2.0) + 1.0
    val beta = (1.0 - mode) * (concentration - 2.0) + 1.0
    fun logGamma(x: Double): Double {
        // Lanczos approximation
        val g = 7
        val c = doubleArrayOf(
            0.9999999999998099,
            676.5203681218851, -1259.1392167224028,
            771.3234287776531, -176.6150291621406,
            12.507343278686905, -0.13857109526572012,
            9.984369578019572E-6, 1.5056327351493116e-7
        )
        var x = x
        if (x < -0.5) return ln(Math.PI / sin(Math.PI * x)) - logGamma(1.0 - x)
        x -= 1.0
        var a = c[0]
        val t = x + g + 0.5
        for (i in 1..g + 1) a += c[i] / (x + i)
        return 0.5 * ln(2.0 * Math.PI) + (x + 0.5) * ln(t) - t + ln(a)
    }

    val logBeta = logGamma(alpha) + logGamma(beta) - logGamma(alpha + beta)
    // Compute PDF value at mode for normalization (so peak = 1.0)
    val logPeakUnscaled = (alpha - 1.0) * ln(mode) + (beta - 1.0) * ln(1.0 - mode) - logBeta
    val peakUnscaled = exp(logPeakUnscaled)
    return { x: Double ->
        if (x <= min || x >= max) { 0.0 }
        else {
            val t = (x - min) / (max - min) // Map to [0, 1]
            val logPDF = (alpha - 1.0) * ln(t) + (beta - 1.0) * ln(1.0 - t) - logBeta
            exp(logPDF) / peakUnscaled // Normalize so peak = 1.0
        }
    }
}

/**
 * Sample a value from the given probability density function over [min, max] using rejection sampling.
 * The PDF does not need to be normalized, but does need to be non-negative with [PDFMax] as an upper bound on its output over the domain.
 *
 * @param PDF    Function mapping a Double in [min, max] to a non-negative density.
 * @param min    Lower bound of the domain
 * @param max    Upper bound of the domain
 * @param PDFMax Upper bound on the PDF's output. Defaults to 1.0 for normalized peaks (as given by [skewedDistribution]).
 */
fun Random.samplePDF(
    PDF: (Double) -> Double,
    min: Double,
    max: Double,
    PDFMax: Double = 1.0,
) : Double {
    while (true) {
        val x = nextDouble(min, max)
        val y = nextDouble(PDFMax)
        if (y < PDF(x) || PDF(x).isNaN()) return x
    }
}
@JvmOverloads @JvmName("randSamplePDF")
fun samplePDF(
    PDF: (Double) -> Double,
    min: Double,
    max: Double,
    PDFMax: Double = 1.0,
    random: Random = Random.Default
) = random.samplePDF(PDF, min, max, PDFMax)




















