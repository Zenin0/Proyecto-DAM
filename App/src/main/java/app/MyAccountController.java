package app;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        try {
            loadImagen();
        } catch (SQLException e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText(e.getMessage());
            dialog.show();
        }
        */
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
        imagenUsuario.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            Stage stage = (Stage) imagenUsuario.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {
                    Image image = new Image(selectedFile.toURI().toString());
                    imagenUsuario.setImage(image);
                    applyCircularMask(imagenUsuario);
                } catch (Exception e) {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Failed to load image: " + e.getMessage());
                    dialog.show();
                }
            }
        });
    }

    private void loadImagen() throws SQLException {
        String query = "SELECT Image FROM Usuarios WHERE Nombre_Usuario = ?";

        PreparedStatement statement = App.con.prepareStatement(query);
        statement.setString(1, GlobalData.userName);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            byte[] imageBytes = resultSet.getBytes("Image");
            if (imageBytes != null && imageBytes.length > 0) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                Image image = new Image(inputStream);
                this.imagenUsuario.setImage(image);
            }
        }
    }



    public void actualizar() {
        if (actualizar(GlobalData.userName, this.newUserNameField.getText(), this.newPass1Field.getText(), this.newPass2Field.getText(), this.imagenUsuario.getImage())) {
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

    private void applyCircularMask(ImageView imageView) {
        Circle clip = new Circle();
        clip.setCenterX(imageView.getFitWidth() / 2);
        clip.setCenterY(imageView.getFitHeight() / 2);
        clip.setRadius(imageView.getFitWidth() / 2);
        imageView.setClip(clip);
    }

    public static boolean actualizar(String username, String newUsername, String pass1, String pass2, javafx.scene.image.Image PFP) {
        try {
            if (!pass1.equals(pass2)) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setHeaderText("ERROR");
                dialog.setContentText("Las contraseñas no coinciden");
                dialog.show();
            } else {
                MD5Hasher md5 = new MD5Hasher(pass1);
                String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ?";
                PreparedStatement checkStatement = App.con.prepareStatement(query);
                checkStatement.setString(1, username);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    String updateQuery = "UPDATE Usuarios SET Nombre_Usuario = ?, Pass = ?, Image = ? WHERE Nombre_Usuario = ?";
                    PreparedStatement updateStatement = App.con.prepareStatement(updateQuery);
                    updateStatement.setString(1, newUsername);
                    updateStatement.setString(2, md5.getMd5());
                    updateStatement.setString(3, convertImageToBase64(PFP));
                    updateStatement.setString(4, username);
                    updateStatement.executeUpdate();

                    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                    dialog.setTitle("Usuario");
                    dialog.setHeaderText("Usuario actualizado correctamente");
                    dialog.show();
                    return true;
                } else {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setHeaderText("ERROR");
                    dialog.setHeaderText("Este usuario no existe");
                    dialog.show();
                    return false;
                }
            }
        } catch (SQLException e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        } catch (IOException e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText(e.getMessage());
            dialog.show();
        }
        return false;
    }

    private static String convertImageToBase64(javafx.scene.image.Image image) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(
                (int) image.getWidth(),
                (int) image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
