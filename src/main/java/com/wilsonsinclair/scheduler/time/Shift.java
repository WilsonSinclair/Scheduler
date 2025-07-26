package com.wilsonsinclair.scheduler.time;

import com.wilsonsinclair.scheduler.Employee;
import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.TreeSet;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Shift implements Serializable {

    public static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    public static final LocalTime CLOSING_TIME = LocalTime.of(21, 0);

    private static final LocalTime TWO_PM = LocalTime.of(14, 0);
    private static final LocalTime FOUR_PM = LocalTime.of(16, 0);

    enum ShiftType {
        OPENER,
        CLOSER,
        LUNCH,
        OPEN_TO_CLOSE,
        LUNCH_TO_CLOSE,
    }

    // A Set of times that opening shifts can end at. We ideally avoid CLOSING_TIME if possible.
    // The use of a TreeSet instead of HashSet guarantees the order of the elements as this is important.
    // When looping through these times, we want to try to assign an open to 2 or 4 shift before we
    // resort to an open to close.
    public static final Set<LocalTime> OPENING_SHIFT_END_TIMES = new TreeSet<>(
        Set.of(TWO_PM, FOUR_PM, CLOSING_TIME)
    );

    // A Set of times that lunch shifts can end at. Order here is important as before.
    public static final Set<LocalTime> LUNCH_SHIFT_END_TIMES = new TreeSet<>(
        Set.of(FOUR_PM, TWO_PM, CLOSING_TIME)
    );

    //The starting and ending times of this shift
    private transient ObjectProperty<LocalTime> startTime, endTime;

    private transient ObjectProperty<LocalDate> date;

    //The employee that is assigned to this shift
    private transient ObjectProperty<Employee> employee;

    @Serial
    private static final long serialVersionUID = 1L;

    private transient ShiftType shiftType;

    private final int duration;

    public Shift(Employee employee, LocalDate date, LocalTime startTime, LocalTime endTime) {
        setStartTime(startTime);
        setEndTime(endTime);
        setEmployee(employee);
        setDate(date);

        shiftType = assignShiftType();
        duration = (int) Duration.between(startTime, endTime).toMinutes();
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
        }
        return startTime;
    }

    public ObjectProperty<LocalTime> endTimeProperty() {
        if (endTime == null) {
            endTime = new SimpleObjectProperty<>();
        }
        return endTime;
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

    public Employee getEmployee() {
        return employeeProperty().get();
    }

    public int getDuration() { return duration; }

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

    public void setDate(LocalDate d) {
        dateProperty().set(d);
    }

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
        if (
            getStartTime().equals(LocalTime.of(10, 0)) ||
            getStartTime().equals(LocalTime.of(11, 0))
        ) {
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
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // Initialize transient properties first
        startTime = new SimpleObjectProperty<>();
        endTime = new SimpleObjectProperty<>();
        employee = new SimpleObjectProperty<>();
        date = new SimpleObjectProperty<>();

        setStartTime((LocalTime) in.readObject());
        setEndTime((LocalTime) in.readObject());
        setEmployee((Employee) in.readObject());
        setDate((LocalDate) in.readObject());

        // Reassign shift type after deserialization
        shiftType = assignShiftType();
    }

    @Override
    public String toString() {
        return (
            getStartTime().toString() + "\n" + getEndTime().toString() + "\n"
        );
    }
}
