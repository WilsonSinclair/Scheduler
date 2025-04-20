package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import com.wilsonsinclair.scheduler.Serializer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ScheduleTest {

    private static final List<Employee> employees = Serializer.loadEmployees();

    @Test
    void toStringTest() {
        Shift s1 = new Shift(employees.get(0), LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(14, 0));

        Schedule schedule = new Schedule(employees);
        schedule.employeeListProperty().getValue().getFirst().assignShift(s1);

        Serializer.saveSchedules(List.of(schedule));
    }
}
