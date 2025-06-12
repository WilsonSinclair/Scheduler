package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import com.wilsonsinclair.scheduler.time.Shift;
import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
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
    private transient BooleanProperty isManager;

    // Shifts that are assigned to the employee
    private transient ListProperty<Shift> assignedShifts;

    private transient ObjectProperty<Shift> mondayShift;
    private transient ObjectProperty<Shift> tuesdayShift;
    private transient ObjectProperty<Shift> wednesdayShift;
    private transient ObjectProperty<Shift> thursdayShift;
    private transient ObjectProperty<Shift> fridayShift;
    private transient ObjectProperty<Shift> saturdayShift;
    private transient ObjectProperty<Shift> sundayShift;

    // A list of all the times an employee cannot work
    private transient ArrayList<ForbiddenTime> forbiddenTimes;

    public Employee(
        String name,
        boolean isOpener,
        boolean isCloser,
        boolean isManager
    ) {
        setName(name);
        setCloser(isCloser);
        setOpener(isOpener);
        setManager(isManager);
        forbiddenTimes = new ArrayList<>();
    }

    public final ListProperty<Shift> assignedShiftsProperty() {
        if (assignedShifts == null || assignedShifts.getValue() == null) {
            assignedShifts = new SimpleListProperty<>(
                FXCollections.observableArrayList()
            );
        }
        return assignedShifts;
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

    public final BooleanProperty managerProperty() {
        if (isManager == null) {
            isManager = new SimpleBooleanProperty();
        }
        return isManager;
    }

    public final ObjectProperty<Shift> mondayShiftProperty() {
        if (mondayShift == null || mondayShift.getValue() == null) {
            mondayShift = new SimpleObjectProperty<>();
        }
        return mondayShift;
    }

    public final ObjectProperty<Shift> tuesdayShiftProperty() {
        if (tuesdayShift == null || tuesdayShift.getValue() == null) {
            tuesdayShift = new SimpleObjectProperty<>();
        }
        return tuesdayShift;
    }

    public final ObjectProperty<Shift> wednesdayShiftProperty() {
        if (wednesdayShift == null || wednesdayShift.getValue() == null) {
            wednesdayShift = new SimpleObjectProperty<>();
        }
        return wednesdayShift;
    }

    public final ObjectProperty<Shift> thursdayShiftProperty() {
        if (thursdayShift == null || thursdayShift.getValue() == null) {
            thursdayShift = new SimpleObjectProperty<>();
        }
        return thursdayShift;
    }

    public final ObjectProperty<Shift> fridayShiftProperty() {
        if (fridayShift == null || fridayShift.getValue() == null) {
            fridayShift = new SimpleObjectProperty<>();
        }
        return fridayShift;
    }

    public final ObjectProperty<Shift> saturdayShiftProperty() {
        if (saturdayShift == null || saturdayShift.getValue() == null) {
            saturdayShift = new SimpleObjectProperty<>();
        }
        return saturdayShift;
    }

    public final ObjectProperty<Shift> sundayShiftProperty() {
        if (sundayShift == null || sundayShift.getValue() == null) {
            sundayShift = new SimpleObjectProperty<>();
        }
        return sundayShift;
    }

    public void setForbiddenTimes(List<ForbiddenTime> times) {
        forbiddenTimes = new ArrayList<>(times);
    }

    public ArrayList<ForbiddenTime> getForbiddenTimes() {
        return forbiddenTimes;
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

    public boolean canClose() {
        return closerProperty().get();
    }

    public boolean canOpen() {
        return openerProperty().get();
    }

    public boolean isManager() {
        return managerProperty().get();
    }

    public void setManager(boolean isManager) {
        managerProperty().set(isManager);
    }

    public List<Shift> getAssignedShifts() {
        return assignedShiftsProperty().get();
    }

    public void setMondayShift(Shift s) {
        mondayShiftProperty().set(s);
    }

    public void setTuesdayShift(Shift s) {
        tuesdayShiftProperty().set(s);
    }

    public void setWednesdayShift(Shift s) {
        wednesdayShiftProperty().set(s);
    }

    public void setThursdayShift(Shift s) {
        thursdayShiftProperty().set(s);
    }

    public void setFridayShift(Shift s) {
        fridayShiftProperty().set(s);
    }

    public void setSaturdayShift(Shift s) {
        saturdayShiftProperty().set(s);
    }

    public void setSundayShift(Shift s) {
        sundayShiftProperty().set(s);
    }

    public void addForbiddenTime(ForbiddenTime time) {
        forbiddenTimes.add(time);
    }

    public void assignShift(Shift s) {
        getAssignedShifts().add(s);

        // Check if the shift has a valid date
        if (s.dateProperty().getValue() == null) {
            System.err.println("Shift has no date associated with it.");
            return;
        }

        switch (s.dateProperty().getValue().getDayOfWeek()) {
            case MONDAY -> setMondayShift(s);
            case TUESDAY -> setTuesdayShift(s);
            case WEDNESDAY -> setWednesdayShift(s);
            case THURSDAY -> setThursdayShift(s);
            case FRIDAY -> setFridayShift(s);
            case SATURDAY -> setSaturdayShift(s);
            case SUNDAY -> setSundayShift(s);
            default -> System.err.println(
                "Shift has no day of week associated with it."
            ); // Should never get here
        }
    }

    public boolean canWork(LocalDate date) {
        for (ForbiddenTime forbiddenTime : forbiddenTimes) {
            if (
                forbiddenTime.getDate().isPresent() && forbiddenTime.isOn(date)
            ) {
                return false;
            }
        }
        return true;
    }

    public boolean canWork(
        LocalDate date,
        LocalTime shiftStart,
        LocalTime shiftEnd
    ) {
        for (ForbiddenTime forbiddenTime : forbiddenTimes) {
            if (
                forbiddenTime.getDate().isPresent() && forbiddenTime.isOn(date)
            ) {
                if (forbiddenTime.intersects(shiftStart, shiftEnd)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canWork(DayOfWeek dayOfWeek) {
        for (ForbiddenTime forbiddenTime : forbiddenTimes) {
            if (
                forbiddenTime.getDayOfWeek().isPresent() &&
                forbiddenTime.isOn(dayOfWeek)
            ) {
                return false;
            }
        }
        return true;
    }

    public boolean canWork(Shift s) {
        // Can work on this day of the week in general
        if (canWork(s.dateProperty().get().getDayOfWeek())) {
            return canWork(
                s.dateProperty().get(),
                s.getStartTime(),
                s.getEndTime()
            );
        }
        return false;
    }

    public static Callback<Employee, Observable[]> extractor() {
        return (Employee e) ->
            new Observable[] {
                e.nameProperty(),
                e.openerProperty(),
                e.closerProperty(),
            };
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(getName());
        out.writeBoolean(canOpen());
        out.writeBoolean(canClose());
        out.writeBoolean(isManager());
        out.writeObject(getForbiddenTimes());

        // writing assigned shifts
        if (assignedShifts == null || assignedShifts.getValue() == null) {
            out.writeInt(0);
        } else {
            int size = assignedShiftsProperty().size();
            out.writeInt(size);
            for (Shift s : assignedShiftsProperty()) {
                out.writeObject(s);
            }
        }
    }

    @Serial
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        nameProperty().set(in.readUTF());
        openerProperty().set(in.readBoolean());
        closerProperty().set(in.readBoolean());
        managerProperty().set(in.readBoolean());
        forbiddenTimes = (ArrayList<ForbiddenTime>) in.readObject();

        // reading assigned shifts
        int size = in.readInt();
        assignedShifts = new SimpleListProperty<>(
            FXCollections.observableArrayList()
        );
        for (int i = 0; i < size; i++) {
            Shift shift = (Shift) in.readObject();
            assignShift(shift);
        }
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
