package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.Schedule;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Serializer {

    private static final File EMPLOYEE_FILE = new File("employees.ser");
    private static final File SCHEDULE_FILE = new File("schedules.ser");
    
    /*
        Serialize employees to a file for use on subsequent runs of the application. We pass in a list of employees
        to only have to open 2 streams instead of 2 * number of employees.

        The use of the SerializableObservableList class allows the serialization of an ObservableList, which is what
        the ListView class uses to hold its list data.
     */
    public static void saveEmployees(SerializableObservableList<Employee> employees) {
        try {

            // write object to file
            FileOutputStream fos = new FileOutputStream(EMPLOYEE_FILE, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(employees);
            oos.close();
            fos.close();

        } catch (IOException e) {
            System.err.println("Error saving employees to a file.");
            e.printStackTrace();
        }
    }

    /*
        Serializes a list of schedules for reading on subsequent runs of the program.
     */

    public static void saveSchedules(SerializableObservableList<Schedule> schedules) {
        try {
            FileOutputStream fos = new FileOutputStream(SCHEDULE_FILE, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(schedules);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Employee> loadEmployees() {
        try {
            // checks if file exists and creates it if not
            // immediately return to avoid any EOF exceptions.
            if (EMPLOYEE_FILE.createNewFile()) {
                return new ArrayList<>();
            }

            FileInputStream in = new FileInputStream(EMPLOYEE_FILE);
            ObjectInputStream ois = new ObjectInputStream(in);
            SerializableObservableList<Employee> loadedEmployees = (SerializableObservableList<Employee>) ois.readObject();
            return new ArrayList<>(loadedEmployees.getData());

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Schedule> loadSchedules() {
        try {
            // checks if file exists and creates it if not
            // immediately return to avoid any EOF exceptions.
            if (SCHEDULE_FILE.createNewFile()) {
                return new ArrayList<>();
            }
            
            if (SCHEDULE_FILE.length() == 0) {
                return new ArrayList<>();
            }

            FileInputStream in = new FileInputStream(SCHEDULE_FILE);
            ObjectInputStream ois = new ObjectInputStream(in);
            SerializableObservableList<Schedule> loadedSchedules = (SerializableObservableList<Schedule>) ois.readObject();
            return new ArrayList<>(loadedSchedules.getData());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Returning empty schedule list");
        return new ArrayList<>();
    }
}
