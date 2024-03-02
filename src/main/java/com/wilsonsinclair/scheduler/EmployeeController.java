package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private ListView<Employee> employeeListView;

    @FXML
    private ListView<ForbiddenTime> forbiddenTimesListView;

    @FXML
    private Tab employeeTab;

    @FXML
    private TextField employeeName;

    @FXML
    private RadioButton isOpenerButton, isCloserButton;

    @FXML
    private Button saveEmployeeButton, addEmployeeButton, deleteEmployeeButton;

    @FXML
    public void loadEmployee() {
        Employee employee = employeeListView.getSelectionModel().getSelectedItem();

        employeeName.setDisable(false);
        isOpenerButton.setDisable(false);
        isCloserButton.setDisable(false);


        employeeName.setText(employee.getName());
        isOpenerButton.setSelected(employee.canOpen());
        isCloserButton.setSelected(employee.canClose());

        ObservableList<ForbiddenTime> forbiddenTimes = FXCollections.observableArrayList(employee.getForbiddenTimes());
        forbiddenTimesListView.setItems(forbiddenTimes);
    }

    @FXML
    public void saveEmployees() {
        Employee e = employeeListView.getSelectionModel().getSelectedItem();
        e.setName(employeeName.getText());
        e.setOpener(isOpenerButton.isSelected());
        e.setCloser(isCloserButton.isSelected());
        e.setForbiddenTimes(new ArrayList<>(forbiddenTimesListView.getItems()));
        employeeListView.refresh();
        Serializer.saveEmployees(new SerializableObservableList<>(employeeListView.getItems()));
    }

    @FXML
    public void addEmployee() {
        employeeListView.getItems().add(new Employee("Employee", false, false));
        employeeListView.getSelectionModel().selectLast();
        loadEmployee();
    }

    @FXML
    public void deleteEmployee() {
        final int index = employeeListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            employeeListView.getItems().remove(index);
        }
        loadEmployee();
        saveEmployees();
    }

    private void clearFields() {
        employeeName.clear();
        isOpenerButton.setSelected(false);
        isCloserButton.setSelected(false);
        forbiddenTimesListView.setItems(FXCollections.emptyObservableList());
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
        ArrayList<Employee> employees = Serializer.loadEmployees();
        employeeListView.setItems(FXCollections.observableArrayList(employees));
    }
}

