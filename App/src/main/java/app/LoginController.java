package app;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    private Button buttonReg;

    @FXML
    private PasswordField pasLog;

    @FXML
    private PasswordField passReg1;

    @FXML
    private PasswordField passReg2;

    @FXML
    private TextField usuLog;

    @FXML
    private TextField usuReg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Botones
        buttonLog.setOnMouseClicked((event) -> {
            try {
                switchToSecondary();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage() + "\n" + e.getCause());
                dialog.show();
            }
        });

        buttonReg.setOnMouseClicked((event) -> registrar());
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    private void registrar() {
        try {
        Connection con = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/users", "root", "admini");
        if (this.passReg1.getText().equals(this.passReg2.getText())) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText("Las contraseñas no coinciden");
            dialog.show();
        } else {
            String username = this.usuReg.getText();
            String pass = this.passReg1.getText();
        }
    } catch (SQLException e) {
        Alert dialog = new Alert(AlertType.ERROR);
        dialog.setTitle("ERROR");
        dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
        dialog.show();
    }

    }

}
