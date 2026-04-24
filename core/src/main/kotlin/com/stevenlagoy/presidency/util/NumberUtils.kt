@file:JvmName("NumberUtils")
package com.stevenlagoy.presidency.util

import kotlin.math.abs
import kotlin.math.sqrt

fun clamp(value: Int, min: Int, max: Int) = value.coerceIn(min, max)

fun clamp(value: Double, min: Double, max: Double) = value.coerceIn(min, max)

val primeCache = mutableMapOf<Number, Boolean>()

fun Int.isPrime(): Boolean = toLong().isPrime()

fun Long.isPrime(): Boolean {
    if (primeCache.contains(this)) return primeCache[this]!!

    for (i in 2..sqrt(toDouble()).toInt()) {
        if (this % i == 0L) {
            primeCache[this] = false
            return false
        }
    }
    primeCache[this] = true
    return true
}

fun Int.nextPrime(): Int = toLong().nextPrime().toInt()

fun Long.nextPrime(): Long {
    if (this < 1) return 1
    var counter = this
    while (!counter.isPrime()) counter++
    return counter
}

fun Int.toOrdinal(): String = toLong().toOrdinal()

fun Long.toOrdinal(): String {
    val index = when(abs(this) % 100) {
        in 11L..13L -> 0
        else -> if (abs(this) % 10 < 3) abs(this) % 10 else 4
    }.toInt()
    val suffixes = arrayOf("th", "st", "nd", "rd", "th")
    return (if (this < 0) "negative" else "") + abs(this).toString() + suffixes[index]
}

fun Long.toWords(): String {

    val negative = "negative"
    val numberNames = listOf(
        listOf("", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"),
        listOf("", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety")
    )
    val hundred = "hundred"
    val thousand = "thousand"
    val illion = "illion"
    val largeNumbers = listOf("", "m", "b", "tr", "quadr", "quint", "sext", "sept", "oct", "non")

    val latinNames = mapOf(
        1 to listOf("", "un", "duo", "tre", "quattuor", "quin", "sex", "septen", "octo", "novem"),
        10 to listOf("", "dec", "vigint", "trigint", "quadragint", "quinquagint", "sexagint", "septagint", "octogint", "nonagint"),
        100 to listOf("", "cent", "ducent", "trecent", "quadringent", "quingent", "sescent", "septingent", "octingent", "nongent")
    )
    val millin = "millin"
    // This will handle up to 10 ^ 10 ^ 6. Surely that's all you'll need!

    var result = ""
    val eachDigit = this.toString().split("").map { it.toInt() }.reversed()
    // Make groups of 3
    val groups = eachDigit.groupIntoTuples(3)
    var place = 1

    fun numberLessThanThousandToWords(number: Int): String {
        var res = ""
        (listOf(0, 0, 0) + number.toString().split("").map { it.toInt() }).takeLast(3).forEachIndexed { d, p ->
            res += "${numberNames[(p+1) % 2][d]} "
            if (p == 2) res += "$hundred "
        }
        return res
    }

    fun multipleOfThousandToWords(multipleOfThousand: Int): String {
        if (multipleOfThousand == 0) return ""
        if (multipleOfThousand == 1) return thousand
        if (multipleOfThousand >= 2) return "${largeNumbers[multipleOfThousand-1]}$illion"
        return ""
    }

    // A number name is based on groups of three, each with a suffix
    for ((multipleOfThousand, group) in groups.withIndex()) {
        val numberPart = numberLessThanThousandToWords(group)
        val suffix = ""
    }

    return result
}

fun Int.toWords() = toLong().toWords()

fun List<Int>.groupIntoTuples(tupleSize: Int): List<Int> {
    val pad = (3 - size % 3) % 3
    val padded = List(pad) { 0 } + this
    return padded.chunked(tupleSize).map{ (a, b, c) -> a * 100 + b * 10 + c * 1 }
}
