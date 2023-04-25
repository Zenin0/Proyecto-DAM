package app;

import java.sql.*;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

public class RegistroClass {

    private String usuario;
    private String pass1;
    private String pass2;
    private boolean admin;
    private int id;

    RegistroClass(String Usuario, String Pass1, String Pass2, boolean admin) {
        this.usuario = Usuario;
        this.pass1 = Pass1;
        this.pass2 = Pass2;
        this.admin = admin;
    }

    public boolean registrar() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/Manolo_Airlines", "root",
                    "admini");
            if (!this.pass1.equals(this.pass2)) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Las contraseñas no coinciden");
                dialog.show();
                return false;
            } else {
                String username = this.usuario;
                MD5_hashing md5 = new MD5_hashing(this.pass1);
                Boolean admin = this.admin;

                // Comprobar si el usuario y la contraseña ya existen en la base de datos
                String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario=? AND Pass=?";
                PreparedStatement checkStatement = con.prepareStatement(query);
                checkStatement.setString(1, username);
                checkStatement.setString(2, md5.getMd5());
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Este Usuario y esta contraseña ya existen");
                    dialog.show();
                    return false;

                } else {

                    if (admin) {
                        PasswordDialog adminck = new PasswordDialog();
                        Optional<String> result = adminck.showAndWait();
                        if (!new MD5_hashing(result.get()).getMd5().equals(new MD5_hashing("root").getMd5())) {
                            Alert dialog = new Alert(AlertType.ERROR);
                            dialog.setTitle("ERROR");
                            dialog.setHeaderText("Contraseña de administrador incorrecta");
                            dialog.show();
                            return false;
                        }
                    }
                    
                    String test = "SELECT max(id) FROM Usuarios";
                    PreparedStatement prst = con.prepareStatement(test);
                    ResultSet resulttest = prst.executeQuery();
                    if (resulttest.next()) {
                        this.id = resulttest.getInt(1);

                        // Insert the new record into the database
                        String consulta = "INSERT INTO Usuarios (id, Nombre_Usuario, Pass, is_Admin) VALUES (? , ?, ?, ?)";
                        PreparedStatement insertStatement = con.prepareStatement(consulta);
                        insertStatement.setInt(1, this.id + 1);
                        insertStatement.setString(2, username);
                        insertStatement.setString(3, md5.getMd5());
                        insertStatement.setBoolean(4, admin);
                        insertStatement.executeUpdate();

                        Alert dialog = new Alert(AlertType.CONFIRMATION);
                        dialog.setTitle("Usuario");
                        dialog.setHeaderText("Usuario creado correctamente");
                        dialog.show();
                        return true;
                    }

                }
            }
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

}
