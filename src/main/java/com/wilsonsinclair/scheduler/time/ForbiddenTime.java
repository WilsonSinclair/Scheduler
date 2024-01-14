package com.wilsonsinclair.scheduler.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/*
    A class to represent a time that a shift can not occur.
    This time can span the entire day, a portion of it, or the same time for every day of the week.
 */
public class ForbiddenTime {

    private boolean isRepeating = false;
    private LocalDate date;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public ForbiddenTime(DayOfWeek dayOfWeek, boolean isRepeating) {
        this.dayOfWeek = dayOfWeek;
        this.isRepeating = isRepeating;
    }

    public ForbiddenTime(LocalDate date) {
        this.date = date;
        this.dayOfWeek = date.getDayOfWeek();
    }

    public ForbiddenTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ForbiddenTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        isRepeating = true;
    }

    public Optional<LocalDate> getDate() {
        return Optional.ofNullable(date);
    }

    public Optional<LocalTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public Optional<LocalTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    public Optional<DayOfWeek> getDayOfWeek() {
        return Optional.ofNullable(dayOfWeek);
    }

    public boolean isRepeating() {
        return isRepeating;
    }
}
