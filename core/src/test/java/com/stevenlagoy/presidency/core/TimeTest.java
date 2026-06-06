package com.stevenlagoy.presidency.core;

import static org.junit.jupiter.api.Assertions.*;

import com.stevenlagoy.presidency.util.TimeUtils;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.stevenlagoy.jsonic.JSONObject;
import com.stevenlagoy.presidency.core.Manager.ManagerState;

public final class TimeTest {

    public static final Engine ENGINE = new Engine();

    @BeforeEach
    public void initTimeManager() {
        if (ENGINE.TIME_MANAGER.getState() != ManagerState.ACTIVE) {
            ENGINE.TIME_MANAGER.init();
            if (ENGINE.TIME_MANAGER.getState() != ManagerState.ACTIVE) {
                fail("Failed to initialize TimeManager, necessary for testing.");
            }
        }
    }

    @AfterEach
    public void resetTimeManager() {
        ENGINE.TIME_MANAGER.cleanup();
        ENGINE.TIME_MANAGER.init();
    }

    @Test
    public void testYearToMillisValid() {
        long millis = TimeUtils.yearToMillis(1970);
        assertEquals(-18_000_000L, millis);
        millis = TimeUtils.yearToMillis(2000);
        assertEquals(946_666_800_000L, millis);
        millis = TimeUtils.yearToMillis(1776);
        assertEquals(-6_122_062_800_000L, millis);
        millis = TimeUtils.yearToMillis(2026);
        assertEquals(1_767_207_600_000L, millis);
    }

    @Test
    public void testYearToMillisOutOfBounds() {
        assertEquals(-1L, TimeUtils.yearToMillis(1000));
        assertEquals(-1L, TimeUtils.yearToMillis(TimeUtils.MAX_SAFE_YEAR + 1));
    }

    @Test
    public void testTimeConversions() {
        assertEquals(1.0, TimeUtils.timeToSeconds(TimeUtils.secondDuration), 0.0001);
        assertEquals(1.0, TimeUtils.timeToDays(TimeUtils.dayDuration), 0.0001);
        assertEquals(1.0, TimeUtils.timeToWeeks(TimeUtils.weekDuration), 0.0001);
        assertEquals(1.0, TimeUtils.timeToYears(TimeUtils.yearDuration), 0.0001);
    }

    @Test
    public void testIsLeapYear() {
        assertTrue(TimeUtils.isLeapYear(2020));
        assertFalse(TimeUtils.isLeapYear(2019));
        assertTrue(TimeUtils.isLeapYear(2000));
        assertFalse(TimeUtils.isLeapYear(1900));
    }

    @Test
    public void testDateFromStringValid() {
        LocalDate date = TimeUtils.dateFromString("2027-01-20");
        assertEquals(LocalDate.of(2027, 1, 20), date);
    }

    @Test
    public void testDateFromStringInvalid() {
        assertNull(TimeUtils.dateFromString("bad-format"));
        assertNull(TimeUtils.dateFromString("2027-01"));
    }

    @Test
    public void testOrdinalToDateFormat() {
        Set<String> uniqueDates = new HashSet<>();
        for (int i = 0; i < 366; i++) {
            String dateFormat = TimeUtils.ordinalToDateFormat(i);
            assertTrue(uniqueDates.add(dateFormat));
        }
        assertEquals(366, uniqueDates.size());
    }

    @Test
    public void testOrdinalToDateFormatLeapDay() {
        assertEquals("02/29", TimeUtils.ordinalToDateFormat(59));
    }

    @Test
    public void testOrdinalToDateFormatFirstDay() {
        assertEquals("01/01", TimeUtils.ordinalToDateFormat(0));
    }

    @Test
    public void testOrdinalToDateFormatInvalid() {
        assertNull(TimeUtils.ordinalToDateFormat(-1));
        assertNull(TimeUtils.ordinalToDateFormat(370));
    }

    @Test
    public void testDateFormatToOrdinalLeapDay() {
        assertEquals(59, TimeUtils.dateFormatToOrdinal("02/29"));
    }

