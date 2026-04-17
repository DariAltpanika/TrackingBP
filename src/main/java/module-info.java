module com.trackingbp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

    opens com.trackingbp to javafx.fxml;
    opens com.trackingbp.controller to javafx.fxml;
    opens com.trackingbp.model to com.fasterxml.jackson.databind;  // ← ДОБАВЬТЕ

    exports com.trackingbp;
}