package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import javafx.collections.FXCollections;
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

        saveEmployeeButton.setDisable(true);

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
        Serializer.saveEmployees(new SerializableObservableList<>(employeeListView.getItems()));
        new Alert(Alert.AlertType.INFORMATION, "Employee data saved", ButtonType.OK).showAndWait();
        saveEmployeeButton.setDisable(true);
    }

    @FXML
    public void addEmployee() {
        employeeListView.getItems().add(new Employee("Employee", false, false));
        employeeListView.getSelectionModel().selectLast();
        loadEmployee();
    }

    /*
        Displays a confirmation popup to ensure that the user wishes to delete the selected Employee.
        If they select 'OK', the deletion is done and vice versa.
     */
    @FXML
    public void confirmEmployeeDeletion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete selected Employee?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteEmployee();

                // Now we change the Alert to inform the user that the deletion has taken place.
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Employee Deleted");
                alert.show();
            }
        });
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

    @FXML
    public void enableSaveButton() {
        if (saveEmployeeButton.isDisabled()) {
            saveEmployeeButton.setDisable(false);
        }
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
        ObservableList<Employee> observableList = FXCollections.observableArrayList(Employee.extractor());
        ArrayList<Employee> employees = Serializer.loadEmployees();
        observableList.addAll(employees);
        employeeListView.setItems(observableList);
    }
}

