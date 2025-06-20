package com.wilsonsinclair.scheduler.time;

import java.io.Serial;
import java.io.Serializable;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

/*
    A class to represent a tme that a shift can not occur.
    This time can span the entire day, a portion of it, or the same time for every day of the week.
 */
public class ForbiddenTime implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean isRepeating = false;
    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private int nthDay;

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

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
        The nth Day of the week of the month. For example, can't work the 1st Thursday of the month
        or can't work the 1st and 3rd Monday of the month. N must be in the range (0, 5] as a month
        cannot have <= 0 or > 5 occurrences of a specified day of the week.
     */
    public ForbiddenTime(DayOfWeek dayOfWeek, int n, Month month, Year year) {
        if (n <= 0 || n > 5) {
            throw new IllegalArgumentException("n must be in the range (0, 5]");
        }
        else {
            this.date = YearMonth.of(year.getValue(), month)
                    .atDay(1)
                    .with(TemporalAdjusters.dayOfWeekInMonth(n, dayOfWeek));
            this.nthDay = n;
        }
    }

    /*
        Just as above, but also has an associated starting and ending time
     */
    public ForbiddenTime(DayOfWeek dayOfWeek, int n, Month month, Year year, LocalTime startTime, LocalTime endTime) {
        if (n <= 0 || n > 5) {
            throw new IllegalArgumentException("n must be in the range (0, 5]");
        }
        else {
            this.date = YearMonth.of(year.getValue(), month)
                    .atDay(1)
                    .with(TemporalAdjusters.dayOfWeekInMonth(n, dayOfWeek));
            this.nthDay = n;
        }
        this.startTime = startTime;
        this.endTime = endTime;
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
        Default constructor for an empty Forbidden Time that is to be built out over time
     */
    public ForbiddenTime() {}

    /*
        Get the date of the forbidden time.
        It may or may not be present.
     */
    public Optional<LocalDate> getDate() {
        return Optional.ofNullable(date);
    }

    public void setDate(LocalDate date) { this.date = date; }

    public Optional<Integer> getNthDay() {
        return Optional.of(nthDay);
    }

    public void setNthDay(int n) {
        this.nthDay = n;
    }

    /*
        Get the starting time of the forbidden time.
        It may or may not be present.
     */
    public Optional<LocalTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public void setStartTime(int startHour, int startMinute) { this.startTime = LocalTime.of(startHour, startMinute); }

    public void setEndTime(int endHour, int endMinute) { this.endTime = LocalTime.of(endHour, endMinute); }

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

    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public boolean isRepeating() {
        return isRepeating;
    }

    public boolean isOn(DayOfWeek dayOfWeek) {
        if (getDayOfWeek().isEmpty()) {
            return false;
        }
        return this.dayOfWeek.equals(dayOfWeek);
    }

    public boolean isOn(LocalDate targetDate) {
        if (getDate().isEmpty()) {
            return false;
        }
        return date.equals(targetDate);
    }

    public boolean startsBeforeOrAt(LocalTime time) {
        if (getStartTime().isEmpty()) {
            return false;
        }
        return startTime.isBefore(time) || startTime.equals(time);
    }

    public boolean startsAfterOrAt(LocalTime time) {
        if (getStartTime().isEmpty()) {
            return false;
        }
        return startTime.isAfter(time) || startTime.equals(time);
    }

    public boolean endsBeforeOrAt(LocalTime time) {
        if (getEndTime().isEmpty()) {
            return false;
        }
        return endTime.isBefore(time) || endTime.equals(time);
    }

    public boolean endsAfterOrAt(LocalTime time) {
        if (getEndTime().isEmpty()) {
            return false;
        }
        return endTime.isAfter(time) || endTime.equals(time);
    }

    public boolean intersects(LocalTime shiftStart, LocalTime shiftEnd) {
        if (startsAfterOrAt(shiftStart) && startsBeforeOrAt(shiftEnd)) {
            return true;
        }
        else if (startsBeforeOrAt(shiftStart) && endsAfterOrAt(shiftEnd)) {
            return true;
        }
        else return endsAfterOrAt(shiftStart) && endsBeforeOrAt(shiftEnd);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (getDayOfWeek().isPresent()) {
            if (getNthDay().isPresent()) {
                switch (getNthDay().get()) {
                    case 1 -> sb.append("1st ");
                    case 2 -> sb.append("2nd ");
                    case 3 -> sb.append("3rd ");
                    case 4 -> sb.append("4th ");
                    case 5 -> sb.append("5th ");
                }
            }
            sb.append(dayOfWeek.toString()).append(" ");
        }
        if (getDate().isPresent()) {
            sb.append(date.toString()).append(" ");
        }
        if (getStartTime().isPresent() && getEndTime().isPresent()) {
            sb.append("From ");
            sb.append(startTime.format(timeFormatter));
            sb.append(" To ");
            sb.append(endTime.format(timeFormatter));
        }
        return sb.toString();
    }
}
