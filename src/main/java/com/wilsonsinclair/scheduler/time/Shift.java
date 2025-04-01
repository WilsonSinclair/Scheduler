package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;

import java.time.LocalTime;

public class Shift {

    //The starting and ending times of this shift
    private final LocalTime startTime, endTime;

    //The employee that is assigned to this shift
    private final Employee employee;

    public Shift(Employee employee, LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public Employee getEmployee() { return employee; }

    @Override
    public String toString() {
        return employee.getName() + "\nIn: " + startTime.toString() + "\nOut: " + endTime.toString();
    }
}
