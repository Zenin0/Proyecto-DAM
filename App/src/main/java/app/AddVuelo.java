package app;

import java.sql.*;
import java.text.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

public class AddVuelo {

    AddVuelo() {

    }

    public boolean registrar(String CiudadSalida, String CiudadDestino, int idAvion, String fecha)
            throws ParseException {
        int ciudadSalida = 0;
        int ciudadDestino = 0;
        int idAvionInt;
        int id;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/Manolo_Airlines", "root",
                    "admini");
            String query = "SELECT ID_Ciudad FROM Ciudades WHERE Nombre_Ciudad = ?";
            PreparedStatement checkStatement = con.prepareStatement(query);
            checkStatement.setString(1, CiudadDestino);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                ciudadDestino = resultSet.getInt(1);
            }
            query = "SELECT ID_Ciudad FROM Ciudades WHERE Nombre_Ciudad = ?";
            checkStatement = con.prepareStatement(query);
            checkStatement.setString(1, CiudadSalida);
            resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                ciudadSalida = resultSet.getInt(1);
            }
            idAvionInt = idAvion;
            // Comprobar si el usuario y la contraseña ya existen en la base de datos
            query = "SELECT COUNT(*) FROM Vuelos WHERE Ciudad_Salida = ? AND Ciudad_Destino = ? AND ID_Avion = ?";
            checkStatement = con.prepareStatement(query);
            checkStatement.setInt(1, ciudadSalida);
            checkStatement.setInt(2, ciudadDestino);
            checkStatement.setInt(3, idAvionInt);
            resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Este vuelo ya esta registrado");
                dialog.show();
                return false;

            } else {

                String test = "SELECT max(ID_Vuelo) FROM Vuelos";
                PreparedStatement prst = con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    id = resulttest.getInt(1);

                    // Insert the new record into the database
                    String consulta = "INSERT INTO Vuelos (ID_Vuelo, Ciudad_Salida, Ciudad_Destino, ID_Avion, Fecha_Salida, Creada_Por) VALUES (? , ?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = con.prepareStatement(consulta);
                    insertStatement.setInt(1, id + 1);
                    insertStatement.setInt(2, ciudadSalida);
                    insertStatement.setInt(3, ciudadDestino);
                    insertStatement.setInt(4, idAvionInt);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date date = dateFormat.parse(fecha);
                    insertStatement.setDate(5, new java.sql.Date(date.getTime()));
                    insertStatement.setString(6, GlobalData.userName);
                    insertStatement.executeUpdate();
                    return true;
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
