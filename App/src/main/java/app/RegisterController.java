package app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;

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

        RegistroClass Reg = new RegistroClass();
        if (Reg.registrar(this.usuReg.getText(), this.passReg1.getText(), this.passReg2.getText(),
                this.adminCheckBox.isSelected())) {
            this.usuReg.setText("");
            this.passReg1.setText("");
            this.passReg2.setText("");
        }
    }

    public void clear() {
        this.usuReg.setText("");
        this.passReg1.setText("");
        this.passReg2.setText("");
    }

}
