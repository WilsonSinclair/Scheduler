package com.wilsonsinclair.scheduler.time;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXToggleButton;

import java.net.URL;
import java.time.DayOfWeek;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class ForbiddenTimeController implements Initializable {

    @FXML
    private MFXDatePicker datePicker;

    @FXML
    private MFXComboBox<String> typeComboBox;

    @FXML
    private MFXComboBox<String> ordinalChoiceBox;

    @FXML
    private MFXComboBox<DayOfWeek> dayOfWeekComboBox;

    @FXML
    private MFXComboBox<Integer> startHourComboBox, endHourComboBox, startMinuteComboBox, endMinuteComboBox;
    
    @FXML
    private MFXToggleButton allDayDateSwitch, allDayDayOfWeekSwitch, startTimeAmPmToggleSwitch, endTimeAmPmToggleSwitch;

    @FXML
    private DialogPane forbiddenTimeDialogPane;

    private ForbiddenTime forbiddenTime;

    private final String[] TYPE_CHOICE_BOX_VALUES = {"Certain Date", "Certain Time", "Day of Week"};

    private final String[] ORDINAL_CHOICE_BOX_VALUES = {"All", "1st", "2nd", "3rd", "4th", "5th"};

    private final Integer[] HOURS = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    private final int[] MINUTES = IntStream.rangeClosed(0, 59).toArray();

    public ForbiddenTime getForbiddenTime() { return forbiddenTime; }

    /*
        Handles changes to the Day of Week combo box.
     */
    private final EventHandler<ActionEvent> dayOfWeekComboBoxEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            DayOfWeek dayOfWeek = dayOfWeekComboBox.getValue();
            switch (ordinalChoiceBox.getSelectionModel().getSelectedIndex()) {
                //TODO
                case 1 -> forbiddenTime.setNthDay(1);
                case 2 -> forbiddenTime.setNthDay(2);
                case 3 -> forbiddenTime.setNthDay(3);
                case 4 -> forbiddenTime.setNthDay(4);
                case 5 -> forbiddenTime.setNthDay(5);

            }
            forbiddenTime.setDayOfWeek(dayOfWeek);
            if (!allDayDayOfWeekSwitch.isSelected()) {
                addForbiddenTime();
            }
        }
    };

    // Called when the user is adding a specific time and the OK button is clicked to finally
    // create and add this time.
    private final EventHandler<ActionEvent> timeEvent = actionEvent -> addForbiddenTime();

    // Helper method that is used to create a forbidden time from the combo boxes and add
    // that to the ForbiddenTime field in this class. This is used in the timeEvent handler and
    // optionally in the datePickerEvent and dayOfWeek handler if that specific date has a time associated with it.
    private void addForbiddenTime() {
        int startHour, endHour, startMinute, endMinute;

        // initialize the above variables, assigning default values if the user has not yet
        // selected a value for one or more of them.
        startHour = startHourComboBox.getValue() != null ? startHourComboBox.getValue() : 1;
        endHour = endHourComboBox.getValue() != null ? endHourComboBox.getValue() : 1;
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

    /*
        Handles changes to the DatePicker.
     */
    private final EventHandler<ActionEvent> datePickerEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            forbiddenTime.setDate(datePicker.getValue());
            if (!allDayDateSwitch.isSelected()) {
                addForbiddenTime();
            }
        }
    };

    private final EventHandler<MouseEvent> amPMToggleSwitchEvent = mouseEvent -> {
        MFXToggleButton toggleSwitch = (MFXToggleButton) mouseEvent.getSource();
        if (toggleSwitch.isSelected()) {
            toggleSwitch.setText("PM");
        }
        else {
            toggleSwitch.setText("AM");
        }
    };

    // Handles changes to the allDay switches, which when toggled, mean that there is no need
    // to associate a time with either a certain date or day of the week and vice versa
    private final EventHandler<MouseEvent> allDaySwitchEvent = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {

            // The toggle switch that triggered this handler
            MFXToggleButton toggleSwitch = (MFXToggleButton) mouseEvent.getSource();

            // This should never fail
            assert(toggleSwitch != null);

            // the employee is only unable to work within a certain time on this day of the week
            if (!toggleSwitch.isSelected()) {
                startHourComboBox.setDisable(false);
                startMinuteComboBox.setDisable(false);
                endHourComboBox.setDisable(false);
                endMinuteComboBox.setDisable(false);
                startTimeAmPmToggleSwitch.setDisable(false);
                endTimeAmPmToggleSwitch.setDisable(false);
            } else {
                startHourComboBox.setDisable(true);
                startMinuteComboBox.setDisable(true);
                endHourComboBox.setDisable(true);
                endMinuteComboBox.setDisable(true);
                startTimeAmPmToggleSwitch.setDisable(true);
                endTimeAmPmToggleSwitch.setDisable(true);
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // The OK button at the bottom right of the dialog window
        Button okButton = (Button) forbiddenTimeDialogPane.lookupButton(ButtonType.OK);
        
        typeComboBox.setItems(FXCollections.observableArrayList(TYPE_CHOICE_BOX_VALUES));
        ordinalChoiceBox.setItems(FXCollections.observableArrayList(ORDINAL_CHOICE_BOX_VALUES));
        ordinalChoiceBox.getSelectionModel().selectFirst();

        dayOfWeekComboBox.setItems(FXCollections.observableArrayList(DayOfWeek.values()));

        // Attaching handlers
        startTimeAmPmToggleSwitch.setOnMouseClicked(amPMToggleSwitchEvent);
        endTimeAmPmToggleSwitch.setOnMouseClicked(amPMToggleSwitchEvent);

        // Populating hour values for the start and end time combo boxes
        startHourComboBox.setItems(FXCollections.observableArrayList(HOURS));
        endHourComboBox.setItems(FXCollections.observableArrayList(HOURS));

        // what the fuck
        Integer[] minutes = IntStream.of(MINUTES).boxed().toArray(Integer[]::new);
        startMinuteComboBox.setItems(FXCollections.observableArrayList(minutes));
        endMinuteComboBox.setItems(FXCollections.observableArrayList(minutes));

        allDayDateSwitch.setOnMouseClicked(allDaySwitchEvent);
        allDayDayOfWeekSwitch.setOnMouseClicked(allDaySwitchEvent);

        //Listen for changes to this choice box
        typeComboBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, value, newValue) -> {
            switch (TYPE_CHOICE_BOX_VALUES[newValue.intValue()]) {
                case "Certain Date" -> {
                    forbiddenTime = new ForbiddenTime();
                    okButton.setOnAction(datePickerEvent);
                    startHourComboBox.setDisable(true);
                    endHourComboBox.setDisable(true);
                    startMinuteComboBox.setDisable(true);
                    endMinuteComboBox.setDisable(true);
                    startTimeAmPmToggleSwitch.setDisable(true);
                    endTimeAmPmToggleSwitch.setDisable(true);
                    datePicker.setDisable(false);
                    allDayDateSwitch.setDisable(false);
                    allDayDayOfWeekSwitch.setDisable(true);
                    dayOfWeekComboBox.setDisable(true);
                    ordinalChoiceBox.setDisable(true);

                    okButton.disableProperty().bind(datePicker.valueProperty().isNull());
                }
                case "Certain Time" -> {
                    forbiddenTime = new ForbiddenTime();
                    okButton.setOnAction(timeEvent);
                    datePicker.setDisable(true);
                    dayOfWeekComboBox.setDisable(true);
                    startHourComboBox.setDisable(false);
                    endHourComboBox.setDisable(false);
                    startMinuteComboBox.setDisable(false);
                    endMinuteComboBox.setDisable(false);
                    startTimeAmPmToggleSwitch.setDisable(false);
                    endTimeAmPmToggleSwitch.setDisable(false);

                    allDayDateSwitch.setDisable(true);
                    allDayDayOfWeekSwitch.setDisable(true);

                    ordinalChoiceBox.setDisable(true);

                    okButton.disableProperty().bind(startHourComboBox.getSelectionModel().selectedItemProperty().isNull());
                    okButton.disableProperty().bind(startMinuteComboBox.getSelectionModel().selectedItemProperty().isNull());
                    okButton.disableProperty().bind(endHourComboBox.getSelectionModel().selectedItemProperty().isNull());
                    okButton.disableProperty().bind(endMinuteComboBox.getSelectionModel().selectedItemProperty().isNull());
                }
                case "Day of Week" -> {
                    forbiddenTime = new ForbiddenTime();
                    okButton.setOnAction(dayOfWeekComboBoxEvent);
                    startHourComboBox.setDisable(true);
                    endHourComboBox.setDisable(true);
                    startMinuteComboBox.setDisable(true);
                    endMinuteComboBox.setDisable(true);
                    startTimeAmPmToggleSwitch.setDisable(true);
                    endTimeAmPmToggleSwitch.setDisable(true);
                    allDayDayOfWeekSwitch.setDisable(false);
                    allDayDateSwitch.setDisable(true);
                    dayOfWeekComboBox.setDisable(false);
                    datePicker.setDisable(true);

                    ordinalChoiceBox.setDisable(false);

                    okButton.disableProperty().bind(dayOfWeekComboBox.valueProperty().isNull());
                }
                default -> {
                }
            }
        });
    }
}
