package com.wilsonsinclair.scheduler.factories;

import com.wilsonsinclair.scheduler.Employee;

import com.wilsonsinclair.scheduler.time.Day;
import com.wilsonsinclair.scheduler.time.Schedule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleFactoryTest {

    private static final Employee testManager = new Employee("Test Manager", true, true, true);
    private static final Employee testShiftLead = new Employee("Test Shift Lead", true, true, false);
    private static final Employee testCrewMember = new Employee("Test Crew Member", false, false, false);


    // How much the manager's hours can vary from the target value
    private static final int managerHourVariance = 5;

    @Test
    void generateScheduleWith3LunchesAnd2ClosersTest() {
        List<Employee> employees = List.of(testManager, testShiftLead, testCrewMember);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        Schedule schedule = ScheduleFactory.generateSchedule(employees, startDate, 2, 1);
        final int lunchShifts = 3;
        final int closerShifts = 2;
        final int managerHours = 45;

        for (Day day : schedule.getDays()) {
            assertTrue(day.hasOpener());
            assertTrue(day.hasLunchers(lunchShifts));
            assertTrue(day.hasClosers(closerShifts));
            assertTrue(Math.abs(schedule.getEmployeeHours().get(employees.getFirst()) - managerHours) <= managerHourVariance);
        }
    }
}
