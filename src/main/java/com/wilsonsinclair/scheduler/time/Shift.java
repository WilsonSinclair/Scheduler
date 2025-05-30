package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Shift implements Serializable {
    
    enum ShiftType {
        OPENER,
        CLOSER,
        LUNCH,
        OPEN_TO_CLOSE,
        LUNCH_TO_CLOSE
    }
     
    public static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    public static final LocalTime CLOSING_TIME = LocalTime.of(21, 0);

    //The starting and ending times of this shift
    private transient ObjectProperty<LocalTime> startTime, endTime;

    private transient ObjectProperty<LocalDate> date;

    //The employee that is assigned to this shift
    private transient ObjectProperty<Employee> employee;
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    private transient ShiftType shiftType;

    public Shift(Employee employee, LocalDate date, LocalTime startTime, LocalTime endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
        setEmployee(employee);
        setDate(date);
        
        shiftType = assignShiftType();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        if (date == null || date.getValue() == null) {
            date = new SimpleObjectProperty<>();
        }
        return date;
    }

    public ObjectProperty<LocalTime> startTimeProperty() {
        if (startTime == null || startTime.getValue() == null) {
            startTime = new SimpleObjectProperty<>();
        }
        return startTime;
    }

    public ObjectProperty<LocalTime> endTimeProperty() {
        if (endTime == null || endTime.getValue() == null) {
            endTime = new SimpleObjectProperty<>();
        }
        return endTime;
    }

    public ObjectProperty<Employee> employeeProperty() {
        if (employee == null || employee.getValue() == null) {
            employee = new SimpleObjectProperty<>();
        }
        return employee;
    }

    public LocalTime getStartTime() { return startTimeProperty().get(); }
    public LocalTime getEndTime() { return endTimeProperty().get(); }
    public Employee getEmployee() { return employeeProperty().get(); }
    public ShiftType getType() { return shiftType; }

    public void setEmployee(Employee e) { employeeProperty().set(e); }
    public void setStartTime(LocalTime t) { startTimeProperty().set(t); }
    public void setEndTime(LocalTime t) { endTimeProperty().set(t); }
    public void setDate(LocalDate d) { dateProperty().set(d); }
    
    /*
    Assigns a shift type based on the start and end times.
    */
    private ShiftType assignShiftType() {
        // Opening shifts
        if (getStartTime().equals(OPENING_TIME)) {
            if (getEndTime().equals(CLOSING_TIME)) {
                return ShiftType.OPEN_TO_CLOSE;
            }
            return ShiftType.OPENER;
        }
        
        // Lunch Shifts
        if (getStartTime().equals(LocalTime.of(10, 0)) || getStartTime().equals(LocalTime.of(11, 0))) {
            if (getEndTime().equals(CLOSING_TIME)) {
                return ShiftType.LUNCH_TO_CLOSE;
            }
            return ShiftType.LUNCH;
        }
        return ShiftType.CLOSER;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(startTimeProperty().get());
        out.writeObject(endTimeProperty().get());
        out.writeObject(employeeProperty().get());
        out.writeObject(dateProperty().get());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setStartTime((LocalTime) in.readObject());
        setEndTime((LocalTime) in.readObject());
        setEmployee((Employee) in.readObject());
        setDate((LocalDate) in.readObject());
    }


    @Override
    public String toString() {
        return getStartTime().toString() + "\n" + getEndTime().toString() + "\n";
    }
}
