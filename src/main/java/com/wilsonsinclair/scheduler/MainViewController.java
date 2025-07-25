package com.wilsonsinclair.scheduler;

import com.wilsonsinclair.scheduler.factories.EmployeeListCellFactory;
import com.wilsonsinclair.scheduler.factories.ScheduleFactory;
import com.wilsonsinclair.scheduler.time.ForbiddenTimeController;
import com.wilsonsinclair.scheduler.factories.ForbiddenTimeListCellFactory;
import com.wilsonsinclair.scheduler.time.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.mfxcore.utils.converters.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;

import javafx.util.StringConverter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainViewController implements Initializable {

    @FXML
    private MFXListView<Employee> employeeListView;

    @FXML
    private MFXListView<ForbiddenTime> forbiddenTimesListView;

    @FXML
    private TextField employeeName, managerHoursTextField;

    @FXML
    private MFXRadioButton isOpenerButton, isCloserButton, isManagerButton;

    @FXML
    private MFXButton saveEmployeeButton, addForbiddenTimeButton, deleteScheduleButton, generateScheduleButton;

    @FXML
    private MFXTableView<Employee> scheduleTable;

    @FXML
    private MFXComboBox<Schedule> scheduleComboBox;

    @FXML
    private MFXComboBox<Integer> numLunchersComboBox, numClosersComboBox;

    private static List<Schedule> schedules;

    private static Schedule schedule;

    private static final Integer[] NUM_LUNCHERS_CHOICES = new Integer[]{2, 3, 4};
    private static final Integer[] NUM_CLOSERS_CHOICES = new Integer[]{2, 3};

    private static final Logger logger = LoggerFactory.getLogger(MainViewController.class);

    @FXML
    public void loadSelectedEmployee() {
        Employee employee = getSelectedEmployee();

        //In case an empty cell is selected, this should avoid any Null Pointer exceptions.
        if (employee == null) {
            return;
        }

        employeeName.setText(employee.getName());
        isOpenerButton.setSelected(employee.canOpen());
        isCloserButton.setSelected(employee.canClose());
        isManagerButton.setSelected(employee.isManager());

        employeeName.setDisable(false);
        isOpenerButton.setDisable(false);
        isCloserButton.setDisable(false);
        addForbiddenTimeButton.setDisable(false);
        isManagerButton.setDisable(false);
        saveEmployeeButton.setDisable(true);

        ObservableList<ForbiddenTime> forbiddenTimes = FXCollections.observableArrayList(employee.getForbiddenTimes());
        forbiddenTimesListView.setItems(forbiddenTimes);
    }

    @FXML
    public void saveEmployees() {
        Employee e = getSelectedEmployee();
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
    public void handleSaveSettingsButton() {
        try {
            Settings newSettings = Settings.getInstance();
            newSettings.setNumLunchers(numLunchersComboBox.getValue());
            newSettings.setNumClosers(numClosersComboBox.getValue());
            newSettings.setManagerHours(Integer.parseInt(managerHoursTextField.getText()));
            newSettings.save();
        } catch (IOException e) {
            logger.error("Error saving settings.", e);
        }
    }

    @FXML
    public void addEmployee() {
        Employee oldEmployee = getSelectedEmployee();
        employeeListView
            .getItems()
            .add(new Employee("Employee", false, false, false));
        employeeListView.getSelectionModel().selectIndex(employeeListView.getItems().size() - 1);
        employeeListView.getSelectionModel().deselectItem(oldEmployee);
        loadSelectedEmployee();
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
    private void deleteEmployee() {
        Employee e = getSelectedEmployee();
        if (e != null) {
            employeeListView.getItems().remove(e);
            employeeListView.getSelectionModel().selectIndex(employeeListView.getItems().size() - 1);
        }

        // We must now refresh the UI to reflect the now currently selected Employee
        // because the last selected one is now deleted from the ListView.
        loadSelectedEmployee();

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
            ForbiddenTimeController forbiddenTimeController = loader.getController();

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
            logger.error("Error loading the ForbiddenTimeView FXML file.", exception);
        }
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

        numLunchersComboBox.setItems(FXCollections.observableArrayList(NUM_LUNCHERS_CHOICES));
        numClosersComboBox.setItems(FXCollections.observableArrayList(NUM_CLOSERS_CHOICES));

        // Read settings from settings.json
        Settings settings = Settings.getInstance();
        numLunchersComboBox.selectItem(settings.getNumLunchers());
        numClosersComboBox.selectItem(settings.getNumClosers());
        managerHoursTextField.setText(Integer.toString(settings.getManagerHours()));

        employeeName.textProperty().addListener((observable, oldValue, newValue) -> {
           if (!oldValue.equals(newValue)) {
               enableSaveButton();
           }
        });

        ObservableList<Employee> employeeList = FXCollections.observableArrayList(Employee.extractor());
        employeeList.addAll(Serializer.loadEmployees());
        employeeListView.setItems(employeeList);

        schedules = Serializer.loadSchedules();
        scheduleComboBox.setItems(FXCollections.observableArrayList(schedules));
        if (schedules.isEmpty()) {
            // If there are no previously saved schedules, we create a blank one as a placeholder.
            schedule = new Schedule(new ArrayList<>(), LocalDate.now());
        } else {
            //otherwise, we load the first schedule
            schedule = schedules.getFirst();
        }

        scheduleComboBox.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            populateScheduleTable(newValue);
        });

        generateScheduleButton.setOnAction(event -> {
            schedule = ScheduleFactory.generateSchedule(
                Serializer.loadEmployees(),
                LocalDate.now(),
                Settings.getInstance()
            );
            scheduleComboBox.getItems().add(schedule);
            scheduleComboBox.getSelectionModel().selectLast();

            Serializer.saveSchedules(
                new SerializableObservableList<>(scheduleComboBox.getItems())
            );
        });

        deleteScheduleButton.disableProperty().bind(scheduleComboBox.getSelectionModel().selectedIndexProperty().lessThan(0));

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
            scheduleComboBox.getSelectionModel().selectLast();
        });

        MFXTableColumn<Employee> employeeNameColumn = new MFXTableColumn<>("Name");
        MFXTableColumn<Employee> mondayColumn = new MFXTableColumn<>("Monday");
        MFXTableColumn<Employee> tuesdayColumn = new MFXTableColumn<>("Tuesday");
        MFXTableColumn<Employee> wednesdayColumn = new MFXTableColumn<>("Wednesday");
        MFXTableColumn<Employee> thursdayColumn = new MFXTableColumn<>("Thursday");
        MFXTableColumn<Employee> fridayColumn = new MFXTableColumn<>("Friday");
        MFXTableColumn<Employee> saturdayColumn = new MFXTableColumn<>("Saturday");
        MFXTableColumn<Employee> sundayColumn = new MFXTableColumn<>("Sunday");

        employeeNameColumn.setRowCellFactory(employee -> new MFXTableRowCell<>(Employee::getName));
        mondayColumn.setRowCellFactory(employee -> new MFXTableRowCell<>(Employee::getMondayShiftAsString));
        tuesdayColumn.setRowCellFactory(employee -> new MFXTableRowCell<>(Employee::getTuesdayShiftAsString));
        wednesdayColumn.setRowCellFactory(employee -> new MFXTableRowCell<>(Employee::getWednesdayShiftAsString));
        thursdayColumn.setRowCellFactory(employee -> new MFXTableRowCell<>(Employee::getThursdayShiftAsString));
        fridayColumn.setRowCellFactory(employee -> new MFXTableRowCell<>(Employee::getFridayShiftAsString));
        saturdayColumn.setRowCellFactory(employee -> new MFXTableRowCell<>(Employee::getSaturdayShiftAsString));
        sundayColumn.setRowCellFactory(employee -> new MFXTableRowCell<>(Employee::getSundayShiftAsString));

        scheduleTable.getTableColumns().addAll(employeeNameColumn, mondayColumn, tuesdayColumn, wednesdayColumn, thursdayColumn, fridayColumn, saturdayColumn, sundayColumn);

        scheduleTable.autosizeColumnsOnInitialization();

        StringConverter<Employee> converter = FunctionalStringConverter.to(employee -> (employee == null) ? "" : employee.getName());
        employeeListView.setCellFactory(employee -> new EmployeeListCellFactory(this, employeeListView, employee));
        employeeListView.getSelectionModel().setAllowsMultipleSelection(false);
        employeeListView.setConverter(converter);
        employeeListView.features().enableBounceEffect();
        employeeListView.features().enableSmoothScrolling(0.5);

        forbiddenTimesListView.setCellFactory(forbiddenTime -> new ForbiddenTimeListCellFactory(this, forbiddenTimesListView, forbiddenTime));
        forbiddenTimesListView.features().enableBounceEffect();
        forbiddenTimesListView.features().enableSmoothScrolling(0.5);

        ValidationSupport validator = new ValidationSupport();
        validator.registerValidator(managerHoursTextField, Validator.createRegexValidator("Value must be a number > 0", "^[1-9][0-9]*$", Severity.ERROR));
    }

    private Employee getSelectedEmployee() {
        return employeeListView.getSelectionModel().getSelectedValue();
    }
}
