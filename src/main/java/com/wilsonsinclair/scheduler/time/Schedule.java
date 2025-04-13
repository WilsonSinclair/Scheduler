package com.wilsonsinclair.scheduler.time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
    This class models an actual schedule posted in a store. The underlying data is just a List of Days. Each Day
    is simply a date that contains a list of Shifts. Each of these shifts has an assigned Employee and a start and end time.
 */
public class Schedule implements Serializable {

    private List<Day> days;

    public Schedule() {
        days = new ArrayList<>(7);
    }

    public List<Day> getDays() {
        return days;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Day day : days) {
            sb.append(day);
        }
        return sb.toString();
    }
}
