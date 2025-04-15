package com.wilsonsinclair.scheduler.time;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/*
    The purpose of this class is to provide a wrapper for the Schedule, Day and Shift classes, so that their data
    can be more easily displayed in a TableView, which only accepts one class as a type.
 */
public class ScheduleTable {

    private ObjectProperty<Schedule> schedule;
    private ObjectProperty<Day> day;
    private ObjectProperty<Shift> shift;

    public ScheduleTable(Schedule s) {
        setSchedule(s);
        setDay(s.getDays().getFirst());
        setShift(getDay().getShifts().getFirst());
    }

    public ObjectProperty<Schedule> scheduleProperty() {
        if (schedule == null || schedule.getValue() == null) {
            schedule = new SimpleObjectProperty<>();
        }
        return schedule;
    }

    public ObjectProperty<Day> dayProperty() {
        if (day == null || day.getValue() == null) {
            day = new SimpleObjectProperty<>();
        }
        return day;
    }

    public ObjectProperty<Shift> shiftProperty() {
        if (shift == null || shift.getValue() == null) {
            shift = new SimpleObjectProperty<>();
        }
        return shift;
    }

    public void setSchedule(Schedule s) {
        scheduleProperty().set(s);
    }

    public void setDay(Day d){
        dayProperty().set(d);
    }

    public void setShift(Shift s) {
        shiftProperty().set(s);
    }

    public Schedule getSchedule() {
        return scheduleProperty().get();
    }

    public Day getDay() {
        return dayProperty().get();
    }

    public Shift getShift() {
        return shiftProperty().get();
    }
}
