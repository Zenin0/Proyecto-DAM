package app;

import java.sql.*;
import java.util.*;

public class Menus {

    private final String DB_URL = "jdbc:mysql://172.17.0.2:3306/Manolo_Airlines";
    private final String USER = "root";
    private final String PASS = "admini";

    public ArrayList<String> listaCiudadesStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<String>();

        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT Nombre_Ciudad FROM Ciudades";
        PreparedStatement checkStatement = con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        String arr = null;
        while (rs.next()) {
            String em = rs.getString("Nombre_Ciudad");
            arr = em.replace("\n", ",");
            out.add(arr);
        }
        con.close();
        return out;
    }

    public ArrayList<String> listaAvionesStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<String>();

        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT ID_Avion,Nombre_Avion,Anyo_Fabricacion,Capacidad FROM Aviones";
        PreparedStatement checkStatement = con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            out.add(rs.getString("ID_Avion") + " - " + rs.getString("Nombre_Avion") + " - "
                    + rs.getString("Capacidad"));
        }
        con.close();
        return out;
    }

    public ArrayList<String> listaVuelosStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<String>();

        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT * FROM Vuelos";
        PreparedStatement checkStatement = con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        String arr = null;
        while (rs.next()) {
            String em = rs.getString("ID_Vuelo") + " - " +rs.getString("Ciudad_Salida") + " - " + rs.getString("Ciudad_Destino") + " - " + rs.getDate("Fecha_Salida");
            arr = em.replace("\n", ",");
            out.add(arr);
        }
        con.close();
        return out;
    }
}
