package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Inicio del Programa
 */
public class App extends Application {

    public static Connection con;
    private static Scene scene;

    static {
        try {
            con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        } catch (SQLException e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText(e.getMessage());
            dialog.show();
        }
    }

    public App() {
    }

    /**
     * Seleccionar el FXML de inicio
     *
     * @param fxml Nombre del fichero FXML
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }


    /**
     * Cargar el FXML
     *
     * @param fxml Nombre del Fichero
     * @return Parent
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }


    /**
     * Comenzar el programa
     *
     * @param stage Ventana
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 1080, 700);
        stage.setTitle("Manolo Airlines");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("css/logoMA.png"))));
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        // Shutdown hook
        stage.setOnCloseRequest(event -> {
            try {
                App.con.close();
            } catch (SQLException e) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

    }

}