@file:JvmName("NumberUtils")
package com.stevenlagoy.presidency.util

import java.math.BigDecimal
import java.math.BigInteger
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
    var counter = this + 1
    while (!counter.isPrime()) counter++
    return counter
}

fun Int.toOrdinal(): String = toLong().toOrdinal()

fun Long.toOrdinal(): String {
    val index = when(abs(this) % 100) {
        in 11L..13L -> 0
        else -> if (abs(this) % 10 <= 3) abs(this) % 10 else 4
    }.toInt()
    val suffixes = arrayOf("th", "st", "nd", "rd", "th")
    return (if (this < 0) "negative " else "") + abs(this).toString() + suffixes[index]
}

fun List<Int>.groupIntoTuples(tupleSize: Int): List<Int> {
    val pad = (3 - size % 3) % 3
    val padded = List(pad) { 0 } + this
    return padded.chunked(tupleSize).map{ (a, b, c) -> a * 100 + b * 10 + c * 1 }
}

private val zero = "zero"
private val negative = "negative"
private val numberNames = listOf(
    listOf("", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"),
    listOf("", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety")
)
private val hundred = "hundred"
private val thousand = "thousand"
private val illion = "illion"
private val largeNumbers = listOf("", "m", "b", "tr", "quadr", "quint", "sext", "sept", "oct", "non")
private val latinNames = mapOf(
    1 to listOf("", "un", "duo", "tre", "quattuor", "quin", "sex", "septen", "octo", "novem"),
    10 to listOf("", "dec", "vigint", "trigint", "quadragint", "quinquagint", "sexagint", "septagint", "octogint", "nonagint"),
    100 to listOf("", "cent", "ducent", "trecent", "quadringent", "quingent", "sescent", "septingent", "octingent", "nongent")
)
private val millin = "millin"
// This will handle up to 10 ^ 10 ^ 6. Surely that's all you need!

private fun numberLessThanThousandToWords(number: Int): String {
    if (number == 0) return zero
    var res = ""
    (listOf(0, 0, 0) + number.toString().split("").filter { it.isNotBlank() }.map { it.toInt() }).takeLast(3).forEachIndexed { p, d ->
        when (p) {
            2 -> res += (if (d > 0) numberNames[0][d] else if (res.isBlank()) numberNames[0][d] else "") + " "
            1 -> res += numberNames[1][d] + (if(d > 0) "-" else "")
            0 -> if (d > 0) res = numberNames[0][d] + " " + hundred + " " + res
        }
    }
    return res.trim()
}

private fun multipleOfThousandToWords(multipleOfThousand: Int) = when(multipleOfThousand) {
    0 -> ""
    1 -> thousand
    in 2..10 -> "${largeNumbers[multipleOfThousand-1]}$illion"
    else -> {
        (multipleOfThousand-1).toLatinBase() + illion
    }
}

fun Int.toLatinBase(): String {
    val onesPlace = this % 10
    val tensPlace = (this % 100 - onesPlace) / 10
    val hundredsPlace = this / 100
    return latinNames[1]!![onesPlace] + latinNames[10]!![tensPlace] + latinNames[100]!![hundredsPlace]
}

private fun numberTuplesToWords(numberTuples: List<Int>): String {
    if (numberTuples.size == 1 && numberTuples[0] == 0) return zero
    var result = ""
    for ((multipleOfThousand, numberLessThanThousand) in numberTuples.withIndex()) {
        val numberPart = numberLessThanThousandToWords(numberLessThanThousand)
        val suffix = multipleOfThousandToWords(numberTuples.size - multipleOfThousand - 1)
        result += if (numberLessThanThousand != 0) "$numberPart $suffix " else ""
    }
    return normalizeNumberName(result)
}

private fun normalizeNumberName(numberName: String) = numberName
    .replace("ten-zero", "ten")
    .replace("ten-one", "eleven")
    .replace("ten-two", "twelve")
    .replace("ten-three", "thirteen")
    .replace("ten-four", "fourteen")
    .replace("ten-five", "fifteen")
    .replace("ten-six", "sixteen")
    .replace("ten-seven", "seventeen")
    .replace("ten-eight", "eighteen")
    .replace("ten-nine", "nineteen")
    .replace(Regex("\\s+"), " ")
    .replace(Regex("(?<=.)\\s*zero"), "")
    .replace(Regex("- |-$"), " ")
    .replace(Regex("\\s+"), " ")
    .trim()

private fun toWords(numberRepresentation: String, individualNumbers: Boolean = false): String? {
    if (!numberRepresentation.matches(Regex("^-?\\d+(\\.\\d+)?$"))) return null

    if (individualNumbers) {
        return numberRepresentation.fold("") { res, digit ->
            when (digit) {
                '-' -> "$res negative"
                '0' -> "$res zero"
                '1' -> "$res one"
                '2' -> "$res two"
                '3' -> "$res three"
                '4' -> "$res four"
                '5' -> "$res five"
                '6' -> "$res six"
                '7' -> "$res seven"
                '8' -> "$res eight"
                '9' -> "$res nine"
                '.' -> "$res point"
                else -> res
            }
        }.trim()
    }

    val isNegative = numberRepresentation.contains("-")
    val isWhole = !numberRepresentation.contains(".")

    val (wholePart, fractionalPart) = (numberRepresentation.replace("-", "").split(".") + listOf("0")).take(2)
    val groups = wholePart.split("").filterNot(String::isEmpty).map(String::toInt).groupIntoTuples(3)

    val wholePartWords = numberTuplesToWords(groups)
    val fractionalPartWords = toWords(fractionalPart, true)

    return "${if (isNegative) negative else ""} $wholePartWords${if (!isWhole) " point $fractionalPartWords" else ""}"
        .replace(Regex("\\s+"), " ").trim()
}

fun Int.toWords(individualNumbers: Boolean = false) = toWords(toString(), individualNumbers)

fun Long.toWords(individualNumbers: Boolean = false) = toWords(toString(), individualNumbers)

fun BigInteger.toWords(individualNumbers: Boolean = false) = toWords(toString(), individualNumbers)

fun Float.toWords(individualNumbers: Boolean = false) = toWords(toString(), individualNumbers)

fun Double.toWords(individualNumbers: Boolean = false) = toWords(toString(), individualNumbers)

fun BigDecimal.toWords(individualNumbers: Boolean = false) = toWords(toString(), individualNumbers)
