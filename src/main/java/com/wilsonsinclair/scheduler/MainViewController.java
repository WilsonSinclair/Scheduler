package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.time.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import org.controlsfx.control.tableview2.TableView2;

public class MainViewController implements Initializable {

    @FXML
    private ContextMenu employeeListViewContextMenu, forbiddenTimesListViewContextMenu;

    @FXML
    private ListView<Employee> employeeListView;

    @FXML
    private ListView<ForbiddenTime> forbiddenTimesListView;

    @FXML
    private TextField employeeName;

    @FXML
    private RadioButton isOpenerButton, isCloserButton, isManagerButton;

    @FXML
    private Button saveEmployeeButton, addForbiddenTImeButton, generateScheduleButton, deleteScheduleButton;

    @FXML
    private TableView2<Employee> scheduleTable;

    @FXML
    private TableColumn<Employee, String> employeeNameColumn;

    @FXML
    private TableColumn<
        Employee,
        Shift
    > mondayColumn, tuesdayColumn, wednesdayColumn, thursdayColumn, fridayColumn, saturdayColumn, sundayColumn;

    @FXML
    private ComboBox<Schedule> scheduleComboBox;

    private static List<Schedule> schedules;

    private static Schedule schedule;

    @FXML
    public void loadEmployee() {
        Employee employee = getSelectedEmployee();

        //In case an empty cell is selected, this should avoid any Null Pointer exceptions.
        if (employee == null) {
            return;
        }

        employeeName.setDisable(false);
        isOpenerButton.setDisable(false);
        isCloserButton.setDisable(false);
        addForbiddenTImeButton.setDisable(false);
        isManagerButton.setDisable(false);

        saveEmployeeButton.setDisable(true);

        employeeName.setText(employee.getName());
        isOpenerButton.setSelected(employee.canOpen());
        isCloserButton.setSelected(employee.canClose());
        isManagerButton.setSelected(employee.isManager());

        ObservableList<ForbiddenTime> forbiddenTimes =
            FXCollections.observableArrayList(employee.getForbiddenTimes());
        forbiddenTimesListView.setItems(forbiddenTimes);
    }

    @FXML
    public void saveEmployees() {
        Employee e = employeeListView.getSelectionModel().getSelectedItem();
        e.setName(employeeName.getText());
        e.setOpener(isOpenerButton.isSelected());
        e.setCloser(isCloserButton.isSelected());
        e.setForbiddenTimes(forbiddenTimesListView.getItems());
        Serializer.saveEmployees(
            new SerializableObservableList<>(employeeListView.getItems())
        );
        new Alert(
            Alert.AlertType.INFORMATION,
            "Employee data saved",
            ButtonType.OK
        ).showAndWait();
        saveEmployeeButton.setDisable(true);
    }

