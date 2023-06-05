module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires org.json;
    requires java.desktop;


    opens app to javafx.fxml;
    exports app;
}
