package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
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

        /*
        ArrayList<Employee> employees = new ArrayList<>();
        Employee wilson = new Employee("Wilson", true, true);
        wilson.addForbiddenTime(new ForbiddenTime(DayOfWeek.SATURDAY, true));
        employees.add(wilson);
        employees.add(new Employee("Zoe", false, false));
        Serializer.saveEmployees(new SerializableObservableList<>(employees));
        */

        launch();
    }
}