package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        ArrayList<Employee> employees = new ArrayList<>();
        Employee employee = new Employee("Wilson", true, true);
        employee.addForbiddenTime(new ForbiddenTime(DayOfWeek.SUNDAY, LocalTime.of(7, 0), LocalTime.of(10, 0), true));
        employee.addForbiddenTime(new ForbiddenTime(LocalDate.of(2024, 1, 30)));
        employees.add(employee);

        Serializer.saveEmployees(employees);
        for (Employee e :  Serializer.loadEmployees()) {
            System.out.println(e.toString());
        }
    }
}