    @FXML
    public void addEmployee() {
        employeeListView
            .getItems()
            .add(new Employee("Employee", false, false, false));
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
        alert
            .showAndWait()
            .ifPresent(response -> {
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
        final int index = employeeListView
            .getSelectionModel()
            .getSelectedIndex();
        if (index >= 0) {
            employeeListView.getItems().remove(index);
        }

        // We must now refresh the UI to reflect the now currently selected Employee
        // because the last selected one is now deleted from the ListView.
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
    private void handleAddForbiddenTimeButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(
                getClass().getResource("fobiddentime_view.fxml")
            );
            DialogPane pane = loader.load();
            ForbiddenTimeController forbiddenTimeController =
                loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Forbidden Time");

            Optional<ButtonType> response = dialog.showAndWait();

            if (response.isPresent() && response.get() == ButtonType.OK) {
                ForbiddenTime forbiddenTime =
                    forbiddenTimeController.getForbiddenTime();
                if (forbiddenTime != null) {
                    forbiddenTimesListView.getItems().add(forbiddenTime);
                    saveEmployees();
                }
            }
        } catch (IOException exception) {
            System.err.println(
                "Error loading the ForbiddenTimeView FXML file."
            );
        }
    }

    @FXML
    private void deleteForbiddenTime() {
        final int index = forbiddenTimesListView
            .getSelectionModel()
            .getSelectedIndex();
        if (index >= 0) {
            forbiddenTimesListView.getItems().remove(index);
        }
        saveEmployees();
    }

    private void populateScheduleTable(Schedule s) {
        if (s == null) {
            return;
        }

        // Clear existing employees' shift assignments
        for (Employee emp : s.employeeListProperty()) {
            emp.getAssignedShifts().clear();
        }

        // Reassign shifts from the schedule's days to employees
        for (Day day : s.getDays()) {
            for (Shift shift : day.getShifts()) {
                if (shift.getEmployee() != null) {
                    shift.getEmployee().assignShift(shift);
                }
            }
        }

        scheduleTable.getItems().setAll(s.employeeListProperty());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sets the Cell Factory for the items in this list view. We do this so that this list view does not use
        // the toString() implementation for Employee as this would provide unnecessary information.
        employeeListView.setCellFactory(
            new Callback<>() {
                @Override
                public ListCell<Employee> call(
                    ListView<Employee> employeeListView
                ) {
                    return new ListCell<>() {
                        @Override
                        public void updateItem(
                            Employee employee,
                            boolean empty
                        ) {
                            super.updateItem(employee, empty);
                            if (empty || employee == null) {
                                setText(null);
                            } else {
                                setText(employee.getName());
                            }
                        }
                    };
                }
            }
        );
        ObservableList<Employee> employeeList =
            FXCollections.observableArrayList(Employee.extractor());
        ArrayList<Employee> employees = Serializer.loadEmployees();
        employeeList.addAll(employees);
        employeeListView.setItems(employeeList);

        // Simply consumes any sort events, as we don't want the user to be able to sort columns on this table.
        scheduleTable.setOnSort(Event::consume);

        schedules = Serializer.loadSchedules();
        scheduleComboBox.setItems(FXCollections.observableArrayList(schedules));
        if (schedules.isEmpty()) {
            // If there are no previously saved schedules, we create a blank one as a placeholder.
            schedule = new Schedule(new ArrayList<>(), LocalDate.now());
        } else {
            //otherwise, we load the first schedule
            schedule = schedules.getFirst();
        }
        scheduleComboBox.setOnAction(event -> {
            populateScheduleTable(
                scheduleComboBox.getSelectionModel().getSelectedItem()
            );
        });

        generateScheduleButton.setOnAction(event -> {
            schedule = ScheduleFactory.generateSchedule(
                Serializer.loadEmployees(),
                LocalDate.now()
            );
            scheduleComboBox.getItems().add(schedule);
            scheduleComboBox.getSelectionModel().selectLast();

            Serializer.saveSchedules(
                new SerializableObservableList<>(scheduleComboBox.getItems())
            );
        });

        deleteScheduleButton.setOnAction(event -> {
            final int index = scheduleComboBox.getSelectionModel().getSelectedIndex();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING");
            alert.setHeaderText("Delete selected Schedule?");
            alert
                .showAndWait()
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        scheduleComboBox.getItems().remove(index);
                        Serializer.saveSchedules(
                                new SerializableObservableList<>(scheduleComboBox.getItems())
                        );
                    }
                });
            scheduleComboBox.getSelectionModel().select(index);
        });

        employeeNameColumn.setCellValueFactory(data ->
            data.getValue().nameProperty()
        );
        mondayColumn.setCellValueFactory(data ->
            data.getValue().mondayShiftProperty()
        );
        tuesdayColumn.setCellValueFactory(data ->
            data.getValue().tuesdayShiftProperty()
        );
        wednesdayColumn.setCellValueFactory(data ->
            data.getValue().wednesdayShiftProperty()
        );
        thursdayColumn.setCellValueFactory(data ->
            data.getValue().thursdayShiftProperty()
        );
        fridayColumn.setCellValueFactory(data ->
            data.getValue().fridayShiftProperty()
        );
        saturdayColumn.setCellValueFactory(data ->
            data.getValue().saturdayShiftProperty()
        );
        sundayColumn.setCellValueFactory(data ->
            data.getValue().sundayShiftProperty()
        );

        // Set custom cell factories for shift columns to display shift times
        mondayColumn.setCellFactory(column ->
            new TableCell<Employee, Shift>() {
                @Override
                protected void updateItem(Shift shift, boolean empty) {
                    super.updateItem(shift, empty);
                    if (empty || shift == null) {
                        setText(null);
                    } else {
                        setText(
                            shift.getStartTime() + " - " + shift.getEndTime()
                        );
                    }
                }
            }
        );

        tuesdayColumn.setCellFactory(column ->
            new TableCell<Employee, Shift>() {
                @Override
                protected void updateItem(Shift shift, boolean empty) {
                    super.updateItem(shift, empty);
                    if (empty || shift == null) {
                        setText(null);
                    } else {
                        setText(
                            shift.getStartTime() + " - " + shift.getEndTime()
                        );
                    }
                }
            }
        );

        wednesdayColumn.setCellFactory(column ->
            new TableCell<Employee, Shift>() {
                @Override
                protected void updateItem(Shift shift, boolean empty) {
                    super.updateItem(shift, empty);
                    if (empty || shift == null) {
                        setText(null);
                    } else {
                        setText(
                            shift.getStartTime() + " - " + shift.getEndTime()
                        );
                    }
                }
            }
        );

        thursdayColumn.setCellFactory(column ->
            new TableCell<Employee, Shift>() {
                @Override
                protected void updateItem(Shift shift, boolean empty) {
                    super.updateItem(shift, empty);
                    if (empty || shift == null) {
                        setText(null);
                    } else {
                        setText(
                            shift.getStartTime() + " - " + shift.getEndTime()
                        );
                    }
                }
            }
        );

        fridayColumn.setCellFactory(column ->
            new TableCell<Employee, Shift>() {
                @Override
                protected void updateItem(Shift shift, boolean empty) {
                    super.updateItem(shift, empty);
                    if (empty || shift == null) {
                        setText(null);
                    } else {
                        setText(
                            shift.getStartTime() + " - " + shift.getEndTime()
                        );
                    }
                }
            }
        );

        saturdayColumn.setCellFactory(column ->
            new TableCell<Employee, Shift>() {
                @Override
                protected void updateItem(Shift shift, boolean empty) {
                    super.updateItem(shift, empty);
                    if (empty || shift == null) {
                        setText(null);
                    } else {
                        setText(
                            shift.getStartTime() + " - " + shift.getEndTime()
                        );
                    }
                }
            }
        );

        sundayColumn.setCellFactory(column ->
            new TableCell<Employee, Shift>() {
                @Override
                protected void updateItem(Shift shift, boolean empty) {
                    super.updateItem(shift, empty);
                    if (empty || shift == null) {
                        setText(null);
                    } else {
                        setText(
                            shift.getStartTime() + " - " + shift.getEndTime()
                        );
                    }
                }
            }
        );
    }

    private Employee getSelectedEmployee() {
        return employeeListView.getSelectionModel().getSelectedItem();
    }
}
