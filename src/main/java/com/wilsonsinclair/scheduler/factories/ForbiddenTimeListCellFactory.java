package com.wilsonsinclair.scheduler.factories;

import com.wilsonsinclair.scheduler.time.ForbiddenTime;
import com.wilsonsinclair.scheduler.MainViewController;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ForbiddenTimeListCellFactory extends MFXListCell<ForbiddenTime> {
    private final MFXFontIcon deleteIcon;

    public ForbiddenTimeListCellFactory(MainViewController controller, MFXListView<ForbiddenTime> listView, ForbiddenTime data) {
        super(listView, data);

        deleteIcon = new MFXFontIcon("fas-trash", 18);
        deleteIcon.getStyleClass().add("delete-icon");

        deleteIcon.setVisible(false);
        this.setOnMouseEntered(event -> deleteIcon.setVisible(true));
        this.setOnMouseExited(event -> deleteIcon.setVisible(false));

        deleteIcon.setOnMouseClicked(event -> {
            listView.getItems().remove(data);
            controller.saveEmployees();
        });

        render(data);
    }

    @Override
    protected void render(ForbiddenTime data) {
        super.render(data);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        if (deleteIcon != null) {
            getChildren().addAll(spacer, deleteIcon);
        }
    }
}
