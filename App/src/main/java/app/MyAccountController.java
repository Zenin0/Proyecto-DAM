package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MyAccountController implements Initializable {

    @FXML
    private Button actualizarButton;

    @FXML
    private ImageView imagenUsuario;

    @FXML
    private PasswordField newPass1Field;

    @FXML
    private PasswordField newPass2Field;

    @FXML
    private TextField newUserNameField;

    @FXML
    private Button volverButton;

    private static File selectedImageFile;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadImagen();
        } catch (SQLException e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText(e.getMessage());
            dialog.show();
        }

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
            if (!this.newUserNameField.getText().isEmpty()) {
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
                if (!this.newUserNameField.getText().isEmpty()) {
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
                if (!this.newUserNameField.getText().isEmpty()) {
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
                if (!this.newUserNameField.getText().isEmpty()) {
                    actualizar();
                } else {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Por favor rellene todos los campos");
                    dialog.show();
                }
            }
        });
        imagenUsuario.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecciona una imagen de perfil");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png")
            );

            Stage stage = (Stage) imagenUsuario.getScene().getWindow();
            selectedImageFile = fileChooser.showOpenDialog(stage);

            if (selectedImageFile != null) {
                try {
                    Image image = new Image(selectedImageFile.toURI().toString());
                    imagenUsuario.setImage(image);
                    applyCircularMask(imagenUsuario);
                } catch (Exception e) {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Fallo al cargar la imagen: " + e.getMessage());
                    dialog.show();
                }
            }
        });

    }

    private void loadImagen() throws SQLException {
        String query = "SELECT Image FROM Usuarios WHERE Nombre_Usuario = ?";
        PreparedStatement statement = App.con.prepareStatement(query);
        statement.setString(1, GlobalData.userName);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            InputStream imageFile = rs.getBinaryStream("Image");
            if (imageFile != null) {
                imagenUsuario.setImage(new Image(imageFile));
                applyCircularMask(imagenUsuario);
            } else {
                imagenUsuario.setImage(new Image("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/user.png"));
            }
        }
    }



    public void actualizar() {
        String newUsername = this.newUserNameField.getText();
        String pass1 = this.newPass1Field.getText();
        String pass2 = this.newPass2Field.getText();

        if (!newUsername.isEmpty()) {
            if (pass1.equals(pass2)) {
                if (Gestioner.actualizar(GlobalData.userName, newUsername, pass1, selectedImageFile)) {
                    try {
                        App.setRoot("inicio_user");
                    } catch (IOException e) {
                        Alert dialog = new Alert(Alert.AlertType.ERROR);
                        dialog.setTitle("ERROR");
                        dialog.setHeaderText(e.getMessage());
                        dialog.show();
                    }
                }
            } else {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Las contraseñas no coinciden");
                dialog.show();
            }
        } else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText("Por favor rellene todos los campos");
            dialog.show();
        }
    }



    private void applyCircularMask(ImageView imageView) {
        Circle clip = new Circle();
        clip.setCenterX(imageView.getFitWidth() / 2);
        clip.setCenterY(imageView.getFitHeight() / 2);
        clip.setRadius(imageView.getFitWidth() / 2);
        imageView.setClip(clip);
    }


}
