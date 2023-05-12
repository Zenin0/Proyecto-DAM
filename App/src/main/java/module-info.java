module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;


    opens app to javafx.fxml;
    exports app;
}
