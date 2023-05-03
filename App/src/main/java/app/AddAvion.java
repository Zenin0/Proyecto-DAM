package app;

import java.sql.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

public class AddAvion {

    AddAvion() {

    }

    public boolean registrar(String nombreAvion, int anyoFabricacion, int capacidad) {
        try {
            int id;
            Connection con = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/Manolo_Airlines", "root",
                    "admini");
            // Comprobar si el usuario y la contraseña ya existen en la base de datos
            String query = "SELECT COUNT(*) FROM Aviones WHERE Nombre_Avion = ? AND Anyo_Fabricacion = ?";
            PreparedStatement checkStatement = con.prepareStatement(query);
            checkStatement.setString(1, nombreAvion);
            checkStatement.setInt(2, anyoFabricacion);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Este avión ya esta registrado");
                dialog.show();
                return false;

            } else {

                String test = "SELECT max(ID_Avion) FROM Aviones";
                PreparedStatement prst = con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    id = resulttest.getInt(1);

                    // Insert the new record into the database
                    String consulta = "INSERT INTO Aviones (ID_Avion, Nombre_Avion, Anyo_Fabricacion, capacidad) VALUES (? , ?, ?, ?)";
                    PreparedStatement insertStatement = con.prepareStatement(consulta);
                    insertStatement.setInt(1, id + 1);
                    insertStatement.setString(2, nombreAvion);
                    insertStatement.setInt(3, anyoFabricacion);
                    insertStatement.setInt(4, capacidad);
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
