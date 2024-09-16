module com.wilsonsinclair.scheduler {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires com.google.common;
    requires org.controlsfx.controls;

    opens com.wilsonsinclair.scheduler to javafx.fxml;
    opens com.wilsonsinclair.scheduler.time to javafx.fxml;

    exports com.wilsonsinclair.scheduler;
    exports com.wilsonsinclair.scheduler.time;
}