@file:JvmName("TimeUtils")
package com.stevenlagoy.presidency.util

import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*
import kotlin.math.abs

/** Minimum safe year to use with date util methods. */
const val MIN_SAFE_YEAR = 1583
/** Maximum safe year to use with date util methods. */
const val MAX_SAFE_YEAR = 292_278_994

/** Duration in milliseconds of one second.  */
const val secondDuration = 1000L
/** Duration in milliseconds of one minute.  */
const val minuteDuration = secondDuration * 60
/** Duration in milliseconds of one hour.  */
const val hourDuration = minuteDuration * 60
/** Duration in milliseconds of one day.  */
const val dayDuration = hourDuration * 24
/** Duration in milliseconds of one week.  */
const val weekDuration = dayDuration * 7
// there is no single month duration because months vary in length
/** Duration in milliseconds of one leap year.  */
const val leapYearDuration = 366 * dayDuration
/** Duration in milliseconds of one standard year.  */
const val yearDuration = 365 * dayDuration
/** Number of days in a year. TODO Think about leap years  */
const val daysInYear = 366

/** The epoch, 1970. */
const val epochYear = 1970
/** Milliseconds since the year zero corresponding to the epoch. */
const val epochMillis = epochYear * yearDuration

/**
 * Lengths in days of each month between January 2027 and January 2029. Index by ordinal month value.
 */
val monthsDurationsDays = intArrayOf(
    31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
    31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
    31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
)
/**
 * Duration in milliseconds of each month between January 2027 and January 2029.
 * Index by ordinal month value.
 */
val monthsDurationsMillis = monthsDurationsDays.map { it * dayDuration }.toLongArray()
/** Name of each day. Lookup from LANG_system_text.  */
val dayNames = arrayOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
/** Three-letter abbreviation of each day. Lookup from LANG_system_text.  */
val day3Abbreviations = arrayOf(
    "mon_abbreviation", "tue_abbreviation", "wed_abbreviation",
    "thu_abbreviation", "fri_abbreviation", "sat_abbreviation", "sun_abbreviation"
)
/** Two-letter abbreviation of each day. Lookup from LANG_system_text.  */
val day2Abbreviations = arrayOf(
    "mo_abbreviation", "tu_abbreviation", "we_abbreviation",
    "th_abbreviation", "fr_abbreviation", "sa_abbreviation", "su_abbreviation"
)
/** One-letter initialization of each day. Lookup from LANG_system_text.  */
val dayInitializations = arrayOf(
    "mon_initial", "tue_initial", "wed_initial", "thu_initial",
    "fri_initial", "sat_initial", "sun_initial"
)
/** Name of each month. Lookup from LANG_system_text.  */
val monthNames = arrayOf(
    "january", "february", "march", "april", "may", "june",
    "july", "august", "september", "october", "november", "december"
)
/** Abbreviation of each month. Lookup from LANG_system_text.  */
val monthAbbreviations = arrayOf(
    "jan_abbreviation", "feb_abbreviation", "mar_abbreviation",
    "apr_abbreviation", "may_abbreviation", "jun_abbreviation",
    "jul_abbreviation", "aug_abbreviation", "sep_abbreviation",
    "oct_abbreviation", "nov_abbreviation", "dec_abbreviation"
)
/** Number of each year between 2027 and 2029.  */
val yearNumbers = arrayOf("2027", "2028", "2029")

fun checkYearInSafeBounds(year: Int): Boolean {
    if (year !in MIN_SAFE_YEAR..MAX_SAFE_YEAR) {
        Logger.error(
            "DATE OUT OF BOUNDS",
            String.format("The year requested, %s, is out of the accurate bounds of [1583,292278994].", year),
            Exception()
        )
        return false
    }
    return true
}

/**
 * Calculates and returns the number of milliseconds since epoch for Jan 1
 * 00:00:00 in a given year, adjusted to the EST timezone.
 *
 * @param year The year to calculate (1900, 1970, 2000)
 * @return The number of milliseconds since epoch.
 */
fun yearToMillis(year: Int): Long {

    if (!checkYearInSafeBounds(year)) return -1L

    var millis = (year.toLong()) * yearDuration - epochMillis
    // calculate for leap years
    var numberLeapYears = 0
    if (year > epochYear) for (i in epochYear..<year) {
        if (isLeapYear(i)) numberLeapYears++
    }
    else if (year < epochYear) for (i in year..epochYear) {
        if (isLeapYear(i)) numberLeapYears--
    }
    // no calculation required if year == epochYear
    millis += (numberLeapYears * dayDuration) + (TimeZone.getTimeZone("America/New_York").rawOffset)
    return millis
}

