module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires org.json;


    opens app to javafx.fxml;
    exports app;
}
