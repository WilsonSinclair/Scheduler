package com.wilsonsinclair.scheduler;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.util.Callback;

/*
    A class to represent an Employee that has shifts on the generated schedule.
    An employee may have specific days and/or times that they cannot work.
 */
public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private transient StringProperty name;
    private transient BooleanProperty isOpener;
    private transient BooleanProperty isCloser;

    // A list of all the times an employee cannot work
    private ArrayList<ForbiddenTime> forbiddenTimes;

    public Employee(String name, boolean isOpener, boolean isCloser) {
        setName(name);
        setCloser(isCloser);
        setOpener(isOpener);
        forbiddenTimes = new ArrayList<>();
    }

    public final StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty();
        }
        return name;
    }

    public final BooleanProperty openerProperty() {
        if (isOpener == null) {
            isOpener = new SimpleBooleanProperty();
        }
        return isOpener;
    }

    public final BooleanProperty closerProperty() {
        if (isCloser == null) {
            isCloser = new SimpleBooleanProperty();
        }
        return isCloser;
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public void setOpener(boolean isOpener) {
        openerProperty().set(isOpener);
    }

    public void setCloser(boolean isCloser) {
        closerProperty().set(isCloser);
    }

    public void setForbiddenTimes(ArrayList<ForbiddenTime> forbiddenTimes) {
        this.forbiddenTimes = forbiddenTimes;
    }

    public boolean canClose() {
        return closerProperty().get();
    }

    public boolean canOpen() {
        return openerProperty().get();
    }

    public List<ForbiddenTime> getForbiddenTimes() {
        return forbiddenTimes;
    }

    public void addForbiddenTime(ForbiddenTime time) {
        forbiddenTimes.add(time);
    }

    public boolean canWork(LocalDate date) {
        for (ForbiddenTime forbiddenTime : forbiddenTimes) {
            if (forbiddenTime.getDate().isPresent() && forbiddenTime.isOn(date)) {
                return false;
            }
        }
        return true;
    }

    public boolean canWork(LocalDate date, LocalTime shiftStart, LocalTime shiftEnd) {
        for (ForbiddenTime forbiddenTime : forbiddenTimes) {
            if (forbiddenTime.getDate().isPresent() && forbiddenTime.isOn(date)) {
                if (forbiddenTime.intersects(shiftStart, shiftEnd)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canWork(DayOfWeek dayOfWeek) {
        for (ForbiddenTime forbiddenTime : forbiddenTimes) {
            if (forbiddenTime.getDayOfWeek().isPresent() && forbiddenTime.isOn(dayOfWeek)) {
                return false;
            }
        }
        return true;
    }

    public static Callback<Employee, Observable[]> extractor() {
        return (Employee e) -> new Observable[]{e.nameProperty(), e.openerProperty(), e.closerProperty()};
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(getName());
        out.writeBoolean(canOpen());
        out.writeBoolean(canClose());
    }

    @Serial
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        nameProperty().set(s.readUTF());
        openerProperty().set(s.readBoolean());
        closerProperty().set(s.readBoolean());
        forbiddenTimes = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(name);
        sb.append("\nCan Open: ");
        sb.append(isOpener);
        sb.append("\nCan Close: ");
        sb.append(isCloser);

        sb.append("\nCannot Work: ");
        for (ForbiddenTime time : forbiddenTimes) {
            sb.append(time.toString());
            sb.append(", ");
        }
        return sb.toString();
    }
}
