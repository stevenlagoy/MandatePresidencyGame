package com.stevenlagoy.presidency.core;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.util.NumberUtils;
import com.stevenlagoy.presidency.util.TimeUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * <h1>TIME MANAGER</h1>
 * {@code ~/core/TimeManager.java}
 * <p>
 *     <b>Author:  </b> Steven LaGoy                <br>
 *     <b>Created: </b> 10 December 2024 at 8:21 AM <br>
 *     <b>Modified:</b> 02 June 2026                <br>
 * </p>
 *
 * TimeManager is responsible for tracking the current game date and providing scheduling details.
 *
 * @author Steven LaGoy
 */
public class TimeManager extends Manager {

    // Static Variables

    /**
     * Date on which the game starts. Wednesday, January 20, 2027 12:00:00 PM
     * GMT-05:00
     */
    public static final ZonedDateTime startDate = ZonedDateTime.of(
            LocalDate.of(2027, 1, 20),
            LocalTime.of(12, 0),
            ZoneId.of("America/New_York")
    );
    /**
     * Date on which the game ends. Saturday, January 20, 2029 12:00:00 PM GMT-05:00
     */
    public static final ZonedDateTime endDate = ZonedDateTime.of(
            LocalDate.of(2029, 1, 20),
            LocalTime.of(12, 0),
            ZoneId.of("America/New_York")
    );

    // Instance Fields

    /** Current Date of Gameplay. */
    private @NotNull ZonedDateTime currentGameDate;

    // Constructor

    /** Create a new inactive DateManager. */
    public TimeManager(@NotNull Engine engine, @NotNull Manager superManager) {
        super(engine, superManager);
        currentGameDate = ZonedDateTime.of(startDate.toLocalDate(), startDate.toLocalTime(), startDate.getZone()); // Copy startDate
    }

    // Manager Methods

    @Override
    @Contract(pure = true)
    public @NotNull List<Manager> getSubManagers() {
        return List.of();
    }

    /** Initialize and Activate this DateManager. */
    @Override
    public void doInit() {
        if (!currentGameDate.equals(startDate)) {
            currentGameDate = ZonedDateTime.of(startDate.toLocalDate(), startDate.toLocalTime(), startDate.getZone());
        }
    }

    /** Deactivate and clean up the data of this DateManager. */
    @Override
    public void doCleanup() {
        currentGameDate = ZonedDateTime.of(startDate.toLocalDate(), startDate.toLocalTime(), startDate.getZone());
    }

    // Serialization Methods

    @Override
    protected @NotNull JSONObject doToJson() {
        return new JSONObject(getClass().getSimpleName(), List.of(
            new JSONObject("currentGameDate", currentGameDate)
        ));
    }

    @Override
    protected void doFromJson(@NotNull JSONObject json) {
        try {
            currentGameDate = TimeUtils.zonedDateTimeFromString(json.requireString("currentGameDate"));
        } catch (IllegalArgumentException | ClassCastException e) {
            onDegraded(e);
            currentGameDate = ZonedDateTime.of(startDate.toLocalDate(), startDate.toLocalTime(), startDate.getZone());
        }
    }

    // Getters and Setters

    // Current Date : Date
    @Contract(pure = true)
    public @NotNull ZonedDateTime getCurrentDate() {
        requireOperational();
        return currentGameDate;
    }

    /**
     * Get current game date and time in YY-MM-DD-HH-MM-SS format.
     */
    @Contract(pure = true)
    public @NotNull String getFormattedCurrentDate() {
        requireOperational();
        return String.format(
            "%s-%s-%s-%s",
            getCurrentYear(), getCurrentMonth(), getCurrentDay(),
            getFormattedCurrentTime().replace(":", "-")
        );
    }

    // Current Year : int
    @Contract(pure = true)
    public int getCurrentYear() {
        requireOperational();
        return currentGameDate.getYear();
    }

    // Current Month : int
    @Contract(pure = true)
    public int getCurrentMonth() {
        requireOperational();
        return currentGameDate.getMonthValue();
    }

    // Current Day : int
    @Contract(pure = true)
    public int getCurrentDay() {
        requireOperational();
        return currentGameDate.getDayOfMonth();
    }

    // Current Time
    /**
     * Get current game date day time in HH:MM:SS format.
     */
    @Contract(pure = true)
    public @NotNull String getFormattedCurrentTime() {
        requireOperational();
        return String.format(
            "%02d:%02d:%02d",
            currentGameDate.getHour(),
            currentGameDate.getMinute(),
            currentGameDate.getSecond()
        );
    }

    // Tick Methods

