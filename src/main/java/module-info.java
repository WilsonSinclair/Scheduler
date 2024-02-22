module com.wilsonsinclair.scheduler {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires com.google.common;

    opens com.wilsonsinclair.scheduler to javafx.fxml;
    exports com.wilsonsinclair.scheduler;
}