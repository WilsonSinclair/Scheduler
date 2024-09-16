package com.wilsonsinclair.scheduler.time;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.ToggleSwitch;

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

    @FXML
    private ToggleSwitch startTimeAmPmToggleSwitch, endTimeAmPmToggleSwitch;

    @FXML
    private DialogPane pane;

    private ForbiddenTime forbiddenTime;

    private final String[] CHOICE_BOX_VALUES = {"Certain Date", "Certain Time", "Day of Week"};

    private final Integer[] HOURS = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private final int[] MINUTES = IntStream.rangeClosed(0, 59).toArray();

    public ForbiddenTime getForbiddenTime() { return forbiddenTime; }

    /*
        Handles changes to the Day of Week combo box. If the forbiddenTime field has yet to be initialized, this
        initializes it and sets the day of the week to the chosen day.
     */
    private final EventHandler<ActionEvent> dayOfWeekComboBoxEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (forbiddenTime == null) {
                forbiddenTime = new ForbiddenTime();
            }
            forbiddenTime.setDayOfWeek(dayOFWeekComboBox.getValue());
        }
    };

    // Handles changes to the starting and ending hour and/or minute combo boxes when the user
    // is adding a forbidden time of the specific time variety.
    private final EventHandler<ActionEvent> timeEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (forbiddenTime == null) {
                forbiddenTime = new ForbiddenTime();
            }
            int startHour, endHour, startMinute, endMinute;

            // initialize the above variables, assigning default values if the user has not yet
            // selected a value for one or more of them.
            startHour = startTimeComboBox.getValue() != null ? startTimeComboBox.getValue() : 1;
            endHour = endTimeComboBox.getValue() != null ? endTimeComboBox.getValue() : 1;
            startMinute = startMinuteComboBox.getValue() != null ? startMinuteComboBox.getValue() : 0;
            endMinute = endMinuteComboBox.getValue() != null ? endMinuteComboBox.getValue() : 0;

            if (startTimeAmPmToggleSwitch.isSelected() && startHour < 12) {
                startHour += 12;
            }
            if (endTimeAmPmToggleSwitch.isSelected() && endHour < 12) {
                endHour += 12;
            }

            forbiddenTime.setStartTime(startHour, startMinute);
            forbiddenTime.setEndTime(endHour, endMinute);
        }
    };

    /*
        Handles changes to the DatePicker. If the forbiddenTime field has yet to be initialized, this
        initializes it and sets the date to the chosen date.
     */
    private final EventHandler<ActionEvent> datePickerEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (forbiddenTime == null) {
                forbiddenTime = new ForbiddenTime();
            }
            forbiddenTime.setDate(datePicker.getValue());
        }
    };

    private final EventHandler<MouseEvent> amPMToggleSwitchEvent = mouseEvent -> {
        ToggleSwitch toggleSwitch = (ToggleSwitch) mouseEvent.getSource();
        toggleSwitch.setText(toggleSwitch.getText().equals("AM") ? "PM" : "AM");
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // The OK button at the bottom right of the dialog window
        Button okButton = (Button) pane.lookupButton(ButtonType.OK);

        choiceBox.setItems(FXCollections.observableArrayList(CHOICE_BOX_VALUES));
        dayOFWeekComboBox.setItems(FXCollections.observableArrayList(DayOfWeek.values()));

        // Attaching handlers
        startTimeAmPmToggleSwitch.setOnMouseClicked(amPMToggleSwitchEvent);
        endTimeAmPmToggleSwitch.setOnMouseClicked(amPMToggleSwitchEvent);

        // Populating hour values for the start and end time combo boxes
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
                    forbiddenTime = new ForbiddenTime();
                    okButton.setOnAction(datePickerEvent);
                    datePicker.setDisable(false);
                    dayOFWeekComboBox.setDisable(true);
                    break;
                case "Certain Time":
                    forbiddenTime = new ForbiddenTime();
                    okButton.setOnAction(timeEvent);
                    datePicker.setDisable(true);
                    dayOFWeekComboBox.setDisable(true);
                    startTimeComboBox.setDisable(false);
                    endTimeComboBox.setDisable(false);
                    startMinuteComboBox.setDisable(false);
                    endMinuteComboBox.setDisable(false);
                    startTimeAmPmToggleSwitch.setDisable(false);
                    endTimeAmPmToggleSwitch.setDisable(false);
                    break;
                case "Day of Week":
                    forbiddenTime = new ForbiddenTime();
                    okButton.setOnAction(dayOfWeekComboBoxEvent);
                    dayOFWeekComboBox.setDisable(false);
                    datePicker.setDisable(true);
                    break;
                default:
                    //TODO
            }
        });
    }
}
