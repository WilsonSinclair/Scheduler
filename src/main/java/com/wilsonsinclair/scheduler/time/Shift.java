package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.*;
import java.time.LocalTime;

public class Shift implements Serializable {

    //The starting and ending times of this shift
    private transient ObjectProperty<LocalTime> startTime, endTime;

    //The employee that is assigned to this shift
    private transient ObjectProperty<Employee> employee;

    public Shift(Employee employee, LocalTime startTime, LocalTime endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
        setEmployee(employee);
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

    public void setEmployee(Employee e) { employeeProperty().set(e); }
    public void setStartTime(LocalTime t) { startTimeProperty().set(t); }
    public void setEndTime(LocalTime t) { endTimeProperty().set(t); }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(startTimeProperty().get());
        out.writeObject(endTimeProperty().get());
        out.writeObject(employeeProperty().get());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setStartTime((LocalTime) in.readObject());
        setEndTime((LocalTime) in.readObject());
        setEmployee((Employee) in.readObject());
    }


    @Override
    public String toString() {
        return getEmployee().getName() + "\nIn: " + getStartTime().toString() + "\nOut: " + getEndTime().toString() + "\n";
    }
}
