package com.wilsonsinclair.scheduler.time;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class ForbiddenTimeController {

    @FXML
    private DatePicker datePicker;

    private ForbiddenTime forbiddenTime;

    public void setForbiddenTime(ForbiddenTime forbiddenTime) {
        this.forbiddenTime = forbiddenTime;
    }

    public LocalDate getDateFromDatePicker() {
        return datePicker.getValue();
    }

}
