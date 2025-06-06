package com.wilsonsinclair.scheduler.time;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.wilsonsinclair.scheduler.Employee;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/*
    A class that models a day on a schedule. A day has a list of Shifts, with an employee being assigned
    to each of these shifts. A day has a date associated with it. A schedule is made up of 7 sequential Days.
 */
public class Day implements Serializable {
    
    private final LocalDate date;
    private transient ListProperty<Shift> shiftsProperty;

    public Day(LocalDate date) {
        this.date = date;
        shiftsProperty = new SimpleListProperty<>();
    }
    
    public ListProperty<Shift> shiftsProperty() {
        if (shiftsProperty == null || shiftsProperty.getValue() == null) {
            shiftsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        }
        return shiftsProperty;
    }

    public LocalDate getDate() { return date; }
    
    public List<Shift> getShifts() {
        return shiftsProperty().get();
    }
    
    public void addShift(Shift s) {
        shiftsProperty().add(s);
    }

    public boolean hasAssigned(Employee e) {
        for (Shift s : shiftsProperty) {
            if (s.getEmployee().equals(e)) { return true; }
        }
        return false;
    }

    /*
    This method checks if a given day has an opener shift.
    We only ever need one opener per day.
    */
    public boolean hasOpener() {
        for (Shift shift : shiftsProperty()) {
            Shift.ShiftType shiftType = shift.getType();
            if (shiftType == Shift.ShiftType.OPENER || shiftType == Shift.ShiftType.OPEN_TO_CLOSE) {
                return true;
            }
        }
        return false;
    }

    /*
   This method checks if a given day has the required number of closing shifts.
   The number of closers required may change depending on the store's sales volume.
   */
    public boolean hasClosers(int num) {
        int count = 0;
        for (Shift shift : shiftsProperty()) {
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

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        
        if (shiftsProperty == null || shiftsProperty.getValue() == null) {
            out.writeInt(0);
        }
        else {
            out.write(shiftsProperty().getSize());
            for (Shift shift : shiftsProperty) {
                out.writeObject(shift);
            }
        }
    }
    
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        
        shiftsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            shiftsProperty().add((Shift) in.readObject());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shift s : shiftsProperty) {
            sb.append(s);
        }
        return sb.toString();
    }
}
