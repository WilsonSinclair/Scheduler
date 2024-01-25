package com.wilsonsinclair.scheduler.time;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

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
    void dateWithStartAndEndTimeTest() {
        ForbiddenTime time = new ForbiddenTime(LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(9, 0));
        assertTrue(time.getDate().isPresent());
        assertTrue(time.getStartTime().isPresent());
        assertTrue(time.getEndTime().isPresent());

        assertTrue(time.getDate().get().isAfter(LocalDate.ofYearDay(2023, 1)));
    }

    @Test
    void repeatingDayOfWeekWithStartAndEndTimeTest() {
        ForbiddenTime time = new ForbiddenTime(DayOfWeek.SUNDAY, LocalTime.of(8, 0), LocalTime.of(10, 0), true);
        assertTrue(time.getDayOfWeek().isPresent());
        assertTrue(time.getStartTime().isPresent());
        assertTrue(time.getEndTime().isPresent());

        assertTrue(time.isRepeating());
        assertEquals(time.getDayOfWeek().get(), DayOfWeek.SUNDAY);
        assertTrue(time.getStartTime().get().isAfter(LocalTime.of(7, 0)));
        assertTrue(time.getStartTime().get().isBefore(LocalTime.of(11, 0)));
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
        ForbiddenTime time = new ForbiddenTime(LocalDate.of(2024, 1, 13));
        assertTrue(time.getDate().isPresent());
        assertTrue(time.getDayOfWeek().isPresent());

        assertEquals(time.getDayOfWeek().get(), DayOfWeek.SATURDAY);
        assertEquals(time.getDate().get().getMonth(), Month.JANUARY);
    }

    @Test
    void getStartTime() {
        ForbiddenTime time = new ForbiddenTime(LocalTime.of(7, 0), LocalTime.of(8, 0));
        assertTrue(time.getStartTime().isPresent());
        assertTrue(time.getStartTime().get().isBefore(LocalTime.of(9, 0)));
    }

    @Test
    void getEndTime() {
        ForbiddenTime time = new ForbiddenTime(LocalTime.of(7, 0), LocalTime.of(8, 0));
        assertTrue(time.getEndTime().isPresent());
        assertTrue(time.getEndTime().get().isBefore(LocalTime.of(13, 0)));
    }

    @Test
    void startsBeforeTest() {
        LocalDate birthday = LocalDate.of(2024, 12, 18);
        ForbiddenTime forbiddenTime = new ForbiddenTime(birthday, LocalTime.of(8, 0), LocalTime.of(12, 0));
        LocalTime shiftStart = LocalTime.of(9, 0);
        assertTrue(forbiddenTime.startsBeforeOrAt(shiftStart));
    }

    @Test
    void startsAfterTest() {
        LocalDate birthday = LocalDate.of(2024, 12, 18);
        ForbiddenTime time = new ForbiddenTime(birthday, LocalTime.of(8, 0), LocalTime.of(12, 0));
        LocalTime shiftStart = LocalTime.of(9, 0);
        assertFalse(time.startsAfterOrAt(shiftStart));
    }

    @Test
    void endsBeforeTest() {
        LocalDate birthday = LocalDate.of(2024, 12, 18);
        ForbiddenTime forbiddenTime = new ForbiddenTime(birthday, LocalTime.of(8, 0), LocalTime.of(10, 0));
        LocalTime shiftEnd = LocalTime.of(11, 0);
        assertTrue(forbiddenTime.endsBeforeOrAt(shiftEnd));
    }

    @Test
    void endsAfterTest() {
        LocalDate birthday = LocalDate.of(2024, 12, 18);
        ForbiddenTime forbiddenTime = new ForbiddenTime(birthday, LocalTime.of(8, 0), LocalTime.of(12, 0));
        LocalTime shiftEnd = LocalTime.of(11, 0);
        assertTrue(forbiddenTime.endsAfterOrAt(shiftEnd));
    }

    @Test
    void doesIntersectTest_1() {
        LocalDate shiftDate = LocalDate.of(2024, 1, 24);
        ForbiddenTime forbiddenTime = new ForbiddenTime(shiftDate, LocalTime.of(8, 0), LocalTime.of(12, 0));
        LocalTime shiftStart = LocalTime.of(10, 0);
        LocalTime shiftEnd = LocalTime.of(13, 0);
        assertTrue(forbiddenTime.intersects(shiftStart, shiftEnd));
    }

    @Test
    void doesIntersectTest_2() {
        LocalDate shiftDate = LocalDate.of(2024, 1, 24);
        ForbiddenTime forbiddenTime = new ForbiddenTime(shiftDate, LocalTime.of(8, 0), LocalTime.of(12, 0));
        LocalTime shiftStart = LocalTime.of(7, 0);
        LocalTime shiftEnd = LocalTime.of(10, 0);
        assertTrue(forbiddenTime.intersects(shiftStart, shiftEnd));
    }

    @Test
    void doesIntersectTest_3() {
        LocalDate shiftDate = LocalDate.of(2024, 1, 24);
        ForbiddenTime forbiddenTime = new ForbiddenTime(shiftDate, LocalTime.of(8, 0), LocalTime.of(12, 0));
        LocalTime shiftStart = LocalTime.of(9, 0);
        LocalTime shiftEnd = LocalTime.of(11, 0);
        assertTrue(forbiddenTime.intersects(shiftStart, shiftEnd));
    }

    @Test
    void isRepeating() {

    }
}