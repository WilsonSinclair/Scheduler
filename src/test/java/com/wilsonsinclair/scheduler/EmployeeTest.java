package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    @Test
    void getNameTest() {
        Employee employee = new Employee("Wilson", true, true, true);
        assertEquals("Wilson", employee.getName());
    }

    @Test
    void canCloseTest() {
        Employee employee = new Employee("Wilson", true, true, true);
        assertTrue(employee.canClose());
    }

    @Test
    void cannotCloseTest() {
        Employee employee = new Employee("Wilson", true, false, true);
        assertFalse(employee.canClose());
    }

    @Test
    void canOpenTest() {
        Employee employee = new Employee("Wilson", true, true, true);
        assertTrue(employee.canOpen());
    }

    @Test
    void cannotOpenTest() {
        Employee employee = new Employee("Wilson", false, false, true);
        assertFalse(employee.canOpen());
    }

    @Test
    void getEmptyForbiddenTimesTest() {
        Employee wilson = new Employee("Wilson", true, true, true);
        assertTrue(wilson.getForbiddenTimes().isEmpty());
    }

    @Test
    void getNonEmptyForbiddenTimesTest() {
        Employee wilson = new Employee("Wilson", true, true, true);
        wilson.addForbiddenTime(new ForbiddenTime(DayOfWeek.SUNDAY, true));
        assertEquals(1, wilson.getForbiddenTimes().size());
    }

    @Test
    void cannotWorkOnDayOfWeekTest() {
        Employee wilson = new Employee("Wilson", true, true, true);
        wilson.addForbiddenTime(new ForbiddenTime(DayOfWeek.SUNDAY, true));
        assertFalse(wilson.canWork(DayOfWeek.SUNDAY));
    }

    @Test
    void canWorkOnDayOfWeekTest() {
        Employee wilson = new Employee("Wilson", true, true, true);
        wilson.addForbiddenTime(new ForbiddenTime(DayOfWeek.SUNDAY, true));
        assertTrue(wilson.canWork(DayOfWeek.MONDAY));
    }

    @Test
    void cannotWorkOnDateTest() {
        Employee wilson = new Employee("Wilson", true, true, true);
        LocalDate christmas = LocalDate.of(2024, 12, 25);
        wilson.addForbiddenTime(new ForbiddenTime(christmas));
        assertFalse(wilson.canWork(christmas));
    }

    @Test
    void canWorkOnDateTest() {
        Employee wilson = new Employee("Wilson", true, true, true);
        LocalDate birthday = LocalDate.of(2024, 12, 18);
        LocalDate christmas = LocalDate.of(2024, 12, 25);
        wilson.addForbiddenTime(new ForbiddenTime(christmas));
        assertTrue(wilson.canWork(birthday));
    }

    @Test
    void cannotWorkSpecifiedTimeOnDateTest() {
        Employee wilson = new Employee("Wilson", true, true, true);
        LocalDate shiftDate = LocalDate.of(2024, 12, 18);
        LocalTime shiftStart = LocalTime.of(9, 0);
        LocalTime shiftEnd = LocalTime.of(11, 0);
        wilson.addForbiddenTime(new ForbiddenTime(shiftDate, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        assertFalse(wilson.canWork(shiftDate, shiftStart, shiftEnd));
    }
}