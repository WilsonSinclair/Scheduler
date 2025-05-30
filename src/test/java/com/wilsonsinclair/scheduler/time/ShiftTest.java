package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import com.wilsonsinclair.scheduler.Serializer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ShiftTest {

    private static final List<Employee> employees = Serializer.loadEmployees();

    private Employee getRandomEmployee() {
        return employees.get(new Random().nextInt(employees.size()));
    }

    @Test
    void toStringTest() {
        Employee e = getRandomEmployee();
        Shift shift = new Shift(e, LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(12, 0));
        assertEquals("09:00\n12:00\n", shift.toString());
    }
}
