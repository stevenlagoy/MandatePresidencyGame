package com.stevenlagoy.presidency.core;

import static org.junit.jupiter.api.Assertions.*;
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
        if (ENGINE.TimeManager().getState() != ManagerState.ACTIVE) {
            if (!ENGINE.TimeManager().init()) {
                fail("Failed to initialize TimeManager, necessary for testing.");
            }
        }
    }

    @AfterEach
    public void resetTimeManager() {
        ENGINE.TimeManager().cleanup();
        ENGINE.TimeManager().init();
    }

    @Test
    public void testYearToMillisValid() {
        long millis = TimeManager.yearToMillis(1970);
        assertEquals(-18_000_000L, millis);
        millis = TimeManager.yearToMillis(2000);
        assertEquals(946_666_800_000L, millis);
        millis = TimeManager.yearToMillis(1776);
        assertEquals(-6_122_062_800_000L, millis);
        millis = TimeManager.yearToMillis(2026);
        assertEquals(1_767_207_600_000L, millis);
    }

    @Test
    public void testYearToMillisOutOfBounds() {
        assertEquals(-1L, TimeManager.yearToMillis(1000));
        assertEquals(-1L, TimeManager.yearToMillis(TimeManager.MAX_SAFE_YEAR + 1));
    }

    @Test
    public void testTimeConversions() {
        assertEquals(1.0, TimeManager.timeToSeconds(TimeManager.secondDuration), 0.0001);
        assertEquals(1.0, TimeManager.timeToDays(TimeManager.dayDuration), 0.0001);
        assertEquals(1.0, TimeManager.timeToWeeks(TimeManager.weekDuration), 0.0001);
        assertEquals(1.0, TimeManager.timeToYears(TimeManager.yearDuration), 0.0001);
    }

    @Test
    public void testIsLeapYear() {
        assertTrue(TimeManager.isLeapYear(2020));
        assertFalse(TimeManager.isLeapYear(2019));
        assertTrue(TimeManager.isLeapYear(2000));
        assertFalse(TimeManager.isLeapYear(1900));
    }

    @Test
    public void testDateFromStringValid() {
        LocalDate date = TimeManager.dateFromString("2027-01-20");
        assertEquals(LocalDate.of(2027, 1, 20), date);
    }

    @Test
    public void testDateFromStringInvalid() {
        assertNull(TimeManager.dateFromString("bad-format"));
        assertNull(TimeManager.dateFromString("2027-01"));
    }

    @Test
    public void testOrdinalToDateFormat() {
        Set<String> uniqueDates = new HashSet<>();
        for (int i = 0; i < 366; i++) {
            String dateFormat = TimeManager.ordinalToDateFormat(i);
            assertTrue(uniqueDates.add(dateFormat));
        }
        assertEquals(366, uniqueDates.size());
    }

    @Test
    public void testOrdinalToDateFormatLeapDay() {
        assertEquals("02/29", TimeManager.ordinalToDateFormat(59));
    }

    @Test
    public void testOrdinalToDateFormatFirstDay() {
        assertEquals("01/01", TimeManager.ordinalToDateFormat(0));
    }

    @Test
    public void testOrdinalToDateFormatInvalid() {
        assertEquals(null, TimeManager.ordinalToDateFormat(-1));
        assertEquals(null, TimeManager.ordinalToDateFormat(370));
    }

    @Test
    public void testDateFormatToOrdinalLeapDay() {
        assertEquals(59, TimeManager.dateFormatToOrdinal("02/29"));
    }

    @Test
    public void testDateFormatToOrdinalFirstDay() {
        assertEquals(0, TimeManager.dateFormatToOrdinal("01/01"));
    }

    @Test
    public void testDateFormatToOrdinalInvalid() {
        assertEquals(-1, TimeManager.dateFormatToOrdinal("13/01"));
        assertEquals(-1, TimeManager.dateFormatToOrdinal("01/32"));
        assertEquals(-1, TimeManager.dateFormatToOrdinal("bad-format"));
        assertEquals(-1, TimeManager.dateFormatToOrdinal("12"));
    }

    // --- Instance Methods ---

    @Test
    public void testIncrementSecond() {
        ZonedDateTime before = ENGINE.TimeManager().getCurrentDate();
        ENGINE.TimeManager().incrementSecond();
        ZonedDateTime after = ENGINE.TimeManager().getCurrentDate();
        assertEquals(before.plusSeconds(1), after);
    }

    @Test
    public void testIncrementHalfMinute() {
        ZonedDateTime before = ENGINE.TimeManager().getCurrentDate();
        ENGINE.TimeManager().incrementHalfMinute();
        ZonedDateTime after = ENGINE.TimeManager().getCurrentDate();
        assertEquals(before.plusSeconds(30), after);
    }

    @Test
    public void testIncrementMinute() {
        ZonedDateTime before = ENGINE.TimeManager().getCurrentDate();
        ENGINE.TimeManager().incrementMinute();
        ZonedDateTime after = ENGINE.TimeManager().getCurrentDate();
        assertEquals(before.plusMinutes(1), after);
    }

    @Test
    public void testIncrementQuarterHour() {
        ZonedDateTime before = ENGINE.TimeManager().getCurrentDate();
        ENGINE.TimeManager().incrementQuarterHour();
        ZonedDateTime after = ENGINE.TimeManager().getCurrentDate();
        assertEquals(before.plusMinutes(15), after);
    }

    @Test
    public void testIncrementHalfHour() {
        ZonedDateTime before = ENGINE.TimeManager().getCurrentDate();
        ENGINE.TimeManager().incrementHalfHour();
        ZonedDateTime after = ENGINE.TimeManager().getCurrentDate();
        assertEquals(before.plusMinutes(30), after);
    }

    @Test
    public void testIncrementHour() {
        ZonedDateTime before = ENGINE.TimeManager().getCurrentDate();
        ENGINE.TimeManager().incrementHour();
        ZonedDateTime after = ENGINE.TimeManager().getCurrentDate();
        assertEquals(before.plusHours(1), after);
    }

    @Test
    public void testIncrementQuarterDay() {
        ZonedDateTime before = ENGINE.TimeManager().getCurrentDate();
        ENGINE.TimeManager().incrementQuarterDay();
        ZonedDateTime after = ENGINE.TimeManager().getCurrentDate();
        assertEquals(before.plusHours(6), after);
    }

    @Test
    public void testIncrementHalfDay() {
        ZonedDateTime before = ENGINE.TimeManager().getCurrentDate();
        ENGINE.TimeManager().incrementHalfDay();
        ZonedDateTime after = ENGINE.TimeManager().getCurrentDate();
        assertEquals(before.plusHours(12), after);
    }

    @Test
    public void testIncrementDay() {
        ZonedDateTime before = ENGINE.TimeManager().getCurrentDate();
        ENGINE.TimeManager().incrementDay();
        ZonedDateTime after = ENGINE.TimeManager().getCurrentDate();
        assertEquals(before.plusDays(1), after);
    }

    @Test
    public void testIsPastEndDate() {
        while (!ENGINE.TimeManager().isPastEndDate()) {
            ENGINE.TimeManager().incrementDay();
        }
        assertTrue(ENGINE.TimeManager().isPastEndDate());
    }

    @Test
    public void testGetters() {
        ENGINE.TimeManager().init(); // Init to make sure current date is start date
        assertEquals(TimeManager.startDate.getYear(), ENGINE.TimeManager().getCurrentYear());
        assertEquals(TimeManager.startDate.getMonthValue(), ENGINE.TimeManager().getCurrentMonth());
        assertEquals(TimeManager.startDate.getDayOfMonth(), ENGINE.TimeManager().getCurrentDay());
        assertNotNull(ENGINE.TimeManager().getFormattedCurrentDate());
        assertNotNull(ENGINE.TimeManager().getFormattedCurrentTime());
    }

    @Test
    public void testDetermineDate() {
        LocalDate date1 = ENGINE.TimeManager().determineDate(2028, 2, 29);
        assertEquals(LocalDate.of(2028, 2, 29), date1);
        LocalDate date2 = ENGINE.TimeManager().determineDate(5, 14);
        assertEquals(LocalDate.of(ENGINE.TimeManager().getCurrentYear(), 5, 14), date2);
        LocalDate date3 = ENGINE.TimeManager().determineDate(2028, 4, 2, 3);
        assertEquals(LocalDate.of(2028, 4, 18), date3);
        LocalDate date4 = ENGINE.TimeManager().determineDate(2026, 7, 5, -2);
        assertEquals(LocalDate.of(2026, 7, 24), date4);
    }

    @Test
    public void testDetermineDateInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            ENGINE.TimeManager().determineDate(2027, 1, 1, 6);
        });
    }

    @Test
    public void testDetermineDateRelative() {
        LocalDate date1 = ENGINE.TimeManager().determineDateRelative(LocalDate.of(2026, 1, 15), 1, 1);
        assertEquals(LocalDate.of(2026, 1, 19), date1);
    }

    @Test
    public void testDetermineDateRelativeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            ENGINE.TimeManager().determineDateRelative(null, 0, 0);
        });
    }

    @Test
    public void testMillisecondsBetween() {
        ZonedDateTime d1 = TimeManager.startDate;
        ZonedDateTime d2 = TimeManager.endDate;
        long ms = TimeManager.millisecondsBetween(d1, d2);
        assertTrue(ms > 0);
    }

    @Test
    public void testMillisecondsAgo() {
        ZonedDateTime d = TimeManager.startDate.minusDays(1);
        long ms = ENGINE.TimeManager().millisecondsAgo(d);
        assertTrue(ms > 0);
    }

    @Test
    public void testYearsBetween() {
        LocalDate d1 = LocalDate.of(2020, 1, 1);
        LocalDate d2 = LocalDate.of(2025, 1, 1);
        assertEquals(5, ENGINE.TimeManager().yearsBetween(d1, d2));
    }

    @Test
    public void testYearsAgo() {
        LocalDate past = LocalDate.of(ENGINE.TimeManager().getCurrentYear() - 3, 1, 1);
        assertEquals(3, ENGINE.TimeManager().yearsAgo(past));
    }

    @Test
    public void testDetermineDateInvalidOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            ENGINE.TimeManager().determineDate(2027, 4, 2, 0);
        });
    }

    @Test
    public void testDetermineDateRelativeInvalidOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            ENGINE.TimeManager().determineDateRelative(LocalDate.of(2027, 1, 1), 2, 0);
        });
    }

    @Test
    public void testTimeManagerCleanup() {
        ENGINE.TimeManager().cleanup();
        assertEquals(ManagerState.INACTIVE, ENGINE.TimeManager().getState());
    }

    @Test
    public void testTimeManagerToJson() {
        JSONObject json = ENGINE.TimeManager().toJson();
        String expectedKey = "time_manager";
        assertEquals(expectedKey, json.getKey());
        ENGINE.TimeManager().fromJson(json);
        assertEquals(ManagerState.ACTIVE, ENGINE.TimeManager().getState());
    }
}