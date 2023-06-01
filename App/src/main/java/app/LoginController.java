package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controlador del FXML login
 */
public class LoginController implements Initializable {
    // Items
    @FXML
    private Button buttonLog;

    @FXML
    private Button buttonRegCh;

    @FXML
    private PasswordField passLog;

    @FXML
    private TextField usuLog;

    /**
     * Inicializar la ventana
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttonLog.setOnMouseClicked((event) -> {
            try {
                login();
            } catch (IOException | SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

        buttonRegCh.setOnMouseClicked((event) -> {
            try {
                registerChange();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

        passLog.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    login();
                } catch (IOException | SQLException e) {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText(e.getMessage());
                    dialog.show();
                }
            }
        });

        usuLog.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    login();
                } catch (IOException | SQLException e) {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText(e.getMessage());
                    dialog.show();
                }
            }
        });

        buttonLog.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    login();
                } catch (IOException | SQLException e) {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText(e.getMessage());
                    dialog.show();
                }
            }
        });
        buttonRegCh.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    registerChange();
                } catch (IOException e) {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText(e.getMessage());
                    dialog.show();
                }
            }
        });

    }

    /**
     * Cambiar a la ventana de registro
     */
    @FXML
    private void registerChange() throws IOException {
        App.setRoot("register");
    }


    /**
     * Login de los usuarios
     *
     * @see Gestioner#login(String, String)
     */
    private void login() throws IOException, SQLException {

        Alert dialog = new Alert(AlertType.CONFIRMATION);
        if (Gestioner.login(this.usuLog.getText(), this.passLog.getText()) == 1) {
            dialog.setTitle("¡Login correcto!");
            dialog.setHeaderText("¡Bienvenido " + Getter.getNombreAndApellidos(Getter.getUsernameID(this.usuLog.getText())) + "!");
            dialog.show();
            App.setRoot("inicio_admin");
        } else if (Gestioner.login(this.usuLog.getText(), this.passLog.getText()) == 0) {
            dialog.setTitle("¡Login correcto!");
            dialog.setHeaderText("¡Bienvenido " + Getter.getNombreAndApellidos(Getter.getUsernameID(this.usuLog.getText())) + "!");
            dialog.show();
            App.setRoot("inicio_user");
        } else {
            dialog.setTitle("Login Incorrecto");
            dialog.setHeaderText("Inicio de sesión incorrecto");
            dialog.show();
        }
    }


}
