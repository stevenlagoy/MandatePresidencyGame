@file:JvmName("StringUtils")
package com.stevenlagoy.presidency.util

import java.util.regex.Pattern

/* ---------- CORE HELPERS ------------ */

fun String.normalize() = this.replace(Regex("\\s+"), "_").lowercase()
@JvmName("stringNormalize")
fun normalize(string: String) = string.normalize()

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

fun String.remove(regex: Regex): String = replace(regex, "")

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

fun String.replaceDigitsWithWords(): String =
    mapOf(
        "0" to "zero ",
        "1" to "one ",
        "2" to "two ",
        "3" to "three ",
        "4" to "four ",
        "5" to "five ",
        "6" to "six ",
        "7" to "seven ",
        "8" to "eight ",
        "9" to "nine ",
    ).entries.fold(this) { currentString, entry ->
        currentString.replace(entry.key, entry.value)
    }

/* ---------------- TITLE CASE ---------------- */

/**
 * Title-cases this string, optionally ignoring common articles.
 */
fun String.titlecase(ignoreArticles: Boolean): String =
    titlecase(if (ignoreArticles) setOf(
            "a", "an", "and", "as", "at", "but", "by", "for", "in", "nor", "of",
            "off", "on", "or", "per", "so", "that", "the", "to", "until", "up",
            "via", "while", "with", "yet",
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
    split(Regex("""(?<=\S)[.?!:;]\s+(?=[A-Z0-9])|(?<=.);\s+(?=\S)"""))

/* ---------------- FORMATTING ---------------- */

fun String.truncate(maxLength: Int): String {
    return when (length) {
        in 0..maxLength -> this
        else -> this.substring(0..maxLength-3) + "..."
    }
}
fun Any.truncate(maxLength: Int): String = toString().truncate(maxLength)

/* ---------------- COMPARISON ---------------- */

private val specialReplacements: Map<String, String> = mapOf(
    "'d " to " had ",
    "'ll " to " will ",
    "'s " to " is ",
    "'t " to "it ",
    "'ve " to " have ",
    "ain't" to "am not",
    "aren't" to "are not",
    "can't" to "can not",
    "could've" to "could have",
    "couldn't" to "could not",
    "didn't" to "did not",
    "doesn't" to "does not",
    "don't" to "do not",
    "dr." to "doctor",
    "hadn't" to "had not",
    "hasn't" to "has not",
    "haven't" to "have not",
    "he'd" to "he had",
    "he'll" to "he will",
    "he's" to "he is",
    "here'd" to "here had",
    "here'll" to "here will",
    "here're" to "here are",
    "here's" to "here is",
    "how'd" to "how did",
    "how'll" to "how will",
    "how're" to "how are",
    "how's" to "how is",
    "how've" to "how have",
    "i'd" to "i had",
    "i'll" to "i will",
    "i'm" to "i am",
    "i've" to "i have",
    "isn't" to "is not",
    "it'd" to "it had",
    "it'll" to "it will",
    "it's" to "it is",
    "let's" to "let us",
    "might'nt" to "might not",
    "might've" to "might have",
    "mr." to "mister",
    "ms." to "miss",
    "must'nt" to "must not",
    "must've" to "must have",
    "n't" to " not",
    "o'" to "of the ",
    "o'er" to "over",
    "ought've" to "ought to have",
    "oughtn't" to "ought not",
    "oughtn't've" to "ought not to have",
    "shan't" to "shall not",
    "she'd" to "she had",
    "she'll" to "she will",
    "she's" to "she is",
    "should've" to "should have",
    "shouldn't" to "should not",
    "somebody'd" to "somebody had",
    "somebody'll" to "somebody will",
    "somebody's" to "somebody is",
    "someone'd" to "someone had",
    "someone'll" to "someone will",
    "someone's" to "someone is",
    "something'd" to "something had",
    "something'll" to "something will",
    "something's" to "something is",
    "st." to "saint",
    "that'd" to "that had",
    "that'll" to "that will",
    "that's" to "that is",
    "there'd" to "there had",
    "there'll" to "there will",
    "there're" to "there are",
    "there's" to "there is",
    "these'd" to "these had",
    "these'll" to "these will",
    "these're" to "these are",
    "they'd" to "they had",
    "they'll" to "they will",
    "they're" to "they are",
    "they've" to "they have",
    "this'd" to "this had",
    "this'll" to "this will",
    "this's" to "this is",
    "those'd" to "those had",
    "those'll" to "those will",
    "those're" to "those are",
    "wasn't" to "was not",
    "weren't" to "were not",
    "what'd" to "what did",
    "what'll" to "what will",
    "what're" to "what are",
    "what's" to "what is",
    "what've" to "what have",
    "when'd" to "when did",
    "when'll" to "when will",
    "when're" to "when are",
    "when's" to "when is",
    "when've" to "when have",
    "where'd" to "where did",
    "where'll" to "where will",
    "where're" to "where are",
    "where's" to "where is",
    "where've" to "where have",
    "which'd" to "which did",
    "which'll" to "which will",
    "which're" to "which are",
    "which's" to "which is",
    "which've" to "which have",
    "who'd" to "who did",
    "who'll" to "who will",
    "who're" to "who are",
    "who's" to "who is",
    "who've" to "who have",
    "why'd" to "why did",
    "why'll" to "why will",
    "why're" to "why are",
    "why's" to "why is",
    "why've" to "why have",
    "will've" to "will have",
    "won't" to "will not",
    "would've" to "would have",
    "wouldn't" to "would not",
    "y'all" to "you all",
    "you'd" to "you had",
    "you'll" to "you will",
    "you're" to "you are",
    "you've" to "you have",
)

private fun String.applySpecialReplacements(): String =
    specialReplacements.entries.fold(this) { currentString, entry ->
        currentString.replace(entry.key, entry.value)
    }

private val fuzzyCompareCache: MutableMap<Pair<String, String>, Double> = mutableMapOf()

private fun String.comparables(): List<String> = listOf(
    this,
    this.lowercase(),
    this.remove(Regex("""\p{P}""")), // Remove punctuation
    this.remove(Regex("""\p{P}""")).lowercase(),
    this.remove(Regex("""\s""")), // Remove whitespace
    this.remove(Regex("""\s""")).lowercase(),
    this.remove(Regex("""[\p{P}\s]""")), // Remove punctuation and whitespace
    this.remove(Regex("""[\p{P}\s]""")).lowercase(),
    this.remove(Regex("""\W""")), // Remove non-word characters [^a-zA-Z0-9_]
    this.remove(Regex("""\W""")).lowercase(),
    this.remove(Regex("""[^a-zA-Z]""")), // Remove everything except letters
    this.remove(Regex("""[^a-zA-Z]""")).lowercase(),
    this.lowercase().applySpecialReplacements(), // Special replacements (contractions, abbreviations, etc)
    this.lowercase().applySpecialReplacements().remove(Regex("""\W""")), // Special replacements and remove non-word characters
).distinct()

/**
 * Compare two strings using fuzzy matching. Returns the similarity between
 * the two strings.
 *
 * 1.0 = The strings are identical
 * 0.0 = The strings share no chaacters in common
 *
 * Two strings may not have a similarity greater than the percentage
 * difference of their lengths. I.E. a string of length 10 may be up to 10%
 * similar to a string of length 100, and vice versa.
 *
 * This method is reflexive: A.fuzzyCompare(A) == 1.0
 * This method is symmetric: A.fuzzyCompare(B) == B.fuzzyCompare(A)
 * This method is NOT transitive: A.fuzzyCompare(B) == 0.8 and B.fuzzyCompare(C) == 0.8 =/=> A.fuzzyCompare(C) == 0.8
 */
fun String.fuzzyCompare(other: String): Double {
    if (this.isEmpty() || other.isEmpty()) return 0.0

    val key = Pair(this, other)
    fuzzyCompareCache[key]?.let { return it }
    fuzzyCompareCache[Pair(other, this)]?.let { return it }

    val comparablesA = this.comparables()
    val comparablesB = other.comparables()

    var best = 0.0
    for (a in comparablesA) for (b in comparablesB) {
        if (a.isEmpty() || b.isEmpty()) continue
        val similarity = 1 - a.damerauLevenshtein(b).toDouble() / maxOf(a.length, b.length)
        best = maxOf(best, similarity)
    }

    fuzzyCompareCache[key] = best
    return best
}

fun String.damerauLevenshtein(other: String): Int {
    val n = this.length; val m = other.length
    val d = Array(n + 1) { IntArray(m + 1) }
    for (i in 0..n) d[i][0] = i
    for (j in 0..m) d[0][j] = j

    for (i in 1..n) for (j in 1..m) {
        val subCost = if (this[i - 1] == other[j - 1]) 0 else 1
        d[i][j] = minOf(
            d[i - 1][j] + 1,           // deletion
            d[i][j - 1] + 1,           // insertion
            d[i - 1][j - 1] + subCost, // substitution
        )
        if (i > 1 && j > 1 && this[i - 1] == other[j - 2] && this[i - 2] == other[j - 1]) {
            d[i][j] = minOf(d[i][j], d[i - 2][j - 2] + 1) // transposition
        }
    }
    return d[n][m]
}

/**
 * Compare two strings and determine if they match. Matching means the strings'
 * similarity is greater than the threshold value (by default, 0.8).
 *
 * This method is reflexive: A.matches(A) == true
 * This method is symmetric: A.matches(B) == B.matches(A)
 * This method is NOT transitive: A.matches(B) and B.matches(C) =/=> A.matches(C)
 */
fun String.fuzzyMatches(other: String, threshold: Double = 0.8): Boolean = fuzzyCompare(other) >= threshold

fun String.metaphone() = this.lowercase().replace(Regex("""\p{P}"""), "").replaceDigitsWithWords()
    // Drop duplicate adjacent letters, except for 'C'.
    .replace(Regex("""([^c])\\1+"""), "$1")
    // If the word begins with 'KN', 'GN', 'PN', 'AE', 'WR', drop the first letter.
    .replace(Regex("""\b[kgp](n)|\ba(e)|\bw(r)"""), "$1$2$3")
    // Drop 'B' after 'M' at the end of the word.
    .replace(Regex("""mb(s|er|ed|ers|ing?)\b"""), "m$1")
    // 'C' transforms to 'X' if followed by 'IA' or 'H' (unless in latter case, it is part of '-SCH-', in which case it transforms to 'K'). 'C' transforms to 'S' if followed by 'I', 'E', or 'Y'. Otherwise, 'C' transforms to 'K'.
    .replace("sch", "skh").replace(Regex("""c(ia)|c(h)"""), "x$1$2").replace(Regex("""c([iey])"""), "s$1").replace("c", "k")
    // 'D' transforms to 'J' if followed by 'GE', 'GY', or 'GI'. Otherwise, 'D' transforms to 'T'.
    .replace(Regex("""d(ge|gy|gi)"""), "j$1").replace("d", "t")
    // Drop 'G' if followed by 'H' and 'H' is not at the end or before a vowel. Drop 'G' if followed by 'N' or 'NED' and is at the end.
    .replace(Regex("""g(h[^aeiouy\s])"""), "$1").replace(Regex("""g(n(ed\s)?)"""), "$1")
    // 'G' transforms to 'J' if before 'I', 'E', or 'Y', and is not in 'GG'. Otherwise, 'G' transforms to 'K'.
    .replace(Regex("""(?<!g)g([iey])"""), "j$1").replace("g", "k")
    // Drop 'H' if after a vowel and not before a vowel.
    .replace(Regex("""([aeiouy])h([^aeiouy])"""), "$1$2")
    // 'CK' transforms to 'K'
    .replace("ck", "k")
    // 'PH' transforms to 'F'
    .replace("ph", "f")
    // 'Q' transforms to 'K'
    .replace("q", "k")
    // 'S' transforms to 'X' if followed by 'H', 'IO', or 'IA'.
    .replace(Regex("""s(h|io|ia)"""), "x")
    // 'T' transforms to 'X' if followed by 'IA' or 'IO'. 'TH' transforms to '0'. Drop 'T' if followed by 'CH'.
    .replace(Regex("""t(ia|io)"""), "x$1").replace("th", "0").replace(Regex("""t(ch)"""), "$1")
    // 'V' transforms to 'F'
    .replace("v", "f")
    // 'WH' transforms to 'W' at the beginning. Drop 'W' if not followed by a vowel.
    .replace(Regex("""\b(w)h"""), "$1").replace(Regex("""w([aeiouy])"""), "$1")
    // 'X' transforms to 'S' if at the beginning. Otherwise, 'X' transforms to 'KS'.
    .replace(Regex("""\bx"""), "s").replace("x", "ks")
    // Drop 'Y' if not followed by a vowel.
    .replace(Regex("""y([^aeiouy])"""), "$1")
    // 'Z' transforms to 'S'.
    .replace("z", "s")
    // Drop all vowels unless it is the beginning.
    .replace(Regex("""(?<!\b)[aeiouy]"""), "")

/* ---------------- SMALL HELPERS ---------------- */

private inline fun String.anyIndexed(pred: (Int, Char) -> Boolean): Boolean =
    indices.any { pred(it, this[it]) }

private inline fun String.countIndexed(pred: (Int, Char) -> Boolean): Int =
    indices.count { pred(it, this[it]) }

private inline fun String.indexOfFirst(pred: (Int, Char) -> Boolean): Int =
    indices.firstOrNull { pred(it, this[it]) } ?: -1
