package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void getName() {
        Employee employee = new Employee("Wilson", true, true);
        assertEquals(employee.getName(), "Wilson");
    }

    @Test
    void canClose() {
        Employee employee = new Employee("Wilson", true, true);
        assertTrue(employee.canClose());
    }

    @Test
    void cannotClose() {
        Employee employee = new Employee("Wilson", true, false);
        assertFalse(employee.canClose());
    }

    @Test
    void canOpen() {
        Employee employee = new Employee("Wilson", true, true);
        assertTrue(employee.canOpen());
    }

    @Test
    void cannotOpen() {
        Employee employee = new Employee("Wilson", false, false);
        assertFalse(employee.canOpen());
    }

    @Test
    void getEmptyForbiddenTimes() {
        Employee wilson = new Employee("Wilson", true, true);
        assertTrue(wilson.getForbiddenTimes().isEmpty());
    }

    @Test
    void getNonEmptyForbiddenTimes() {
        Employee wilson = new Employee("Wilson", true, true);
        wilson.addForbiddenTime(new ForbiddenTime(DayOfWeek.SUNDAY, true));
        assertEquals(wilson.getForbiddenTimes().size(), 1);
    }

    @Test
    void cannotWorkOnDayOfWeek() {
        Employee wilson = new Employee("Wilson", true, true);
        wilson.addForbiddenTime(new ForbiddenTime(DayOfWeek.SUNDAY, true));
        assertFalse(wilson.canWork(DayOfWeek.SUNDAY));
    }

    @Test
    void canWorkOnDayOfWeek() {
        Employee wilson = new Employee("Wilson", true, true);
        wilson.addForbiddenTime(new ForbiddenTime(DayOfWeek.SUNDAY, true));
        assertTrue(wilson.canWork(DayOfWeek.MONDAY));
    }

    @Test
    void cannotWorkOnDate() {
        Employee wilson = new Employee("Wilson", true, true);
        LocalDate christmas = LocalDate.of(2024, 12, 25);
        wilson.addForbiddenTime(new ForbiddenTime(christmas));
        assertFalse(wilson.canWork(christmas));
    }

    @Test
    void canWorkOnDate() {
        Employee wilson = new Employee("Wilson", true, true);
        LocalDate birthday = LocalDate.of(2024, 12, 18);
        LocalDate christmas = LocalDate.of(2024, 12, 25);
        wilson.addForbiddenTime(new ForbiddenTime(christmas));
        assertTrue(wilson.canWork(birthday));
    }

    @Test
    void cannotWorkSpecifiedTimeOnDate() {
        Employee wilson = new Employee("Wilson", true, true);
        LocalDate shiftDate = LocalDate.of(2024, 12, 18);
        LocalTime shiftStart = LocalTime.of(9, 0);
        LocalTime shiftEnd = LocalTime.of(11, 0);
        wilson.addForbiddenTime(new ForbiddenTime(shiftDate, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        assertFalse(wilson.canWork(shiftDate, shiftStart, shiftEnd));
    }
}