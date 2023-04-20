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

public class RegisterController implements Initializable {
    // Items
    @FXML
    private Button buttonReg;

    @FXML
    private Button buttonlogCh;

    @FXML
    private PasswordField passReg1;

    @FXML
    private PasswordField passReg2;

    @FXML
    private TextField usuReg;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttonReg.setOnMouseClicked((event) -> registrar());
        buttonlogCh.setOnMouseClicked((event) -> {
            try {
                loginChange();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });
    }

    @FXML
    private void loginChange() throws IOException {
        App.setRoot("login");
    }

    private void registrar() {
        try {
        Connection con = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/Manolo_Airlines", "root", "admini");
        if (!this.passReg1.getText().equals(this.passReg2.getText())) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText("Las contraseñas no coinciden");
            dialog.show();
        } else {
            String username = this.usuReg.getText();
            String pass = this.passReg1.getText();
            // String de la consulta
            String consulta = "INSERT INTO `Usuarios` (id, Nombre_Usuario, Pass, Tipo) VALUES ('1','" + username + "', '" + pass + "', 'admin');";
            // Preparar la consulta
            PreparedStatement statement = con.prepareStatement(consulta);
            statement.execute();
        }
    } catch (SQLException e) {
        Alert dialog = new Alert(AlertType.ERROR);
        dialog.setTitle("ERROR");
        dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
        dialog.show();
    }

    }

}
