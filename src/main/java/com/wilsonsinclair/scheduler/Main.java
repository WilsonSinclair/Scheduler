package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
        Employee e = new Employee("Wilson", true, true);
        e.addForbiddenTime(new ForbiddenTime(LocalDate.of(2024, 12, 18)));
        SerializableObservableList<Employee> employees = new SerializableObservableList<>(List.of(e));
        Serializer.saveEmployees(employees);
         */

        launch();
    }
}