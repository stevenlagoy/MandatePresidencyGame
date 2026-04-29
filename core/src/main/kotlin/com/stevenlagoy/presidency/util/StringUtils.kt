@file:JvmName("StringUtils")
package com.stevenlagoy.presidency.util

import java.util.regex.Pattern

/* ---------- CORE HELPERS ------------ */

/**
 * Returns whether the character at [position] lies inside a double-quoted string.
 *
 * Quotes escaped with `\` are ignored. The scan processes characters from the
 * start of the string up to [position], toggling state on each unescaped `"`.
 *
 * @param position index to evaluate
 * @return true if inside a string literal, false otherwise
 */
fun String.isInString(position: Int): Boolean =
    substring(0, position + 1).fold(false) { inString, c ->
        when {
            c == '"' && (this[indexOf(c)] != '\\') -> !inString
            else -> inString
        }
    }

/**
 * Returns whether [position] lies inside a bracketed array `[ ... ]`.
 *
 * Handles nested arrays and ignores brackets inside quoted strings.
 */
fun String.isInArray(position: Int): Boolean = isInBoundingGroup(position, '[', ']')

/**
 * Returns whether [position] lies inside a brace-delimited object `{ ... }`.
 *
 * Handles nested objects and ignores braces inside quoted strings.
 */
fun String.isInObject(position: Int): Boolean = isInBoundingGroup(position, '{', '}')

/**
 * Generic bounded-group detector.
 *
 * Tracks nesting depth of [openBound]/[closeBound] pairs while ignoring
 * characters inside quoted strings.
 *
 * @param position index to evaluate
 * @return true if inside the bounded region
 */
fun String.isInBoundingGroup(position: Int, openBound: Char, closeBound: Char): Boolean =
    (0..position).fold(0) { depth, i ->
        when {
            this[i] == openBound && !isInString(i) -> depth + 1
            this[i] == closeBound && !isInString(i) -> depth - 1
            else -> depth
        }
    } != 0

/* ---------------------- CHAR UTILITIES ----------------------- */

/**
 * Returns true if [target] appears outside quoted strings.
 */
fun String.containsUnquotedChar(target: Char): Boolean =
    withIndex().any { (i, c) -> c == target && !isInString(i) }

/**
 * Counts occurrences of [target] outside quoted strings.
 */
fun String.countUnquotedChar(target: Char): Int =
    withIndex().count { (i, c) -> c == target && !isInString(i) }

/**
 * Finds the first index of [target] outside quoted strings.
 *
 * @return index or -1 if not found
 */
fun String.findFirstUnquotedChar(target: Char): Int = withIndex().indexOfFirst { (i, c) -> c == target && !isInString(i) }

/* ------------ SPLITTING --------------- */

/**
 * Splits this string on [separator], ignoring matches inside quoted strings.
 *
 * @param separator delimiter
 * @param limit max parts (-1 = unlimited)
 */
fun String.splitByUnquotedString(separator: String, limit: Int = -1): List<String> =
    splitByCondition(separator, limit) { i -> !isInString(i) }

/**
 * Splits ignoring separators inside arrays `[ ... ]`.
 */
fun String.splitByStringNotInArray(separator: String, limit: Int = -1) =
    splitByCondition(separator, limit) { i -> !isInArray(i) }

/**
 * Splits ignoring separators inside objects `{ ... }`.
 */
fun String.splitByStringNotInObject(separator: String, limit: Int = -1) =
    splitByCondition(separator, limit) { i -> !isInObject(i) }

/**
 * Generalized splitting utility using a predicate on index validity.
 */
private inline fun String.splitByCondition(
    separator: String,
    limit: Int,
    valid: (Int) -> Boolean
): List<String> {
    val parts = mutableListOf<String>()
    var last = 0
    var i = 0

    while (i <= length - separator.length && (limit < 0 || parts.size + 1 < limit)) {
        if (startsWith(separator, i) && valid(i)) {
            parts.add(substring(last, i))
            last = i + separator.length
            i += separator.length - 1
        }
        i++
    }

    parts.add(substring(last))
    return parts
}

/* --------------------- REPLACE --------------------- */

/**
 * Replaces the last match of [regex] with [replacement].
 */
fun String.replaceLast(regex: String, replacement: String): String {
    val matcher = Pattern.compile(regex).matcher(this)
    val match = generateSequence { if (matcher.find()) matcher.toMatchResult() else null }.lastOrNull()
    return match?.let { substring(0, it.start()) + replacement + substring(it.end()) } ?: this
}

fun String.replaceAllRegex(regex: String, replacement: String): String =
    this.replace(regex.toRegex(), replacement)

/**
 * Replaces all regex matches occurring outside quoted strings.
 */
fun String.replaceAllNotInString(regex: String, replacement: String): String =
    buildString {
        val matcher = Pattern.compile(regex).matcher(this@replaceAllNotInString)
        var last = 0
        while (matcher.find()) {
            val start = matcher.start()
            if (!isInString(start)) {
                append(this@replaceAllNotInString, last, start)
                append(replacement)
                last = matcher.end()
            }
        }
        append(this@replaceAllNotInString.substring(last))
    }

/* ---------------- TITLE CASE ---------------- */

/**
 * Title-cases this string, optionally ignoring common articles.
 */
fun String.titlecase(ignoreArticles: Boolean): String =
    titlecase(if (ignoreArticles) setOf(
            "a","an","the","for","and","nor","but","or","yet","so",
            "at","by","in","of","on","to","up","with","until","while",
            "as","off","per","via","that"
    ) else emptySet())

/**
 * Title-cases words while optionally skipping [ignoredWords].
 *
 * Non-letter prefixes are preserved.
 */
fun String.titlecase(ignoredWords: Collection<String> = emptySet()): String =
    split(" ").mapIndexed { idx, word ->
        val firstLetterIdx = word.indexOfFirst { it.isLetter() }
        if (firstLetterIdx == -1) return@mapIndexed word

        val lower = word.lowercase()
        val isEdge = idx == 0 || idx == lastIndex
        if (!isEdge && lower in ignoredWords) return@mapIndexed word

        buildString {
            append(lower.substring(0, firstLetterIdx))
            append(lower[firstLetterIdx].uppercaseChar())
            append(lower.substring(firstLetterIdx + 1))
        }
    }.joinToString(" ")

/* ---------------- CLAUSE SPLITTING ---------------- */

/**
 * Splits text into clauses using punctuation boundaries.
 *
 * Handles `. ? ! : ;` with heuristics to avoid breaking on decimals,
 * abbreviations, and similar constructs.
 */
fun String.splitClauses(): List<String> =
    split(Regex("(?<=\\S)[.?!:;]\\s+(?=[A-Z0-9])|(?<=.);\\s+(?=\\S)"))

/* ---------------- SMALL HELPERS ---------------- */

private inline fun String.anyIndexed(pred: (Int, Char) -> Boolean): Boolean =
    indices.any { pred(it, this[it]) }

private inline fun String.countIndexed(pred: (Int, Char) -> Boolean): Int =
    indices.count { pred(it, this[it]) }

private inline fun String.indexOfFirst(pred: (Int, Char) -> Boolean): Int =
    indices.firstOrNull { pred(it, this[it]) } ?: -1
