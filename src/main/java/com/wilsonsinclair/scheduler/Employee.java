package com.wilsonsinclair.scheduler;

import java.util.ArrayList;
import com.wilsonsinclair.scheduler.time.ForbiddenTime;

/*
    A class to represent an Employee that has shifts on the generated schedule.
    An employee may have specific days and/or times that they cannot work.
 */
public class Employee {

    private final String name;

    // A list of all the times an employee cannot work
    private final ArrayList<ForbiddenTime> forbiddenTimes;

    public Employee(String name) {
        this.name = name;
        forbiddenTimes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<ForbiddenTime> getForbiddenTimes() {
        return forbiddenTimes;
    }
}
