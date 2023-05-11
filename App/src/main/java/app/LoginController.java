package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Boton que inicia la comprobacion del login
        buttonLog.setOnMouseClicked((event) -> {
            try {
                login();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });
        // Boton para cambiar al modo de registro
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
    }

    // Cambiar a la ventana de registro
    @FXML
    private void registerChange() throws IOException {
        App.setRoot("register");
    }

    // Ejecutar la comprobacion de login
    private void login() throws IOException {

        Alert dialog = new Alert(AlertType.CONFIRMATION);
        if (new Gestioner().login(this.usuLog.getText(), this.passLog.getText()) == 1) {
            dialog.setTitle("Login correcto");
            dialog.setHeaderText("Bienvenido " + this.usuLog.getText());
            dialog.show();
            App.setRoot("inicio_admin");
        } else if (new Gestioner().login(this.usuLog.getText(), this.passLog.getText()) == 0) {
            dialog.setTitle("Login correcto");
            dialog.setHeaderText("Bienvenido " + this.usuLog.getText());
            dialog.show();
            App.setRoot("inicio_user");
        } else {
            dialog.setTitle("ERROR");
            dialog.setHeaderText("Inicio de sesión incorrecto");
            dialog.show();
        }
    }

}
