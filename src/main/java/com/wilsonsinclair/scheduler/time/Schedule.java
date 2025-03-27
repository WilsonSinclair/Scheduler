package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;

import java.time.LocalDate;
import java.util.ArrayList;

import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;

public class Schedule {

    private final ArrayList<Employee> employees;
    private final LocalDate startDate, endDate;

    private final Table<Employee, LocalDate, Shift> scheduleTable;

    private final int SCHEDULE_LENGTH = 7;

    /*
        Since a schedule spans one week, we only need the start date, as the end date is then inferred.
     */
    public Schedule(LocalDate startDate, ArrayList<Employee> employees) {
        this.employees = employees;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(SCHEDULE_LENGTH);

        scheduleTable = HashBasedTable.create();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Start Date: ").append(startDate.toString());
        sb.append("\nEnd Date: ").append(endDate.toString()).append("\n");
        return sb.toString();
    }
}
