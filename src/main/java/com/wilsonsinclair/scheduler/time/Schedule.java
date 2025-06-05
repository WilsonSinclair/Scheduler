package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
    This class models an actual schedule posted in a store. The underlying data is just a List of Days. Each Day
    is simply a date that contains a list of Shifts. Each of these shifts has an assigned Employee and a start and end time.
 */
public class Schedule implements Serializable {

    private transient ListProperty<Day> daysProperty;
    private transient ListProperty<Employee> employees;

    private final LocalDate scheduleStartDate;

    @Serial
    private static final long serialVersionUID = 1L;

    public Schedule(List<Employee> e, LocalDate scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
        ObservableList<Day> list = FXCollections.observableArrayList();
        daysProperty = new SimpleListProperty<>(list);
        
        // Populate the schedule with 7 days
        for (int i = 0; i < 7; i++) {
            daysProperty().get().add(new Day(scheduleStartDate.plusDays(i)));
        }
        
        setEmployees(e);
    }

    public List<Day> getDays() {
        return daysProperty().get();
    }

    public ListProperty<Day> daysProperty() {
        if (daysProperty == null) daysProperty = new SimpleListProperty<>();
        return daysProperty;
    }

    public ListProperty<Employee> employeeListProperty() {
        if (employees == null || employees.getValue() == null) {
            employees = new SimpleListProperty<>();
        }
        return employees;
    }

    public void setEmployees(List<Employee> e) {
        employeeListProperty().set(FXCollections.observableArrayList(e));
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        // writing the list of days
        if (daysProperty == null || daysProperty.getValue() == null) {
            // Write the size of the List as 0.
            out.writeInt(0);
        }
        else {
            out.writeInt(daysProperty.size());
            // write the size of the list
            for (Object o : daysProperty.getValue()) {
                out.writeObject(o);
            }
        }

        // writing the list of employees
        if (employees == null || employees.getValue() == null) {
            out.writeInt(0);
        }
        else {
            out.writeInt(employees.size());
            for (Employee e : employees.getValue()) {
                out.writeObject(e);
            }
        }
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        daysProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        employees = new SimpleListProperty<>(FXCollections.observableArrayList());
        int size = in.readInt(); // the size of our list
        for (int i = 0; i < size; i++) { // we read until the end of the list
            daysProperty.add((Day) in.readObject());
        }

        size = in.readInt();
        for (int i = 0; i < size; i++) {
            employees.add((Employee) in.readObject());
        }
    }

    
    /*
    This method checks if a given day has the required number of closing shifts.
    The number of closers required may change depending on the store's sales volume.
    */
    public boolean hasClosers(Day day, int num) {
        int count = 0;
        for (Shift shift : day.shiftsProperty()) {
            Shift.ShiftType shiftType = shift.getType();
            if (shiftType == Shift.ShiftType.CLOSER || shiftType == Shift.ShiftType.OPEN_TO_CLOSE || shiftType == Shift.ShiftType.LUNCH_TO_CLOSE) {
                count++;
                if (count >= num) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /*
    This method, similar to the ones above, checks if a given Day has enough lunch shifts. The desired number
    may also change according to a store's sales volume.
     */
    public boolean hasLunchers(Day day, int num) {
        int count = 0;
        for (Shift shift : day.shiftsProperty()) {
            Shift.ShiftType shiftType = shift.getType();

            // All others shift types would be here during lunch, with the exception being a shift that is exclusively a closer.
            if (!shiftType.equals(Shift.ShiftType.CLOSER)) {
                count++;
                if (count >= num) {
                    return true;
                }
            }
        }
        return false;
    }

    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return format.format(scheduleStartDate) + " - " + format.format(scheduleStartDate.plusDays(7));
    }
}
