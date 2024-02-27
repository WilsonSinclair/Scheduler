package com.wilsonsinclair.scheduler;

import java.io.Serial;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;

/*
    A class to represent an Employee that has shifts on the generated schedule.
    An employee may have specific days and/or times that they cannot work.
 */
public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private boolean isOpener;
    private boolean isCloser;

    // A list of all the times an employee cannot work
    private ArrayList<ForbiddenTime> forbiddenTimes;

    public Employee(String name, boolean isOpener, boolean isCloser) {
        this.name = name;
        this.isCloser = isCloser;
        this.isOpener = isOpener;
        forbiddenTimes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpener(boolean isOpener) {
        this.isOpener = isOpener;
    }

    public void setCloser(boolean isCloser) {
        this.isCloser = isCloser;
    }

    public void setForbiddenTimes(ArrayList<ForbiddenTime> forbiddenTimes) {
        this.forbiddenTimes = forbiddenTimes;
    }

    public boolean canClose() {
        return isCloser;
    }

    public boolean canOpen() {
        return isOpener;
    }

    public List<ForbiddenTime> getForbiddenTimes() {
        return forbiddenTimes;
    }

    public void addForbiddenTime(ForbiddenTime time) {
        forbiddenTimes.add(time);
    }

    public boolean canWork(LocalDate date) {
        for (ForbiddenTime forbiddenTime : forbiddenTimes) {
            if (forbiddenTime.getDate().isPresent() && forbiddenTime.isOn(date)) {
                return false;
            }
        }
        return true;
    }

    public boolean canWork(LocalDate date, LocalTime shiftStart, LocalTime shiftEnd) {
        for (ForbiddenTime forbiddenTime : forbiddenTimes) {
            if (forbiddenTime.getDate().isPresent() && forbiddenTime.isOn(date)) {
                if (forbiddenTime.intersects(shiftStart, shiftEnd)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canWork(DayOfWeek dayOfWeek) {
        for (ForbiddenTime forbiddenTime : forbiddenTimes) {
            if (forbiddenTime.getDayOfWeek().isPresent() && forbiddenTime.isOn(dayOfWeek)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(name);
        sb.append("\nCan Open: ");
        sb.append(isOpener);
        sb.append("\nCan Close: ");
        sb.append(isCloser);

        sb.append("\nCannot Work: ");
        for (ForbiddenTime time : forbiddenTimes) {
            sb.append(time.toString());
            sb.append(", ");
        }
        return sb.toString();
    }
}
