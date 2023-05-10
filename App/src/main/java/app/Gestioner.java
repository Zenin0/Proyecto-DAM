package app;

import java.sql.*;
import java.text.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;

public class Gestioner {

    private final String DB_URL = "jdbc:mysql://172.17.0.2:3306/Manolo_Airlines";
    private final String USER = "root";
    private final String PASS = "admini";

    public Gestioner() {

    }

    public boolean registrarAvion(String nombreAvion, int anyoFabricacion, int capacidad) {
        try {
            int id;
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
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

            con.close();
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    public boolean registrarCiudad(String Ciudad, String Pais) {
        int id;
        String ciudad = Ciudad.substring(0, 1).toUpperCase() + Ciudad.substring(1).toLowerCase();
        String pais = Pais.substring(0, 1).toUpperCase() + Pais.substring(1).toLowerCase();

        try {
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "SELECT COUNT(*) FROM Ciudades WHERE Nombre_Ciudad = ? AND Pais = ?";
            PreparedStatement checkStatement = con.prepareStatement(query);
            checkStatement.setString(1, ciudad);
            checkStatement.setString(2, pais);
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
                    id = resulttest.getInt(1);

                    String consulta = "INSERT INTO Ciudades (ID_Ciudad, Nombre_Ciudad, Pais) VALUES (? , ?, ?)";
                    PreparedStatement insertStatement = con.prepareStatement(consulta);
                    insertStatement.setInt(1, id + 1);
                    insertStatement.setString(2, ciudad);
                    insertStatement.setString(3, pais);
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

    public boolean registrarVuelo(String CiudadSalida, String CiudadDestino, int idAvion, String fecha)
            throws ParseException {
        int ciudadSalida = 0;
        int ciudadDestino = 0;
        int idAvionInt;
        int id;
        try {
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
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

                    String consulta = "INSERT INTO Vuelos (ID_Vuelo, Ciudad_Salida, Ciudad_Destino, ID_Avion, Fecha_Salida, Creada) VALUES (? , ?, ?, ?, ?, ?)";
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

    public boolean eliminarVuelo(int ID) {
        try {
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "DELETE FROM Vuelos WHERE ID_Vuelo = ?";
            PreparedStatement checkStatement = con.prepareStatement(query);
            checkStatement.setInt(1, ID);
            checkStatement.executeUpdate();
            con.close();
            return true;
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }
}