    @Test
    public void testDateFormatToOrdinalFirstDay() {
        assertEquals(0, TimeUtils.dateFormatToOrdinal("01/01"));
    }

    @Test
    public void testDateFormatToOrdinalInvalid() {
        assertEquals(-1, TimeUtils.dateFormatToOrdinal("13/01"));
        assertEquals(-1, TimeUtils.dateFormatToOrdinal("01/32"));
        assertEquals(-1, TimeUtils.dateFormatToOrdinal("bad-format"));
        assertEquals(-1, TimeUtils.dateFormatToOrdinal("12"));
    }

    // --- Instance Methods ---

    @Test
    public void testIncrementSecond() {
        ZonedDateTime before = ENGINE.TIME_MANAGER.getCurrentDate();
        ENGINE.TIME_MANAGER.incrementSecond();
        ZonedDateTime after = ENGINE.TIME_MANAGER.getCurrentDate();
        assertEquals(before.plusSeconds(1), after);
    }

    @Test
    public void testIncrementHalfMinute() {
        ZonedDateTime before = ENGINE.TIME_MANAGER.getCurrentDate();
        ENGINE.TIME_MANAGER.incrementHalfMinute();
        ZonedDateTime after = ENGINE.TIME_MANAGER.getCurrentDate();
        assertEquals(before.plusSeconds(30), after);
    }

    @Test
    public void testIncrementMinute() {
        ZonedDateTime before = ENGINE.TIME_MANAGER.getCurrentDate();
        ENGINE.TIME_MANAGER.incrementMinute();
        ZonedDateTime after = ENGINE.TIME_MANAGER.getCurrentDate();
        assertEquals(before.plusMinutes(1), after);
    }

    @Test
    public void testIncrementQuarterHour() {
        ZonedDateTime before = ENGINE.TIME_MANAGER.getCurrentDate();
        ENGINE.TIME_MANAGER.incrementQuarterHour();
        ZonedDateTime after = ENGINE.TIME_MANAGER.getCurrentDate();
        assertEquals(before.plusMinutes(15), after);
    }

    @Test
    public void testIncrementHalfHour() {
        ZonedDateTime before = ENGINE.TIME_MANAGER.getCurrentDate();
        ENGINE.TIME_MANAGER.incrementHalfHour();
        ZonedDateTime after = ENGINE.TIME_MANAGER.getCurrentDate();
        assertEquals(before.plusMinutes(30), after);
    }

    @Test
    public void testIncrementHour() {
        ZonedDateTime before = ENGINE.TIME_MANAGER.getCurrentDate();
        ENGINE.TIME_MANAGER.incrementHour();
        ZonedDateTime after = ENGINE.TIME_MANAGER.getCurrentDate();
        assertEquals(before.plusHours(1), after);
    }

    @Test
    public void testIncrementQuarterDay() {
        ZonedDateTime before = ENGINE.TIME_MANAGER.getCurrentDate();
        ENGINE.TIME_MANAGER.incrementQuarterDay();
        ZonedDateTime after = ENGINE.TIME_MANAGER.getCurrentDate();
        assertEquals(before.plusHours(6), after);
    }

    @Test
    public void testIncrementHalfDay() {
        ZonedDateTime before = ENGINE.TIME_MANAGER.getCurrentDate();
        ENGINE.TIME_MANAGER.incrementHalfDay();
        ZonedDateTime after = ENGINE.TIME_MANAGER.getCurrentDate();
        assertEquals(before.plusHours(12), after);
    }

    @Test
    public void testIncrementDay() {
        ZonedDateTime before = ENGINE.TIME_MANAGER.getCurrentDate();
        ENGINE.TIME_MANAGER.incrementDay();
        ZonedDateTime after = ENGINE.TIME_MANAGER.getCurrentDate();
        assertEquals(before.plusDays(1), after);
    }

    @Test
    public void testIsPastEndDate() {
        while (!ENGINE.TIME_MANAGER.isPastEndDate()) {
            ENGINE.TIME_MANAGER.incrementDay();
        }
        assertTrue(ENGINE.TIME_MANAGER.isPastEndDate());
    }

