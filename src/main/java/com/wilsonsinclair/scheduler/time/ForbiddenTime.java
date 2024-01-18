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

    /*
        A day of the week when an employee cannot work at all.
        This may be a repeating occurrence.
     */
    public ForbiddenTime(DayOfWeek dayOfWeek, boolean isRepeating) {
        this.dayOfWeek = dayOfWeek;
        this.isRepeating = isRepeating;
    }

    /*
        A day of the week when a certain time is not workable for an employee.
        This may be a repeating occurrence.
     */
    public ForbiddenTime(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, boolean isRepeating) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isRepeating = isRepeating;
    }

    /*
        A certain date that an employee cannot work. This is assumed to be non-repeating.
     */
    public ForbiddenTime(LocalDate date) {
        this.date = date;
        this.dayOfWeek = date.getDayOfWeek();
    }

    /*
        A certain date that an employee cannot work. This is assumed to be non-repeating.
        This day is constrained within the specified starting and ending times.
     */
    public ForbiddenTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /*
        A certain time during the day that an employee cannot work.
        This is assumed to be a repeating occurrence. (In school, other job, etc..)
     */
    public ForbiddenTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        isRepeating = true;
    }

    /*
        Get the date of the forbidden time.
        It may or may not be present.
     */
    public Optional<LocalDate> getDate() {
        return Optional.ofNullable(date);
    }

    /*
        Get the starting time of the forbidden time.
        It may or may not be present.
     */
    public Optional<LocalTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    /*
        Get the ending time of the forbidden time.
        It may or may not be present.
     */
    public Optional<LocalTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    /*
        Get the day of the week of the forbidden time.
        It may or may not be present.
     */
    public Optional<DayOfWeek> getDayOfWeek() {
        return Optional.ofNullable(dayOfWeek);
    }

    public boolean isRepeating() {
        return isRepeating;
    }
}
