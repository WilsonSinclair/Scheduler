package com.wilsonsinclair.scheduler.factories;

import com.wilsonsinclair.scheduler.Employee;
import com.wilsonsinclair.scheduler.MainViewController;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class EmployeeListCellFactory extends MFXListCell<Employee> {
    private final MFXFontIcon userIcon;
    private final MFXFontIcon deleteIcon;
    private final Label nameLabel;

    public EmployeeListCellFactory(MainViewController controller, MFXListView<Employee> listView, Employee data) {
        super(listView, data);

        userIcon = new MFXFontIcon("fas-user", 18);
        deleteIcon = new MFXFontIcon("fas-trash", 18);
        nameLabel = new Label();

        HBox.setHgrow(deleteIcon, Priority.ALWAYS);

        deleteIcon.getStyleClass().add("delete-icon");
        deleteIcon.setVisible(false);
        this.setOnMouseEntered(event -> deleteIcon.setVisible(true));
        this.setOnMouseExited(event -> deleteIcon.setVisible(false));

        deleteIcon.setOnMouseClicked(event -> {
            controller.confirmEmployeeDeletion();
        });

        userIcon.getStyleClass().add("user-icon");

        nameLabel.getStyleClass().add("data-label");

        render(data);
    }

    @Override
    protected void render(Employee data) {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (data != null && nameLabel != null) {
            bindToEmployee(data);
        }

        if (userIcon == null || deleteIcon == null || nameLabel == null) {
            return;
        }
        getChildren().setAll(userIcon, nameLabel, spacer, deleteIcon);
    }

    @Override
    public void updateItem(Employee e) {
        super.updateItem(e);
        if (e != null) {
            bindToEmployee(e);
        }
        render(e);
    }

    private void bindToEmployee(Employee e) {
        nameLabel.textProperty().unbind();
        nameLabel.textProperty().bind(e.nameProperty());
    }
}
