package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import com.wilsonsinclair.scheduler.time.ForbiddenTimeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private ContextMenu employeeListViewContextMenu, forbiddenTimesListViewContextMenu;

    @FXML
    private ListView<Employee> employeeListView;

    @FXML
    private ListView<ForbiddenTime> forbiddenTimesListView;

    @FXML
    private TextField employeeName;

    @FXML
    private RadioButton isOpenerButton, isCloserButton;

    @FXML
    private Button saveEmployeeButton, addForbiddenTImeButton;

    @FXML
    public void loadEmployee() {
        Employee employee = getSelectedEmployee();

        //In case an empty cell is selected, this should avoid any Null Pointer exceptions.
        if (employee == null) { return; }

        employeeName.setDisable(false);
        isOpenerButton.setDisable(false);
        isCloserButton.setDisable(false);
        addForbiddenTImeButton.setDisable(false);

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
        e.setForbiddenTimes(forbiddenTimesListView.getItems());
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
        alert.setTitle("WARNING");
        alert.setHeaderText("Delete selected Employee?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteEmployee();

                // Now we change the Alert to inform the user that the deletion has taken place.
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("");
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

        // We save the employee data here now that the selected employee has been deleted.
        saveEmployees();
    }

    @FXML
    public void enableSaveButton() {
        if (saveEmployeeButton.isDisabled()) {
            saveEmployeeButton.setDisable(false);
        }
    }

    /*
        Method that handles the opening of the DialogPane used to add a
        forbidden time to an Employee.
     */
    @FXML
    private void handleAddForbiddenTimeButton(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fobiddentime_view.fxml"));
            DialogPane pane = loader.load();
            ForbiddenTimeController forbiddenTimeController = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Forbidden Time");

            Optional<ButtonType> response = dialog.showAndWait();

            if (response.isPresent() && response.get() == ButtonType.OK) {
                ForbiddenTime forbiddenTime = forbiddenTimeController.getForbiddenTime();
                if (forbiddenTime != null) {
                    forbiddenTimesListView.getItems().add(forbiddenTime);
                    saveEmployees();
                }
            }
        }
        catch (IOException exception) {
            System.err.println("Error loading the ForbiddenTimeView FXML file.");
        }

    }

    @FXML
    private void deleteForbiddenTime() {
        final int index = forbiddenTimesListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            forbiddenTimesListView.getItems().remove(index);
        }
        saveEmployees();
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
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(Employee.extractor());
        ArrayList<Employee> employees = Serializer.loadEmployees();
        employeeList.addAll(employees);
        employeeListView.setItems(employeeList);
    }

    private Employee getSelectedEmployee() {
        return employeeListView.getSelectionModel().getSelectedItem();
    }
}

