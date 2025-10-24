/*
 * TimeManager.java
 * Steven LaGoy
 * Created: 10 December 2024 at 8:21 AM
 * Modified: 20 October 2025
 */

package main.core;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import core.JSONObject;

public class TimeManager extends Manager {

    // STATIC VARIABLES ---------------------------------------------------------------------------

    /** Minimum safe year to use with methods in DateManager. */
    public static final int MIN_SAFE_YEAR = 1583;
    /** Maximum safe year to use with methods in DateManager. */
    public static final int MAX_SAFE_YEAR = 292_278_994;
    
    /** Date on which the game starts. Wednesday, January 20, 2027 12:00:00 PM GMT-05:00 */
    public static final ZonedDateTime startDate = ZonedDateTime.of(
        LocalDate.of(2027, 1, 20),
        LocalTime.of(12, 0),
        ZoneId.of("America/New_York")
    );
    /** Date on which the game ends. Saturday, January 20, 2029 12:00:00 PM GMT-05:00 */
    public static final ZonedDateTime endDate = ZonedDateTime.of(
        LocalDate.of(2029, 1, 20),
        LocalTime.of(12, 0),
        ZoneId.of("America/New_York")
    );
    
    /** The Epoch, 1970. */
    public static final int epochYear = 1970;
    /** Milliseconds since the year zero corresponding to the epoch. */
    public static final long epochMillis = epochYear * TimeManager.yearDuration;

    /** Duration in milliseconds of one second. */
    public static final long secondDuration = 1000L;
    /** Duration in milliseconds of one minute. */
    public static final long minuteDuration = secondDuration * 60;
    /** Duration in milliseconds of one hour. */
    public static final long hourDuration = minuteDuration * 60;
    /** Duration in milliseconds of one day. */
    public static final long dayDuration = hourDuration * 24;
    /** Duration in milliseconds of one week. */
    public static final long weekDuration = dayDuration * 7;
    // there is no single month duration because months vary in length
    /** Duration in milliseconds of one leap year. */
    public static final long leapYearDuration = 366 * dayDuration;
    /** Duration in milliseconds of one standard year. */
    public static final long yearDuration = 365 * dayDuration;
    /** Number of days in a year. TODO Think about leap years */
    public static final int daysInYear = 366;

