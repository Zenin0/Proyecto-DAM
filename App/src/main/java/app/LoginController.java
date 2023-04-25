package app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

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

        buttonLog.setOnMouseClicked((event) -> {
            try {
                registrar();
            } catch (IOException e) {
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
    }

    @FXML
    private void registerChange() throws IOException {
        App.setRoot("register");
    }

    private void registrar() throws IOException {
        LoginClass Log = new LoginClass(this.usuLog.getText(), this.passLog.getText());
        if (Log.login() == 1) {
            this.usuLog.setText("");
            this.passLog.setText("");
            App.setRoot("inicio_admin");
        } else {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText("Inicio de sesión incorrecto");
            dialog.show();
        }
    }

}
