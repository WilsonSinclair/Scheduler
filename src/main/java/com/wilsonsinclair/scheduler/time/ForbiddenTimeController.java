package com.wilsonsinclair.scheduler.time;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.time.DayOfWeek;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class ForbiddenTimeController implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ComboBox<DayOfWeek> dayOFWeekComboBox;

    @FXML
    private ComboBox<Integer> startTimeComboBox, endTimeComboBox, startMinuteComboBox, endMinuteComboBox;

    private ForbiddenTime forbiddenTime;

    private final String[] CHOICE_BOX_VALUES = {"Certain Date", "Certain Time", "Day of Week"};

    private final Integer[] HOURS = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private final int[] MINUTES = IntStream.rangeClosed(0, 59).toArray();

    public ForbiddenTime getForbiddenTime() { return forbiddenTime; }

    /*
        Handles changes to the Day of Week combo box. If the forbiddenTime field has yet to be initialized, this
        initializes it and sets the day of the week to the chosen day.
     */
    private final EventHandler<ActionEvent> comboBoxEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (forbiddenTime == null) {
                forbiddenTime = new ForbiddenTime();
            }
            forbiddenTime.setDayOfWeek(dayOFWeekComboBox.getValue());
        }
    };

    /*
        Handles changes to the DatePicker. If the forbiddenTime field has yet to be initialized, this
        initializes it and sets the date to the chosen date.
     */
    private final EventHandler<ActionEvent> datePickerEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (forbiddenTime == null) {
                forbiddenTime = new ForbiddenTime();
            }
            forbiddenTime.setDate(datePicker.getValue());
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.setItems(FXCollections.observableArrayList(CHOICE_BOX_VALUES));
        dayOFWeekComboBox.setItems(FXCollections.observableArrayList(DayOfWeek.values()));

        dayOFWeekComboBox.setOnAction(comboBoxEvent);
        datePicker.setOnAction(datePickerEvent);

        startTimeComboBox.setItems(FXCollections.observableArrayList(HOURS));
        endTimeComboBox.setItems(FXCollections.observableArrayList(HOURS));

        // what the fuck
        Integer[] minutes = IntStream.of(MINUTES).boxed().toArray(Integer[]::new);
        startMinuteComboBox.setItems(FXCollections.observableArrayList(minutes));
        endMinuteComboBox.setItems(FXCollections.observableArrayList(minutes));

        //Listen for changes to this choice box
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, value, newValue) -> {
            switch (CHOICE_BOX_VALUES[newValue.intValue()]) {
                case "Certain Date":
                    datePicker.setDisable(false);
                    dayOFWeekComboBox.setDisable(true);
                    break;
                case "Certain Time":
                    datePicker.setDisable(true);
                    dayOFWeekComboBox.setDisable(true);
                    startTimeComboBox.setDisable(false);
                    endTimeComboBox.setDisable(false);
                    startMinuteComboBox.setDisable(false);
                    endMinuteComboBox.setDisable(false);
                    break;
                case "Day of Week":
                    dayOFWeekComboBox.setDisable(false);
                    datePicker.setDisable(true);
                    break;
                default:
                    //TODO
            }
        });
    }
}
