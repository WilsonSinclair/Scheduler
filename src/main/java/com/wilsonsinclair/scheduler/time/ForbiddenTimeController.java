package com.wilsonsinclair.scheduler.time;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ForbiddenTimeController implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<String> choiceBox;

    private ForbiddenTime forbiddenTime;

    private final String[] CHOICE_BOX_VALUES = {"Certain Date", "Certain Time", "Day of Week"};

    public void setForbiddenTime(ForbiddenTime forbiddenTime) {
        this.forbiddenTime = forbiddenTime;
    }

    public Optional<LocalDate> getDateFromDatePicker() {
        return Optional.ofNullable(datePicker.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.setItems(FXCollections.observableArrayList(CHOICE_BOX_VALUES));

        //Listen for changes to this choice box
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
                switch (CHOICE_BOX_VALUES[newValue.intValue()]) {
                    case "Certain Date":
                        datePicker.setDisable(false);
                        break;
                    case "Certain Time", "Day of Week":
                        datePicker.setDisable(true);
                        break;
                    default:
                        //TODO
                }
            }
        });
    }
}
