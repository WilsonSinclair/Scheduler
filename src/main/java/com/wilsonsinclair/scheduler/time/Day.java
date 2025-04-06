package com.wilsonsinclair.scheduler.time;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
    A class that models a day on a schedule. A day has a list of Shifts, with an employee being assigned
    to each of these shifts. A day has a date associated with it. A schedule is made up of 7 sequential Days.
 */
public class Day {
    private final LocalDate date;
    private List<Shift> shifts;

    public Day(LocalDate date) {
        this.date = date;
        shifts = new ArrayList<>();
    }

    public LocalDate getDate() { return date; }
    public List<Shift> getShifts() { return shifts; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shift s : shifts) {
            sb.append(s);
        }
        return sb.toString();
    }
}
