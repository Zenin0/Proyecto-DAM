package app;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyAccountController implements Initializable {


    @FXML
    private Button actualizarButton;

    @FXML
    private PasswordField newPass1Field;

    @FXML
    private PasswordField newPass2Field;

    @FXML
    private TextField newUserNameField;

    @FXML
    private Button volverButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.newUserNameField.setText(GlobalData.userName);
        this.volverButton.setOnAction(event -> {
            try {
                App.setRoot("inicio_user");
            } catch (IOException e) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });
        this.actualizarButton.setOnAction(event -> {
            if (!this.newUserNameField.getText().isEmpty() || !this.newPass2Field.getText().isEmpty() || !this.newPass1Field.getText().isEmpty()) {
                actualizar();
            } else {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Por favor rellene todos los campos");
                dialog.show();
            }

        });
        newUserNameField.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!this.newUserNameField.getText().isEmpty() || !this.newPass2Field.getText().isEmpty() || !this.newPass1Field.getText().isEmpty()) {
                    actualizar();
                } else {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Por favor rellene todos los campos");
                    dialog.show();
                }
            }
        });
        newPass1Field.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!this.newUserNameField.getText().isEmpty() || !this.newPass2Field.getText().isEmpty() || !this.newPass1Field.getText().isEmpty()) {
                    actualizar();
                } else {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Por favor rellene todos los campos");
                    dialog.show();
                }
            }
        });
        newPass2Field.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!this.newUserNameField.getText().isEmpty() || !this.newPass2Field.getText().isEmpty() || !this.newPass1Field.getText().isEmpty()) {
                    actualizar();
                } else {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Por favor rellene todos los campos");
                    dialog.show();
                }
            }
        });
        actualizarButton.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!this.newUserNameField.getText().isEmpty() || !this.newPass2Field.getText().isEmpty() || !this.newPass1Field.getText().isEmpty()) {
                    actualizar();
                } else {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Por favor rellene todos los campos");
                    dialog.show();
                }
            }
        });
    }

    public void actualizar(){
        if (Gestioner.actualizar(GlobalData.userName, this.newUserNameField.getText(), this.newPass1Field.getText(), this.newPass2Field.getText())) {
            try {
                App.setRoot("inicio_user");
            } catch (IOException e) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        }
    }

}