/** Calculates the number of seconds in the given number of milliseconds.  */
fun timeToSeconds(time: Long): Double {
    return time * 1.0 / secondDuration
}

/** Calculates the number of days in the given number of milliseconds.  */
fun timeToDays(time: Long): Double {
    return time * 1.0 / dayDuration
}

/** Calculates the number of weeks in the given number of milliseconds.  */
fun timeToWeeks(time: Long): Double {
    return time * 1.0 / weekDuration
}

/** Calculates the number of years in the given number of milliseconds.  */
fun timeToYears(time: Long): Double {
    return time * 1.0 / yearDuration
}

/** Determines whether a given year is a leap year.  */
fun isLeapYear(year: Int): Boolean {
    if (year % 100 == 0) {
        return year % 400 == 0
    }
    return year % 4 == 0
}

/**
 * Creates a date from a formatted string.
 *
 * @param dateString The String to parse into a Date.
 * @return Parsed date, or `null` if unsuccessful.
 */
fun dateFromString(dateString: String): LocalDate? {
    val dateParts: Array<String?> = dateString.split("[-/]".toRegex(), limit = 3).toTypedArray()
    if (dateParts.size < 3) return null
    val year = dateParts[0]!!.toInt()
    val month = dateParts[1]!!.toInt()
    val day = dateParts[2]!!.toInt()

    return LocalDate.of(year, month, day)
}

/**
 * Returns the date in the format MM/DD for the given day ordinal. Includes leap
 * years (i = 59).
 *
 * @param dayOrdinal Day ordinal to convert to a date.
 * @return Date in the format MM/DD.
 */
fun ordinalToDateFormat(dayOrdinal: Int): String? {
    var dayOrdinal = dayOrdinal
    if (dayOrdinal < 0 || dayOrdinal > daysInYear - 1) {
        Logger.error(
            "INVALID DAY ORDINAL",
            String.format(
                "The day ordinal %d is out of bounds. Must be between 0 and %d.",
                dayOrdinal,
                daysInYear
            ),
            Exception()
        )
        return null
    }
    if (dayOrdinal == 59) return "02/29" // Leap year

    if (dayOrdinal > 59) dayOrdinal-- // Leap year


    var month = 0
    var elapsed = 0
    for (i in monthsDurationsDays.indices) {
        elapsed += monthsDurationsDays[i]
        if (elapsed > dayOrdinal) {
            month = i + 1
            break
        }
    }
    // get the day part
    val day = monthsDurationsDays[month - 1] - (elapsed - dayOrdinal) + 1

    if (day == 0) {
        Logger.error(
            "DATE CALCULATION ERROR",
            String.format("The date calculation failed to produce a valid date for day ordinal %d.", dayOrdinal),
            Exception()
        )
        return null
    }

    return String.format("%02d/%02d", month, day)
}

/**
 * Returns the day ordinal for the given date in the format MM/DD. Includes leap
 * years (i = 59).
 *
 * @param dateFormat Date in the format MM/DD to convert to a day ordinal.
 * @return Day ordinal for the given date.
 */
fun dateFormatToOrdinal(dateFormat: String): Int {
    val parts: Array<String?> = dateFormat.split("[-/]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    if (parts.size != 2) {
        Logger.error(
            "INVALID DATE FORMAT",
            String.format("The date format \"%s\" is invalid. Must be in the format MM/DD.", dateFormat),
            Exception()
        )
        return -1
    }
    val month: Int
    val day: Int
    try {
        month = parts[0]!!.toInt()
        day = parts[1]!!.toInt()
    } catch (e: NumberFormatException) {
        Logger.error(e)
        return -1
    }

    if (month < 1 || month > 12 || day < 1 || day > 31) {
        Logger.error(
            "INVALID DATE FORMAT", String.format(
                "The date \"%s\" is invalid. Months must be between 1 and 12, and days must be between 1 and 31.",
                dateFormat
            ), Exception()
        )
        return -1
    }
    var result = 0
    for (i in 0..<month - 1) {
        result += monthsDurationsDays[i]
    }
    result += day - 1
    if (month == 2 && day == 29) return 59 // Leap year
    else if (result >= 59) result++ // Leap year


    return result
}

/**
 * Calculates the amount of time in between two dates.
 *
 * @param startDate The start date to calculate the time from.
 * @param endDate   The end date to calculate the time until.
 * @return The amount of time in milliseconds between the two dates.
 */
fun millisecondsBetween(startDate: ZonedDateTime, endDate: ZonedDateTime): Long {
    return abs(endDate.toInstant().toEpochMilli() - startDate.toInstant().toEpochMilli())
}

fun yearsBetween(startDate: LocalDate, endDate: LocalDate): Int {
    return abs(startDate.getYear() - endDate.getYear())
}
