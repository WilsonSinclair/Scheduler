package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;

import java.time.LocalDate;
import java.util.ArrayList;

public class Schedule {

    private final ArrayList<Employee> employees;
    private final LocalDate startDate, endDate;

    /*
        Since a schedule spans one week, we only need the start date, as the end date is then inferred.
     */
    public Schedule(LocalDate startDate, ArrayList<Employee> employees) {
        this.employees = employees;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(7);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Start Date: ").append(startDate.toString());
        sb.append("\nEnd Date: ").append(endDate.toString());
        sb.append("\nEmployees on Schedule: ");
        employees.forEach(employee -> {
            sb.append(employee.getName()).append(", ");
        });
        return sb.toString();
    }
}
