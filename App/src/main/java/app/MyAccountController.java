package app;

import javafx.fxml.*;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class MyAccountController implements Initializable {


    @FXML
    private Button actualizarButton;

    @FXML
    private ToggleButton hidePass1Button;

    @FXML
    private ToggleButton hidePass2Button;

    @FXML
    private PasswordField newPass1Field;

    @FXML
    private PasswordField newPass2Field;

    @FXML
    private TextField newUserNameField;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private Button volverButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.newUserNameField.setText(GlobalData.userName);
        this.hidePass1Button.setOnAction(event -> {
            if (hidePass1Button.isSelected()) {
                this.textField1.setText(this.newPass1Field.getText());
                this.newPass1Field.setVisible(false);
                this.textField1.setVisible(true);

            }
            else{
                this.newPass1Field.setText(this.textField1.getText());
                this.newPass1Field.setVisible(true);
                this.textField1.setVisible(false);

            }
        });
        this.hidePass2Button.setOnAction(event -> {
            if (hidePass2Button.isSelected()) {
                this.textField2.setText(this.newPass2Field.getText());
                this.newPass2Field.setVisible(false);
                this.textField2.setVisible(true);

            }
            else{
                this.newPass2Field.setText(this.textField2.getText());
                this.newPass2Field.setVisible(true);
                this.textField2.setVisible(false);

            }
        });
    }

}