    /** Lengths in days of each month between January 2027 and January 2029. Index by ordinal month value. */
    public static final int[] monthsDurationsDays = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
        31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };
    /** Duration in milliseconds of each month between January 2027 and January 2029. Index by ordinal month value. */
    public static final long[] monthsDurationsMillis = Arrays.stream(monthsDurationsDays).mapToLong(d -> d*dayDuration).toArray();
    /** Name of each day. Lookup from LANG_system_text. */
    public static final String[] dayNames = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    /** Three-letter abbrviation of each day. Lookup from LANG_system_text. */
    public static final String[] day3Abbreviations = {"mon_abbreviation", "tue_abbreviation", "wed_abbreviation", "thu_abbreviation", "fri_abbreviation", "sat_abbreviation", "sun_abbreviation"};
    /** Two-letter abbreviation of each day. Lookup from LANG_system_text. */
    public static final String[] day2Abbreviations = {"mo_abbreviation", "tu_abbreviation", "we_abbreviation", "th_abbreviation", "fr_abbreviation", "sa_abbreviation", "su_abbreviation"};
    /** One-letter initialization of each day. Lookup from LANG_system_text. */
    public static final String[] dayInitializations = {"mon_initial", "tue_initial", "wed_initial", "thu_initial", "fri_initial", "sat_initial", "sun_initial"};
    /** Name of each month. Lookup from LANG_system_text. */
    public static final String[] monthNames = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
    /** Abbreviation of each month. Lookup from LANG_system_text. */
    public static final String[] monthAbbreviations = {"jan_abbreviation", "feb_abbreviation", "mar_abbreviation", "apr_abbreviation", "may_abbreviation", "jun_abbreviation", "jul_abbreviation", "aug_abbreviation", "sep_abbreviation", "oct_abbreviation", "nov_abbreviation", "dec_abbreviation"};
    /** Number of each year between 2027 and 2029. */
    public static final String[] yearNumbers = {"2027", "2028", "2029"};

    // STATIC CLASS FUNCTIONS ---------------------------------------------------------------------

    /**
     * Calculates and returns the number of milliseconds since epoch for Jan 1 00:00:00 in a given year, adjusted to the EST timezone.
     * @param year The year to calculate (1900, 1970, 2000)
     * @return The number of milliseconds since epoch.
     */
    public static long yearToMillis(int year) {
        if (year < MIN_SAFE_YEAR || year > MAX_SAFE_YEAR) {
            Logger.log("DATE OUT OF BOUNDS", String.format("The year requested, %s, is out of the accurate bounds of [1583,292278994].", year), new Exception());
            return -1L;
        }
        long millis = ((long) year) * yearDuration - epochMillis;
        // calculate for leap years
        int numberLeapYears = 0;
        if (year > epochYear)
        for(int i = epochYear; i < year; i++) {
            if (isLeapYear(i)) numberLeapYears++;
        }
        else if (year < epochYear)
        for(int i = year; i <= epochYear; i++) {
            if (isLeapYear(i)) numberLeapYears--;
        }
        // no calculation required if year == epochYear
        millis += (numberLeapYears * dayDuration) + (TimeZone.getTimeZone("America/New_York").getRawOffset());
        return millis;
    }

    /** Calculates the number of seconds in the given number of milliseconds. */
    public static double timeToSeconds(long time) {
        return time * 1.0 / secondDuration;
    }
    /** Calculates the number of days in the given number of milliseconds. */
    public static double timeToDays(long time) {
        return time * 1.0 / dayDuration;
    }
    /** Calculates the number of weeks in the given number of milliseconds. */
    public static double timeToWeeks(long time) {
        return time * 1.0 / weekDuration;
    }
    /** Calculates the number of years in the given number of milliseconds. */
    public static double timeToYears(long time) {
        return time * 1.0 / yearDuration;
    }

    /** Determines whether a given year is a leap year. */
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0) {
            return year % 400 == 0;
        }
        return year % 4 == 0;
    }

    /**
     * Creates a date from a formatted string.
     * @param dateString The String to parse into a Date.
     * @return Parsed date, or {@code null} if unsuccessful.
     */
    public static LocalDate dateFromString(String dateString) {
        String[] dateParts = dateString.split("[-//]", 3);
        if (dateParts.length < 3) return null;
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        return LocalDate.of(year, month, day);
    }

    /**
     * Returns the date in the format MM/DD for the given day ordinal. Includes leap years (i = 59).
     * @param dayOrdinal Day ordinal to convert to a date.
     * @return Date in the format MM/DD.
     */
    public static String ordinalToDateFormat(int dayOrdinal) {
        if (dayOrdinal < 0 || dayOrdinal > daysInYear-1) {
            Logger.log("INVALID DAY ORDINAL", String.format("The day ordinal %d is out of bounds. Must be between 0 and %d.", dayOrdinal, daysInYear), new Exception());
            return null;
        }
        if (dayOrdinal == 59) return "02/29"; // Leap year
        if (dayOrdinal > 59) dayOrdinal--; // Leap year

        int day = 0, month = 0;
        int elapsed = 0;
        for(int i = 0; i < monthsDurationsDays.length; i++) {
            elapsed += monthsDurationsDays[i];
            if (elapsed > dayOrdinal) {
                month = i+1;
                break;
            }
        }
        // get the day part
        day = monthsDurationsDays[month-1] - (elapsed - dayOrdinal) + 1;

        if (month == 0 || day == 0) {
            Logger.log("DATE CALCULATION ERROR", String.format("The date calculation failed to produce a valid date for day ordinal %d.", dayOrdinal), new Exception());
            return null;
        }

        return String.format("%02d/%02d", month, day);
    }

    /**
     * Returns the day ordinal for the given date in the format MM/DD. Includes leap years (i = 59).
     * @param dateFormat Date in the format MM/DD to convert to a day ordinal.
     * @return Day ordinal for the given date.
     */
    public static int dateFormatToOrdinal(String dateFormat) {
        String[] parts = dateFormat.split("[-//]");
        if (parts.length != 2) {
            Logger.log("INVALID DATE FORMAT", String.format("The date format \"%s\" is invalid. Must be in the format MM/DD.", dateFormat), new Exception());
            return -1;
        }
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);

        if (month < 1 || month > 12 || day < 1 || day > 31) {
            Logger.log("INVALID DATE FORMAT", String.format("The date \"%s\" is invalid. Months must be between 1 and 12, and days must be between 1 and 31.", dateFormat), new Exception());
            return -1;
        }
        int result = 0;
        for(int i = 0; i < month-1; i++) {
            result += monthsDurationsDays[i];
        }
        result += day - 1;
        if (month == 2 && day == 29) return 59; // Leap year
        else if (result >= 59) result++; // Leap year

        return result;
    }
    
    /**
     * Calculates the amount of time in between two dates.
     * @param startDate The start date to calculate the time from.
     * @param endDate The end date to calculate the time unil.
     * @return The amount of time in milliseconds between the two dates.
     */
    public static long millisecondsBetween(ZonedDateTime startDate, ZonedDateTime endDate) {
        return Math.abs(endDate.toInstant().toEpochMilli() - startDate.toInstant().toEpochMilli());
    }

    /**
     * Calculates the amount of time in between the current game date and the given date.
     * @param date The date to calculate the time since.
     * @return The amount of time in milliseconds between the current game date and the given date.
     */
    public long millisecondsAgo(ZonedDateTime date) {
        return millisecondsBetween(currentGameDate, date);
    }

    public int yearsBetween(LocalDate startDate, LocalDate endDate) {
        return Math.abs(startDate.getYear() - endDate.getYear());
    }
    /**
     * Calculates the number of years between the current game date and the given date.
     * @param date A past date to use in the calculation.
     * @return The number of years (whole number) ago which the date represents.
     * @see #millisecondsAgo(Date)
     */
    public int yearsAgo(LocalDate date) {
        return yearsBetween(date, currentGameDate.toLocalDate());
    }


    /**
     * Determine a date from a year, month, and day of the month.
     * @param year Complete year (2020, 2028, etc) 
     * @param month 1-indexed month (Jan = 1, Feb = 2, ..., Dec = 12)
     * @param date Day of the month (1, 2, 3, ...)
     * @return LocalDate with the year, month, and day of the month.
     */
    public LocalDate determineDate(int year, int month, int date) { 
        return LocalDate.of(year, month, date);
    }
    /**
     * Determine a date from a month and date. The Year is inferred to be the current game year.
     * @param month 1-indexed month (Jan = 1, Feb = 2, ..., Dec = 12)
     * @param date Day of the month (1, 2, 3, ...)
     * @return LocalDate with the current game year, passed month, and passed day of the month.
     */
    public LocalDate determineDate(int month, int date) {
        return LocalDate.of(currentGameDate.getYear(), month, date);
    }
    /**
     * Get the date based on a year, month, day, and the order of the day in the month. I.E. "3rd Tuesday in April 2025"
     * @param year
     * @param month
     * @param day Day of the week (Monday = 1, Tuesday = 2, ..., Sunday = 7)
     * @param order
     * @return LocalDate
     */
    public LocalDate determineDate(int year, int month, int day, int order) {
        int count = 0;

        if (order == 0) {
            throw new IllegalArgumentException("Cannot get date with an order of zero.");
        }

        // Loop forwards or backwards depending on sign of order
        for (
            int i = order > 0 ? 1 : monthsDurationsDays[month-1];
            order > 0 ? i <= monthsDurationsDays[month-1] : i >= 1;
            i = i + (order > 0 ? 1 : -1)
        ) {
            if (LocalDate.of(year, month, i).getDayOfWeek().getValue() == day) {
                if (++count == Math.abs(order)) {
                    return LocalDate.of(year, month, i);
                }
            }
        }
        throw new IllegalArgumentException("There is no " + NumberOperations.toOrdinal(order) + " " + dayNames[day] + " of " + monthNames[month] + " in " + year);
    }

    public LocalDate determineDateRelative(LocalDate relativeTo, int day, int order) {
        if (order == 0) {
            throw new IllegalArgumentException("Cannot get date with an order of zero.");
        }
        int count = 0;
        for (
            LocalDate candidate = LocalDate.of(relativeTo.getYear(), relativeTo.getMonthValue(), relativeTo.getDayOfMonth() + (order > 0 ? 1 : -1));
            ;
            candidate = candidate.plusDays(order > 0 ? 1 : -1)
        ) {
            if (candidate.getDayOfWeek().getValue() == day) {
                if (++count == Math.abs(order)) {
                    return candidate;
                }
            }
        }
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    /** Current Date of Gameplay. */
    private ZonedDateTime currentGameDate;

    /** State of the Manager. */
    private ManagerState currentState;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    /** Create a new inactive DateManager. */
    public TimeManager() {
        currentState = ManagerState.INACTIVE;
        currentGameDate = ZonedDateTime.of(startDate.toLocalDate(), startDate.toLocalTime(), startDate.getZone());
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    /** Initialize and Activate this DateManager. */
    @Override
    public boolean init() {
        boolean successFlag = true;
        double startTime = Main.Engine().getProgramTime();
        Logger.log(String.format("%s starting at %f", this.getClass().getSimpleName(), startTime));
        if (currentGameDate == null || !currentGameDate.equals(startDate))
            currentGameDate = ZonedDateTime.of(startDate.toLocalDate(), startDate.toLocalTime(), startDate.getZone());
        currentState = successFlag ? ManagerState.ACTIVE : ManagerState.ERROR;
        double endTime = Main.Engine().getProgramTime(); 
        Logger.log(String.format("%s initialized %s at %f. Elapsed: %f", this.getClass().getSimpleName(), successFlag ? "successfully" : "unsuccessfully", endTime, endTime - startTime));
        return successFlag;
    }

    /** Get the current State of this DateManager. */
    @Override
    public ManagerState getState() {
        return currentState;
    }

    /** Deactivate and clean up the data of this DateManager. */
    @Override
    public boolean cleanup() {
        boolean successFlag = true;
        currentState = ManagerState.INACTIVE;
        currentGameDate = null;
        if (!successFlag) currentState = ManagerState.ERROR;
        return successFlag;
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    // Current Date : Date
    public ZonedDateTime getCurrentDate() {
        return currentGameDate;
    }
    /**
     * Get current game date and time in YY-MM-DD-HH-MM-SS format.
     */
    public String getFormattedCurrentDate() {
        StringBuilder dateString = new StringBuilder();

        dateString.append(getCurrentYear()).append("-").append(getCurrentMonth()).append("-").append(getCurrentDay()).append("-").append(getFormattedCurrentTime().replace(":","-"));
        return dateString.toString();
    }

    // Current Year : int
    public int getCurrentYear() {
        return currentGameDate.getYear();
    }

    // Current Month : int
    public int getCurrentMonth() {
        return currentGameDate.getMonthValue();
    }

    // Current Day : int
    public int getCurrentDay() {
        return currentGameDate.getDayOfMonth();
    }

    // Current Time
    /**
     * Get current game date day time in HH:MM:SS format.
     */
    public String getFormattedCurrentTime() {
        return String.format("%02d:%02d:%02d", 
            currentGameDate.getHour(),
            currentGameDate.getMinute(),
            currentGameDate.getSecond()
        );
    }

    // INCREMENT METHODS --------------------------------------------------------------------------

    /**
     * Increments the current game date by one second.
     */
    public boolean incrementSecond() {
        currentGameDate = currentGameDate.plusSeconds(1);

        return isPastEndDate();
    }
    /**
     * Increments the current game date by a quarter minute (15 secs).
     */
    public boolean incrementQuarterMinute() {
        currentGameDate = currentGameDate.plusSeconds(15);

        return isPastEndDate();
    }
    /**
     * Increments the current game date by half a minute (30 secs).
     */
    public boolean incrementHalfMinute() {
        currentGameDate = currentGameDate.plusSeconds(30);

        return isPastEndDate();
    }
    /**
     * Increments the current game date by one minute (60 secs).
     */
    public boolean incrementMinute() {
        currentGameDate = currentGameDate.plusMinutes(1);

        return isPastEndDate();
    }
    /**
     * Increments the current game date by a quarter hour (15 mins).
     */
    public boolean incrementQuarterHour() {
        currentGameDate = currentGameDate.plusMinutes(15);

        return isPastEndDate();
    }
    /**
     * Increments the current game date by half an hour (30 mins).
     */
    public boolean incrementHalfHour() {
        currentGameDate = currentGameDate.plusMinutes(30);

        return isPastEndDate();
    }
    /**
     * Increments the current game date by one hour (60 mins).
     */
    public boolean incrementHour() {
        currentGameDate = currentGameDate.plusHours(1);

        return isPastEndDate();
    }
    /**
     * Increments the current game date by a quarter day (6 hours).
     */
    public boolean incrementQuarterDay() {
        currentGameDate = currentGameDate.plusHours(6);

        return isPastEndDate();
    }
    /**
     * Increments the current game date by half a day (12 hours).
     */
    public boolean incrementHalfDay() {
        currentGameDate = currentGameDate.plusHours(12);

        return isPastEndDate();
    }
    /**
     * Increments the current game date by one day (24 hours).
     */
    public boolean incrementDay() {
        currentGameDate = currentGameDate.plusDays(1);

        return isPastEndDate();
    }

    public boolean isPastEndDate() {
        return currentGameDate.compareTo(endDate) > 0;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Manager fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }

    private static final Map<String, String> fieldsJsons = Map.of(
        "currentGameDate", "current_game_date"
    );

    @Override
    public JSONObject toJson() {
        try {
            List<JSONObject> fields = new ArrayList<>();
            for (String fieldName : fieldsJsons.keySet()) {
                Field field = getClass().getDeclaredField(fieldName);
                fields.add(new JSONObject(fieldName, field.get(this)));
            }
            return new JSONObject("time_manager", fields);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            currentState = ManagerState.ERROR;
            Logger.log("JSON SERIALIZATION ERROR", "Failed to serialize " + getClass().getSimpleName() + " to JSON.", e);
            return null;
        }
    }

    @Override
    public Manager fromJson(JSONObject json) {
        currentState = ManagerState.INACTIVE;
        for (String fieldName : fieldsJsons.keySet()) {
            String jsonKey = fieldsJsons.get(fieldName);
            Object value = json.get(jsonKey);
            if (value == null) continue;
            try {
                Field field = getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.isEnum()) {
                    // For enums, convert string to enum constant
                    @SuppressWarnings({ "unchecked", "rawtypes" })
                    Object enumValue = Enum.valueOf((Class<Enum>) type, value.toString());
                    field.set(this, enumValue);
                }
                else {
                    // For other types, set directly (may need conversion for complex types)
                    field.set(this, value);
                }
            }
            catch (Exception e) {
                currentState = ManagerState.ERROR;
                Logger.log("JSON DESERIALIZATION ERROR", "Failed to set field " + fieldName + " in LanguageManager from JSON.", e);
            }
        }
        return this;
    }
}
