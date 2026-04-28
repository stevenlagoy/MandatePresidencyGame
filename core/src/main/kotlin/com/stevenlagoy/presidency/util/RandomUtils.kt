@file:JvmName("RandomUtils")
package com.stevenlagoy.presidency.util

import kotlin.math.max
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
