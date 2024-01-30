package com.wilsonsinclair.scheduler;

import java.io.*;
import java.util.ArrayList;

public class Serializer {

    private static final String employeeFile = "employees.ser";

    /*
        Serialize employees to a file for use later. We pass in a list of employees
        to only have to open 2 streams instead of 2 * number of employees.
     */
    public static void saveEmployees(ArrayList<Employee> employees) {
        try {
            FileOutputStream fileOut = new FileOutputStream(employeeFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(employees);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            //Temporary
            e.printStackTrace();
        }
    }

    public static ArrayList<Employee> loadEmployees() {
        ArrayList<Employee> loadedEmployees = null;
        try {
            FileInputStream fileIn = new FileInputStream(employeeFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadedEmployees = (ArrayList<Employee>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.err.println("Error while attempting to open ObjectInputStream");
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.err.println("Employee class not found while loading employees from disk.");
            c.printStackTrace();
        }
        return loadedEmployees;
    }
}