    /**
     * Increments the current game date by one second.
     */
    public void incrementSecond() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusSeconds(1);
    }

    /**
     * Increments the current game date by a quarter minute (15 secs).
     */
    public void incrementQuarterMinute() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusSeconds(15);
    }

    /**
     * Increments the current game date by half a minute (30 secs).
     */
    public void incrementHalfMinute() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusSeconds(30);
    }

    /**
     * Increments the current game date by one minute (60 secs).
     */
    public void incrementMinute() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusMinutes(1);
    }

    /**
     * Increments the current game date by a quarter-hour (15 mins).
     */
    public void incrementQuarterHour() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusMinutes(15);
    }

    /**
     * Increments the current game date by half an hour (30 mins).
     */
    public void incrementHalfHour() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusMinutes(30);
    }

    /**
     * Increments the current game date by one hour (60 mins).
     */
    public void incrementHour() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusHours(1);
    }

    /**
     * Increments the current game date by a quarter day (6 hours).
     */
    public void incrementQuarterDay() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusHours(6);
    }

    /**
     * Increments the current game date by half a day (12 hours).
     */
    public void incrementHalfDay() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusHours(12);
    }

    /**
     * Increments the current game date by one day (24 hours).
     */
    public void incrementDay() {
        requireState(ManagerState.ACTIVE);
        currentGameDate = currentGameDate.plusDays(1);
    }

    public boolean isPastEndDate() {
        requireOperational();
        return currentGameDate.compareTo(endDate) > 0;
    }

    // Interval methods

    /**
     * Calculates the amount of time in between the current game date and the given
     * date.
     *
     * @param date The date to calculate the time since.
     * @return The amount of time in milliseconds between the current game date and
     *         the given date.
     */
    public long millisecondsAgo(@NotNull ZonedDateTime date) {
        requireOperational();
        return TimeUtils.millisecondsBetween(currentGameDate, date);
    }

    /**
     * Calculates the number of years between the current game date and the given
     * date.
     *
     * @param date A past date to use in the calculation.
     * @return The number of years (whole number) ago which the date represents.
     */
    public int yearsAgo(@NotNull LocalDate date) {
        requireOperational();
        return TimeUtils.yearsBetween(date, currentGameDate.toLocalDate());
    }

    public int daysAgo(@NotNull LocalDate date) {
        requireOperational();
        return TimeUtils.daysBetween(date, currentGameDate.toLocalDate());
    }

    /**
     * Determines the date it was yearsAgo years before the current game date.
     * @param yearsAgo Number of years to step backwards from the current game date.
     * @return LocalDate it was the given number of years ago.
     */
    public @NotNull LocalDate dateYearsAgo(long yearsAgo) {
        requireOperational();
        return currentGameDate.toLocalDate().minusYears(yearsAgo);
    }

    /**
     * Determine a date from a month and date. The Year is inferred to be the
     * current game year.
     *
     * @param month 1-indexed month (Jan = 1, Feb = 2, ..., Dec = 12)
     * @param date  Day of the month (1, 2, 3, ...)
     * @return LocalDate with the current game year, passed month, and passed day of
     *         the month.
     */
    public @NotNull LocalDate determineDate(int month, int date) {
        requireOperational();
        return LocalDate.of(currentGameDate.getYear(), month, date);
    }

    /**
     * Get the date based on a year, month, day, and the order of the day in the
     * month. I.E. "3rd Tuesday in April 2025"
     *
     * @param year Year
     * @param month Month
     * @param day   Day of the week (Monday = 1, Tuesday = 2, ..., Sunday = 7)
     * @param order Order of the day in the month
     * @return LocalDate
     */
    public @NotNull LocalDate determineDate(int year, int month, int day, int order) {
        requireOperational();

        int count = 0;
        if (order == 0) {
            throw new IllegalArgumentException("Cannot get date with an order of zero.");
        }

        // Loop forwards or backwards depending on sign of order
        for (int i = order > 0 ? 1 : TimeUtils.getMonthsDurationsDays()[month - 1]; order > 0 ? i <= TimeUtils.getMonthsDurationsDays()[month - 1]
            : i >= 1; i = i + (order > 0 ? 1 : -1)) {
            if (LocalDate.of(year, month, i).getDayOfWeek().getValue() == day) {
                if (++count == Math.abs(order)) {
                    return LocalDate.of(year, month, i);
                }
            }
        }
        throw new IllegalArgumentException("There is no " + NumberUtils.toOrdinal(order) + " " + TimeUtils.getDayNames()[day]
            + " of " + TimeUtils.getMonthNames()[month] + " in " + year);
    }

    public @NotNull LocalDate determineDateRelative(@NotNull LocalDate relativeTo, int day, int order) {
        requireOperational();

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
}
