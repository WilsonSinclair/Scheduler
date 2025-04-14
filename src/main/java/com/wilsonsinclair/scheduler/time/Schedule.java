package com.wilsonsinclair.scheduler.time;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;

/*
    This class models an actual schedule posted in a store. The underlying data is just a List of Days. Each Day
    is simply a date that contains a list of Shifts. Each of these shifts has an assigned Employee and a start and end time.
 */
public class Schedule implements Serializable {

    private transient ListProperty<Day> daysProperty;

    public Schedule() {
        ObservableList<Day> list = FXCollections.observableArrayList();
        daysProperty = new SimpleListProperty<>(list);
    }

    public List<Day> getDaysProperty() {
        return daysProperty().get();
    }

    public ListProperty<Day> daysProperty() {
        if (daysProperty == null) daysProperty = new SimpleListProperty<>();
        return daysProperty;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
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
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        daysProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
        int size = in.readInt(); // the size of our list
        for (int i = 0; i < size; i++) { // we read until the end of the list
            daysProperty.add((Day) in.readObject());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Day day : getDaysProperty()) {
            sb.append(day);
        }
        return sb.toString();
    }
}
