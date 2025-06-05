package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DayTest {

    private static final Employee testManager = new Employee("Test Manager", true, true, true);
    private static final Employee testShiftLead = new Employee("Test Shift Lead", true, true, false);

    @Test
    void addShiftTest() {

    }

    @Test
    void hasOpenerTest() {
        LocalDate date = LocalDate.now();
        Schedule schedule = new Schedule(List.of(testManager), date);

        // Test with an empty schedule with no shifts assigned
        for (Day day : schedule.daysProperty()) {
            assertFalse(day.hasOpener());
        }

        Day day = schedule.getDays().getFirst();

        // Add an open to 2 shift. Expect true.
        day.addShift(new Shift(testManager, date, Shift.OPENING_TIME, LocalTime.of(14, 0)));
        assertTrue(day.hasOpener());
    }
}