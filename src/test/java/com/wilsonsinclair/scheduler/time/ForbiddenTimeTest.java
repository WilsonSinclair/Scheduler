package com.wilsonsinclair.scheduler.time;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ForbiddenTimeTest {

    @Test
    void getRepeatingDayOfWeekTest() {
        ForbiddenTime time = new ForbiddenTime(DayOfWeek.SUNDAY, true);
        assertTrue(time.getDayOfWeek().isPresent());
        assertEquals(time.getDayOfWeek().get(), DayOfWeek.SUNDAY);
        assertTrue(time.isRepeating());
    }

    @Test
    void getNonRepeatingDayOfWeekTest() {
        ForbiddenTime time = new ForbiddenTime(DayOfWeek.WEDNESDAY, false);
        assertTrue(time.getDayOfWeek().isPresent());
        assertEquals(time.getDayOfWeek().get(), DayOfWeek.WEDNESDAY);
        assertFalse(time.isRepeating());
    }

    @Test
    void getDateTest() {

    }

    @Test
    void getStartTime() {
    }

    @Test
    void getEndTime() {
    }

    @Test
    void isRepeating() {
    }
}