package app;

import java.sql.*;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

public class RegistroClass {

    RegistroClass() {

    }

    public boolean registrar(String Usuario, String Pass1, String Pass2, boolean admin) {
        int id;
        try {
            String DB_URL = "jdbc:mysql://172.17.0.2:3306/Manolo_Airlines";
            String USER = "root";
            String PASS = "admini";
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            // Comprobar que las contraseñas coincidan
            if (!Pass1.equals(Pass2)) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Las contraseñas no coinciden");
                dialog.show();
                return false;
            } else {
                MD5Hasher md5 = new MD5Hasher(Pass1);

                // Comprobar si el usuario y la contraseña ya existen en la base de datos
                String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
                PreparedStatement checkStatement = con.prepareStatement(query);
                checkStatement.setString(1, Usuario);
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
                        PassDialog adminck = new PassDialog();
                        Optional<String> result = adminck.showAndWait();
                        if (!new MD5Hasher(result.get()).getMd5().equals(new MD5Hasher("root").getMd5())) {
                            Alert dialog = new Alert(AlertType.ERROR);
                            dialog.setTitle("ERROR");
                            dialog.setHeaderText("Contraseña de administrador incorrecta");
                            dialog.show();
                            return false;
                        }
                    }

                    String test = "SELECT max(ID_Usuario) FROM Usuarios";
                    PreparedStatement prst = con.prepareStatement(test);
                    ResultSet resulttest = prst.executeQuery();
                    if (resulttest.next()) {
                        id = resulttest.getInt(1);

                        // Insert the new record into the database
                        String consulta = "INSERT INTO Usuarios (ID_Usuario, Nombre_Usuario, Pass, is_Admin) VALUES (? , ?, ?, ?)";
                        PreparedStatement insertStatement = con.prepareStatement(consulta);
                        insertStatement.setInt(1, id + 1);
                        insertStatement.setString(2, Usuario);
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
            con.close();

        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

}
