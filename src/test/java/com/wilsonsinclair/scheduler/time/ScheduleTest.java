package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {
    
    private static final Employee testManager = new Employee("Test Manager", true, true, true);
    private static final Employee testShiftLead = new Employee("Test Shift Lead", true, true, false);
}
