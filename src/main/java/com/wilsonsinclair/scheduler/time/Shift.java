package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import java.io.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Shift implements Serializable {

    public static final LocalTime OPENING_SHIFT_START_TIME = LocalTime.of(8, 0);
    public static final LocalTime MANAGER_OPENING_SHIFT_START_TIME = LocalTime.of(7, 0);

    public static final LocalTime CLOSING_TIME = LocalTime.of(21, 0);

    public static final LocalTime TEN_AM = LocalTime.of(10, 0);
    public static final LocalTime ELEVEN_AM = LocalTime.of(11, 0);
    public static final LocalTime NOON = LocalTime.of(12, 0);

    public static final LocalTime ONE_PM = LocalTime.of(13, 0);
    public static final LocalTime TWO_PM = LocalTime.of(14, 0);
    public static final LocalTime FOUR_PM = LocalTime.of(16, 0);

    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mma");

    public enum ShiftType {
        OPENER,
        CLOSER,
        LUNCH,
        OPEN_TO_CLOSE,
        LUNCH_TO_CLOSE
    }

    // A Set of times that lunch shifts can end at. Order here is important as before.
    public static final List<LocalTime> LUNCH_SHIFT_END_TIMES = List.of(FOUR_PM, TWO_PM, CLOSING_TIME);

    //The starting and ending times of this shift
    private transient ObjectProperty<LocalTime> startTime, endTime;

    private transient ObjectProperty<LocalDate> date;

    //The employee that is assigned to this shift
    private transient ObjectProperty<Employee> employee;

    @Serial
    private static final long serialVersionUID = 1L;

    private transient ShiftType shiftType;

    private transient DoubleProperty hourDuration;

    public Shift(Employee employee, LocalDate date, LocalTime startTime, LocalTime endTime) {

        Objects.requireNonNull(employee);
        Objects.requireNonNull(date);
        Objects.requireNonNull(startTime);
        Objects.requireNonNull(endTime);

        setStartTime(startTime);
        setEndTime(endTime);
        setEmployee(employee);
        setDate(date);

        shiftType = assignShiftType();
        setDuration(endTime.getHour() - startTime.getHour());
    }

    public ObjectProperty<LocalDate> dateProperty() {
        if (date == null) {
            date = new SimpleObjectProperty<>();
        }
        return date;
    }

    public ObjectProperty<LocalTime> startTimeProperty() {
        if (startTime == null) {
            startTime = new SimpleObjectProperty<>();
            startTime.addListener((observable, oldTime, newTime) -> {
                if (oldTime == null) {
                    return;
                }
                hourDurationProperty().set(hourDurationProperty().get() + (oldTime.getHour() - newTime.getHour()));
            });
        }
        return startTime;
    }

    public ObjectProperty<LocalTime> endTimeProperty() {
        if (endTime == null) {
            endTime = new SimpleObjectProperty<>();
            endTime.addListener((observable, oldTime, newTime) -> {
                if (oldTime == null) {
                    return;
                }
                hourDurationProperty().set(hourDurationProperty().get() + (newTime.getHour() - oldTime.getHour()));
            });
        }
        return endTime;
    }

    public DoubleProperty hourDurationProperty() {
        if (hourDuration == null) {
            hourDuration = new SimpleDoubleProperty();
            hourDuration.addListener((observable, oldValue, newValue) -> {
                if (oldValue == null) {
                    return;
                }
                employeeProperty().get().calculateAssignedHours();
            });
        }
        return hourDuration;
    }

    public ObjectProperty<Employee> employeeProperty() {
        if (employee == null) {
            employee = new SimpleObjectProperty<>();
        }
        return employee;
    }

    public LocalTime getStartTime() {
        return startTimeProperty().get();
    }

    public LocalTime getEndTime() {
        return endTimeProperty().get();
    }

    public double getDuration() {
        return hourDurationProperty().get(); }

    public Employee getEmployee() {
        return employeeProperty().get();
    }

    public ShiftType getType() {
        return shiftType;
    }

    public void setEmployee(Employee e) {
        employeeProperty().set(e);
    }

    public void setStartTime(LocalTime t) {
        startTimeProperty().set(t);
    }

    public void setEndTime(LocalTime t) {
        endTimeProperty().set(t);
    }

    public void setDuration(double d) {
        hourDurationProperty().set(d);
    }

    public void setDate(LocalDate d) {
        dateProperty().set(d);
    }

    public void delayStartTime() {
        if (getStartTime().equals(OPENING_SHIFT_START_TIME) || getStartTime().equals(CLOSING_TIME)) {
            return;
        }
        setStartTime(getStartTime().plusHours(1));
    }

    public void delayEndTime() {
        if (getEndTime().equals(CLOSING_TIME)) {
            return;
        }
        setEndTime(getEndTime().plusHours(1));
    }

    public void accelerateEndTime() {
        if (getEndTime().equals(CLOSING_TIME)) {
            return;
        }
        setEndTime(getEndTime().minusHours(1));
    }

    /*
    Assigns a shift type based on the start and end times.
    */
    private ShiftType assignShiftType() {
        // Opening shifts
        if (getStartTime().equals(OPENING_SHIFT_START_TIME) || getStartTime().equals(MANAGER_OPENING_SHIFT_START_TIME)) {
            if (getEndTime().equals(CLOSING_TIME)) {
                return ShiftType.OPEN_TO_CLOSE;
            }
            return ShiftType.OPENER;
        }

        // Lunch Shifts
        if (getEndTime().equals(CLOSING_TIME)) {
            if (getStartTime().isAfter(OPENING_SHIFT_START_TIME) && getStartTime().isBefore(ONE_PM)) {
                return ShiftType.LUNCH_TO_CLOSE;
            }
            return ShiftType.CLOSER;
        }
        return ShiftType.LUNCH;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(startTimeProperty().get());
        out.writeObject(endTimeProperty().get());
        out.writeObject(employeeProperty().get());
        out.writeObject(dateProperty().get());
        out.writeDouble(hourDurationProperty().get());
    }

    @Serial
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // Initialize transient properties first
        startTime = new SimpleObjectProperty<>();
        endTime = new SimpleObjectProperty<>();
        employee = new SimpleObjectProperty<>();
        date = new SimpleObjectProperty<>();
        hourDuration = new SimpleDoubleProperty();

        setStartTime((LocalTime) in.readObject());
        setEndTime((LocalTime) in.readObject());
        setEmployee((Employee) in.readObject());
        setDate((LocalDate) in.readObject());
        setDuration(in.readDouble());

        // Reassign shift type after deserialization
        shiftType = assignShiftType();
    }

    @Override
    public String toString() {
        if (getEndTime().equals(CLOSING_TIME)) {
            return timeFormat.format(getStartTime()) + "-CL";
        }
        return timeFormat.format(getStartTime()) + "-" + timeFormat.format(getEndTime());
    }
}
