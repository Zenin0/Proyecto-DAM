package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador del FXML register
 */
public class RegisterController implements Initializable {

    // Items
    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private Button buttonReg;

    @FXML
    private Button buttonlogCh;

    @FXML
    private PasswordField passReg1;

    @FXML
    private PasswordField passReg2;

    @FXML
    private TextField usuApellidos;

    @FXML
    private TextField usuNombre;

    @FXML
    private TextField usuReg;


    /**
     * Inicializar la ventana
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Boton que inicia la funcion de registro
        buttonReg.setOnMouseClicked((event) -> registrar());
        // Boton para cambiar a la ventana de login
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

    /**
     * Funcion para cambiar a la ventana de login
     */
    @FXML
    private void loginChange() throws IOException {
        App.setRoot("login");
    }

    /**
     * Funcion para registrar un usuario
     *
     * @see Gestioner#registrar(String,String,String, String, String, boolean)
     */
    private void registrar() {

        if (Gestioner.registrar(this.usuReg.getText(), this.usuNombre.getText(), this.usuApellidos.getText(), this.passReg1.getText(), this.passReg2.getText(), this.adminCheckBox.isSelected())) {
            this.usuReg.setText("");
            this.usuApellidos.setText("");
            this.usuApellidos.setText("");
            this.passReg1.setText("");
            this.passReg2.setText("");
        }
    }

}
