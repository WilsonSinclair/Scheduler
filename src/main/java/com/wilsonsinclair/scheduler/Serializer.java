package com.wilsonsinclair.scheduler;

import java.io.*;
import java.util.ArrayList;


public class Serializer {

    private static final String employeeFile = "employees.ser";

    /*
        Serialize employees to a file for use on subsequent runs of the application. We pass in a list of employees
        to only have to open 2 streams instead of 2 * number of employees.
     */
    public static void saveEmployees(SerializableObservableList<Employee> employees) {
        try {

            // write object to file
            FileOutputStream fos = new FileOutputStream(employeeFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(employees);
            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Employee> loadEmployees() {
        try {
            FileInputStream in = new FileInputStream(employeeFile);
            ObjectInputStream ois = new ObjectInputStream(in);
            SerializableObservableList<Employee> loadedEmployees = (SerializableObservableList<Employee>) ois.readObject();
            return new ArrayList<>(loadedEmployees.getData());

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Employee>();
    }
}
