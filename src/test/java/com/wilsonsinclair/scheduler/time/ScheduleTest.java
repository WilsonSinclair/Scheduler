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
        Shift s1 = new Shift(employees.get(0), LocalTime.of(8, 0), LocalTime.of(14, 0));
        Shift s2 = new Shift(employees.get(1), LocalTime.of(10, 0), LocalTime.of(14, 0));
        Shift s3 = new Shift(employees.get(2), LocalTime.of(11, 0), LocalTime.of(14, 0));

        Day day = new Day(LocalDate.now());
        day.getShifts().addAll(Arrays.asList(s1, s2, s3));

        Schedule schedule = new Schedule();
        schedule.getDaysProperty().add(day);
        Serializer.saveSchedules(List.of(schedule));
        System.out.println(schedule);
    }
}
