package com.wilsonsinclair.scheduler.factories;

import com.wilsonsinclair.scheduler.Employee;
import com.wilsonsinclair.scheduler.MainViewController;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;

public  class EmployeeListCellFactory extends MFXListCell<Employee> {
    private final MFXFontIcon userIcon;
    private final MFXFontIcon deleteIcon;

    public EmployeeListCellFactory(MainViewController controller, MFXListView<Employee> listView, Employee data) {
        super(listView, data);

        userIcon = new MFXFontIcon("fas-user", 18);
        deleteIcon = new MFXFontIcon("fas-trash", 18);

        deleteIcon.getStyleClass().add("delete-icon");
        deleteIcon.setOnMouseClicked(event -> {
            controller.confirmEmployeeDeletion();
        });

        userIcon.getStyleClass().add("user-icon");
        render(data);
    }

    @Override
    protected void render(Employee data) {
        super.render(data);
        if (userIcon != null) {
            getChildren().addFirst(userIcon);
        }
        if (deleteIcon != null) {
            getChildren().addLast(deleteIcon);
        }
    }
}
