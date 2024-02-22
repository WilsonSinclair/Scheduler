package com.wilsonsinclair.scheduler.time;

import java.time.LocalTime;

public class Shift {

    private final LocalTime startTime, endTime;

    public Shift(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    @Override
    public String toString() {
        return startTime.toString() + " - " + endTime.toString();
    }
}
