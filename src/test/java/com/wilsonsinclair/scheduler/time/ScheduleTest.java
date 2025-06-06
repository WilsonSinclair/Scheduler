package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {
    
    private static final Employee testManager = new Employee("Test Manager", true, true, true);
    private static final Employee testShiftLead = new Employee("Test Shift Lead", true, true, false);

    @Test
    void hasLunchersTest() {
        LocalDate date = LocalDate.now();
        Schedule schedule = new Schedule(List.of(testManager, testShiftLead), date);

        // Test with empty schedule. No shifts are assigned. Expect false. Assume 3 for lunch.
        for (Day day : schedule.daysProperty()) {
            assertFalse(schedule.hasLunchers(day, 3));
        }

        Day day = schedule.getDays().getFirst();

        // Add only an opening Shift and expect 2 for lunch. Expect false.
        day.addShift(new Shift(testManager, date, Shift.OPENING_TIME, LocalTime.of(14, 0)));
        assertFalse(schedule.hasLunchers(day, 2));

        // Now add a 10-2 shift. Still expect 2 for lunch. Expect true.
        day.addShift(new Shift(testShiftLead, date, LocalTime.of(10, 0), LocalTime.of(14, 0)));
        assertTrue(schedule.hasLunchers(day, 2));
    }
}
