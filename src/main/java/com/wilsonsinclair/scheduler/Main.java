package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import com.wilsonsinclair.scheduler.time.Schedule;
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
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        stage.setTitle("Scheduler");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        ArrayList<Employee> employees = new ArrayList<>();
        Employee wilson = new Employee("Wilson", true, true);
        wilson.addForbiddenTime(new ForbiddenTime(DayOfWeek.SATURDAY, true));

        Employee tiffany = new Employee("Tiffany", true, true);
        tiffany.addForbiddenTime(new ForbiddenTime(DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(21, 0), true));
        tiffany.addForbiddenTime(new ForbiddenTime(DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(21, 0), true));
        tiffany.addForbiddenTime(new ForbiddenTime(DayOfWeek.SATURDAY, LocalTime.of(14, 0), LocalTime.of(21, 0), true));

        employees.add(wilson);
        employees.add(tiffany);
        Serializer.saveEmployees(employees);

        launch();
    }
}