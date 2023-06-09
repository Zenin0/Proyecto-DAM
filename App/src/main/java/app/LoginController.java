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
     *
     * @throws IOException Error al cargar el FXML
     */
    @FXML
    private void registerChange() throws IOException {
        App.setRoot("register");
    }


    /**
     * Login de los usuarios
     *
     * @throws IOException  Error al cargar el FXML
     * @throws SQLException Error en la consulta
     */
    private void login() throws IOException, SQLException {
        if (this.usuLog.getText().isEmpty() || this.passLog.getText().isEmpty()) {
            Alert error = new Alert(AlertType.ERROR);
            error.setTitle("Login");
            error.setHeaderText("Rellene todos los campos");
            error.show();
        } else {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            int res = ManoloAirlines.login(this.usuLog.getText(), this.passLog.getText());
            // ? Que tipo de usuario ha hecho login
            if (res == 1) {
                dialog.setTitle("¡Login correcto!");
                dialog.setHeaderText("¡Bienvenido " + GlobalData.userName + "!");
                dialog.show();
                App.setRoot("inicio_admin");
            } else if (res == 0) {
                dialog.setTitle("¡Login correcto!");
                dialog.setHeaderText("¡Bienvenido " + ManoloAirlines.getNombreAndApellidos(ManoloAirlines.getUsernameID(this.usuLog.getText())) + "!");
                dialog.show();
                App.setRoot("inicio_user");
            } else {
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle("Login Incorrecto");
                error.setHeaderText("Inicio de sesión incorrecto");
                error.show();
            }
        }
    }


}
