package app;

import java.sql.*;
import java.util.ArrayList;

// Objecto que devuelve listar de Strings que se usan en los Menus
public class Menus {

    // Funcion para obtener una ArrayList de las Ciudades
    public ArrayList<String> listaCiudadesStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<>();

        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT Nombre_Ciudad FROM Ciudades";
        PreparedStatement checkStatement = con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        String arr;
        while (rs.next()) {
            String em = rs.getString("Nombre_Ciudad");
            arr = em.replace("\n", ",");
            out.add(arr);
        }
        con.close();
        return out;
    }

    // Funcion para obtener una ArrayList de los Aviones
    public ArrayList<String> listaAvionesStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<>();

        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
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

    // Funcion para obtener una ArrayList de los Vuelos
    public ArrayList<String> listaVuelosStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<>();

        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT * FROM Vuelos";
        PreparedStatement checkStatement = con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        String arr;
        while (rs.next()) {
            String em = rs.getString("ID_Vuelo") + " - " + rs.getString("Ciudad_Salida") + " - " + rs.getString("Ciudad_Destino") + " - " + rs.getDate("Fecha_Salida");
            arr = em.replace("\n", ",");
            out.add(arr);
        }
        con.close();
        return out;
    }
}