    @Test
    public void testGetters() {
        ENGINE.TIME_MANAGER.init(); // Init to make sure current date is start date
        assertEquals(TimeManager.startDate.getYear(), ENGINE.TIME_MANAGER.getCurrentYear());
        assertEquals(TimeManager.startDate.getMonthValue(), ENGINE.TIME_MANAGER.getCurrentMonth());
        assertEquals(TimeManager.startDate.getDayOfMonth(), ENGINE.TIME_MANAGER.getCurrentDay());
        assertNotNull(ENGINE.TIME_MANAGER.getFormattedCurrentDate());
        assertNotNull(ENGINE.TIME_MANAGER.getFormattedCurrentTime());
    }

    @Test
    public void testDetermineDate() {
        LocalDate date2 = ENGINE.TIME_MANAGER.determineDate(5, 14);
        assertEquals(LocalDate.of(ENGINE.TIME_MANAGER.getCurrentYear(), 5, 14), date2);
        LocalDate date3 = ENGINE.TIME_MANAGER.determineDate(2028, 4, 2, 3);
        assertEquals(LocalDate.of(2028, 4, 18), date3);
        LocalDate date4 = ENGINE.TIME_MANAGER.determineDate(2026, 7, 5, -2);
        assertEquals(LocalDate.of(2026, 7, 24), date4);
    }

    @Test
    public void testDetermineDateInvalid() {
        assertThrows(IllegalArgumentException.class, () -> ENGINE.TIME_MANAGER.determineDate(2027, 1, 1, 6));
    }

    @Test
    public void testDetermineDateRelative() {
        LocalDate date1 = ENGINE.TIME_MANAGER.determineDateRelative(LocalDate.of(2026, 1, 15), 1, 1);
        assertEquals(LocalDate.of(2026, 1, 19), date1);
    }

    @Test
    @SuppressWarnings("all")
    public void testDetermineDateRelativeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> ENGINE.TIME_MANAGER.determineDateRelative(null, 0, 0));
    }

    @Test
    public void testMillisecondsBetween() {
        ZonedDateTime d1 = TimeManager.startDate;
        ZonedDateTime d2 = TimeManager.endDate;
        long ms = TimeUtils.millisecondsBetween(d1, d2);
        assertTrue(ms > 0);
    }

    @Test
    public void testMillisecondsAgo() {
        ZonedDateTime d = TimeManager.startDate.minusDays(1);
        long ms = ENGINE.TIME_MANAGER.millisecondsAgo(d);
        assertTrue(ms > 0);
    }

    @Test
    public void testYearsBetween() {
        LocalDate d1 = LocalDate.of(2020, 1, 1);
        LocalDate d2 = LocalDate.of(2025, 1, 1);
        assertEquals(5, TimeUtils.yearsBetween(d1, d2));
    }

    @Test
    public void testYearsAgo() {
        LocalDate past = LocalDate.of(ENGINE.TIME_MANAGER.getCurrentYear() - 3, 1, 1);
        assertEquals(3, ENGINE.TIME_MANAGER.yearsAgo(past));
    }

    @Test
    public void testDetermineDateInvalidOrder() {
        assertThrows(IllegalArgumentException.class, () -> ENGINE.TIME_MANAGER.determineDate(2027, 4, 2, 0));
    }

    @Test
    public void testDetermineDateRelativeInvalidOrder() {
        assertThrows(IllegalArgumentException.class, () -> ENGINE.TIME_MANAGER.determineDateRelative(LocalDate.of(2027, 1, 1), 2, 0));
    }

    @Test
    public void testTimeManagerCleanup() {
        ENGINE.TIME_MANAGER.cleanup();
        assertEquals(ManagerState.INACTIVE, ENGINE.TIME_MANAGER.getState());
    }

    @Test
    public void testTimeManagerToJson() {
        JSONObject json = ENGINE.TIME_MANAGER.toJson();
        String expectedKey = "time_manager";
        assertEquals(expectedKey, json.getKey());
        ENGINE.TIME_MANAGER.fromJson(json);
        assertEquals(ManagerState.ACTIVE, ENGINE.TIME_MANAGER.getState());
    }
}
