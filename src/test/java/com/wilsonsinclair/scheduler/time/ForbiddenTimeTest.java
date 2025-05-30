package com.wilsonsinclair.scheduler.time;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class ForbiddenTimeTest {

    @Test
    void getRepeatingDayOfWeekTest() {
        ForbiddenTime time = new ForbiddenTime(DayOfWeek.SUNDAY, true);
        assertTrue(time.getDayOfWeek().isPresent());
        assertEquals(DayOfWeek.SUNDAY, time.getDayOfWeek().get());
        assertTrue(time.isRepeating());
    }

    @Test
    void thirdThursdayOfJanuary2025Test() {
        ForbiddenTime time = new ForbiddenTime(DayOfWeek.THURSDAY, 3, Month.JANUARY, Year.of(2025));
        assertTrue(time.getDate().isPresent());
        assertEquals(LocalDate.of(2025, Month.JANUARY, 16), time.getDate().get());
    }

    @Test
    void fifthMondayOfMay2023Test() {
        ForbiddenTime time = new ForbiddenTime(DayOfWeek.MONDAY, 5, Month.MAY, Year.of(2023));
        assertTrue(time.getDate().isPresent());
        assertEquals(LocalDate.of(2023, Month.MAY, 29), time.getDate().get());
    }

    @Test
    void sixthMondayOfJanuary2024Test() {
        assertThrows(IllegalArgumentException.class, () -> new ForbiddenTime(DayOfWeek.MONDAY, 6, Month.JANUARY, Year.of(2024)));
    }

    @Test
    void zerothFridayOfFebruary2025Test() {
        assertThrows(IllegalArgumentException.class, () -> new ForbiddenTime(DayOfWeek.FRIDAY, 0, Month.FEBRUARY, Year.of(2025)));
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
        assertEquals(DayOfWeek.SUNDAY, time.getDayOfWeek().get());
        assertTrue(time.getStartTime().get().isAfter(LocalTime.of(7, 0)));
        assertTrue(time.getStartTime().get().isBefore(LocalTime.of(11, 0)));
    }

    @Test
    void getNonRepeatingDayOfWeekTest() {
        ForbiddenTime time = new ForbiddenTime(DayOfWeek.WEDNESDAY, false);
        assertTrue(time.getDayOfWeek().isPresent());
        assertEquals(DayOfWeek.WEDNESDAY, time.getDayOfWeek().get());
        assertFalse(time.isRepeating());
    }

    @Test
    void getDateTest() {
        ForbiddenTime time = new ForbiddenTime(LocalDate.of(2024, 1, 13));
        assertTrue(time.getDate().isPresent());
        assertTrue(time.getDayOfWeek().isPresent());

        assertEquals(DayOfWeek.SATURDAY, time.getDayOfWeek().get());
        assertEquals(Month.JANUARY, time.getDate().get().getMonth());
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
    void doesNotIntersectTest_1() {
        LocalDate shiftDate = LocalDate.of(2024, 1, 24);
        ForbiddenTime forbiddenTime = new ForbiddenTime(shiftDate, LocalTime.of(8, 0), LocalTime.of(12, 0));
        LocalTime shiftStart = LocalTime.of(13, 0);
        LocalTime shiftEnd = LocalTime.of(14, 0);
        assertFalse(forbiddenTime.intersects(shiftStart, shiftEnd));
    }
}