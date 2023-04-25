package app;

import java.sql.*;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

public class AddCiudad {

    private String ciudad;
    private String pais;
    private int id;

    AddCiudad(String Ciudad, String Pais) {
        this.ciudad = Ciudad;
        this.pais = Pais;
    }

    public boolean registrar() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/Manolo_Airlines", "root",
                    "admini");

            // Comprobar si el usuario y la contraseña ya existen en la base de datos
            String query = "SELECT COUNT(*) FROM Ciudades WHERE Nombre_Ciudad = ? AND Pais = ?";
            PreparedStatement checkStatement = con.prepareStatement(query);
            checkStatement.setString(1, this.ciudad);
            checkStatement.setString(2, this.pais);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Esta ciudad ya esta registrada");
                dialog.show();
                return false;

            } else {

                String test = "SELECT max(ID_Ciudad) FROM Ciudades";
                PreparedStatement prst = con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    this.id = resulttest.getInt(1);

                    // Insert the new record into the database
                    String consulta = "INSERT INTO Ciudades (ID_Ciudad, Nombre_Ciudad, Pais) VALUES (? , ?, ?)";
                    PreparedStatement insertStatement = con.prepareStatement(consulta);
                    insertStatement.setInt(1, this.id + 1);
                    insertStatement.setString(2, this.ciudad);
                    insertStatement.setString(3, this.pais);
                    insertStatement.executeUpdate();
                    return true;
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
