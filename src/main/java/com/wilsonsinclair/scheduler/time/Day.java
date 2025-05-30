package com.wilsonsinclair.scheduler.time;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/*
    A class that models a day on a schedule. A day has a list of Shifts, with an employee being assigned
    to each of these shifts. A day has a date associated with it. A schedule is made up of 7 sequential Days.
 */
public class Day implements Serializable {
    
    private final LocalDate date;
    private ListProperty<Shift> shiftsProperty;

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
