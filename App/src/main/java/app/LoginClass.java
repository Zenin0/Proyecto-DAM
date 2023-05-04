package app;

import java.sql.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

public class LoginClass {

    private String usuario;
    private String pass;

    LoginClass(String Usuario, String Pass1) {
        this.usuario = Usuario;
        this.pass = Pass1;
    }

    // Guía returns
    // return 1 == Acceso admin
    // return 0 == Acceso NO admin
    // return -1, Error login
    public int login() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/Manolo_Airlines", "root",
                    "admini");

            String username = this.usuario;
            MD5_Hashing md5 = new MD5_Hashing(this.pass);

            // Comprobar si el usuario y la contraseña ya existen en la base de datos
            String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
            PreparedStatement checkStatement = conn.prepareStatement(query);
            checkStatement.setString(1, username);
            checkStatement.setString(2, md5.getMd5());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // Comprobar si el usuario es admin o no
                query = "SELECT Is_Admin FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
                checkStatement = conn.prepareStatement(query);
                checkStatement.setString(1, username);
                checkStatement.setString(2, md5.getMd5());
                resultSet = checkStatement.executeQuery();
                resultSet.next();
                Alert dialog = new Alert(AlertType.CONFIRMATION);
                dialog.setTitle("Login correcto");
                dialog.setHeaderText("Acceso Concedido");
                dialog.show();

                if (resultSet.getInt(1) == 1) {
                    conn.close();
                    GlobalData.userName = username;
                    return 1;
                } else if (resultSet.getInt(1) == 0) {
                    conn.close();
                    GlobalData.userName = username;
                    return 0;
                }

            }
            conn.close();

        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
            return -1;
        }
        return -1;

    }
}
