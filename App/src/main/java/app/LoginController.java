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
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

}
