module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive javafx.graphics;


    opens app to javafx.fxml;
    exports app;
}
