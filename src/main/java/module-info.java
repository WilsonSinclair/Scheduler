module com.wilsonsinclair.scheduler {

    requires com.dlsc.formsfx;
    requires com.google.common;
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires MaterialFX;
    requires ch.qos.logback.classic;
    requires org.slf4j;

    opens com.wilsonsinclair.scheduler to javafx.fxml, com.google.gson;
    opens com.wilsonsinclair.scheduler.time to javafx.fxml;

    exports com.wilsonsinclair.scheduler;
    exports com.wilsonsinclair.scheduler.time;
}