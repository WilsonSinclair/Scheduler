package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
    The purpose of this class is to provide a wrapper for the Schedule, Day and Shift classes, so that their data
    can be more easily displayed in a TableView, which only accepts one class as a type.
 */
public class ScheduleTable {

    private ObjectProperty<Schedule> schedule;
    private ListProperty<Day> days;
    private ListProperty<Shift> shifts;
    private static ListProperty<Employee> employees;

    // Map each Employee to a list of shifts that belong to them.
    private Map<Employee, List<Shift>> employeeShiftMap;

    public ScheduleTable(Schedule s, List<Employee> e) {
        setEmployees(e);
        setSchedule(s);
        setDays(s.getDays());
        setShifts(this.getDays().stream().flatMap(day -> day.getShifts().stream()).collect(Collectors.toList()));
    }

    public ListProperty<Employee> employeeProperty() {
        if (employees == null || employees.getValue() == null) {
            employees = new SimpleListProperty<>();
        }
        return employees;
    }

    public ObjectProperty<Schedule> scheduleProperty() {
        if (schedule == null || schedule.getValue() == null) {
            schedule = new SimpleObjectProperty<>();
        }
        return schedule;
    }

    public ListProperty<Day> daysProperty() {
        if (days == null || days.getValue() == null) {
            days = new SimpleListProperty<>();
        }
        return days;
    }

    public ListProperty<Shift> shiftProperty() {
        if (shifts == null || shifts.getValue() == null) {
            shifts = new SimpleListProperty<>();
        }
        return shifts;
    }

    public void setSchedule(Schedule s) {
        scheduleProperty().set(s);
    }

    public void setDays(List<Day> d){
        daysProperty().set(FXCollections.observableArrayList(d));
    }

    public void setShifts(List<Shift> s) {
        shiftProperty().set(FXCollections.observableArrayList(s));
    }

    public void setEmployees(List<Employee> e) {
        employeeProperty().set(FXCollections.observableArrayList(e));
    }

    public Schedule getSchedule() {
        return scheduleProperty().get();
    }

    public List<Day> getDays() {
        return daysProperty().get();
    }

    public List<Shift> getShifts() {
        return shiftProperty().get();
    }
}
