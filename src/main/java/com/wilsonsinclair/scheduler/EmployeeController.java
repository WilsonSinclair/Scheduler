package com.wilsonsinclair.scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private ListView<Employee> employeeListView;

    @FXML
    private Tab employeeTab;

    @FXML
    private TextField employeeName;

    @FXML
    private RadioButton isOpenerButton, isCloserButton;

    @FXML
    public void loadEmployee() {
        Employee employee = employeeListView.getSelectionModel().getSelectedItem();

        employeeName.setDisable(false);
        isOpenerButton.setDisable(false);
        isCloserButton.setDisable(false);

        employeeName.setText(employee.getName());
        isOpenerButton.setSelected(employee.canOpen());
        isCloserButton.setSelected(employee.canClose());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Sets the Cell Factory for the items in this list view. We do this so that this list view does not use
        // the toString() implementation for Employee as this would provide unnecessary information.
        employeeListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Employee> call(ListView<Employee> employeeListView) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(Employee employee, boolean empty) {
                        super.updateItem(employee, empty);
                        if (empty || employee == null) {
                            setText(null);
                        } else {
                            setText(employee.getName());
                        }
                    }
                };
            }
        });
        ObservableList<Employee> employees = FXCollections.observableArrayList(Serializer.loadEmployees());
        employeeListView.setItems(employees);
    }
}

