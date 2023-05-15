package app;

import java.sql.*;
import java.util.ArrayList;

public class Getter {

    Getter() {
    }

    // Funcion para conseguir el nombre de la Ciudad en funcion de su ID
    public String getNombreCiudad(int IDCiudad) throws SQLException {

        String em = "";
        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        String query = "SELECT Nombre_Ciudad FROM Ciudades WHERE ID_Ciudad = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDCiudad);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getString(1);
        }
        con.close();

        return em;
    }

    // Funcion para conseguir el ID del a avion a parti del ID del vuelo
    public int getIDAvioFromVuelo(int IDVuelo) throws SQLException {

        String em = "";
        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        String query = "SELECT ID_Avion FROM Vuelos WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDVuelo);
        ResultSet rs = checkStatement.executeQuery();
        int IDAvion = -1;
        if (rs.next()) {
            IDAvion = rs.getInt(1);
        }
        con.close();

        return IDAvion;
    }

    // Funcion para conseguir los asientos libre de un Vuelo, a partir de la ID del avion que se usa y su ID del vuelo
    public int getAsientosLibresCant(int IDAvion, int IDVuelo) throws SQLException {

        String em = "";
        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        String query = "SELECT count(*) FROM Reservas WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDVuelo);
        ResultSet rs = checkStatement.executeQuery();
        int asientosOcupados = -1;
        if (rs.next()) {
            asientosOcupados = rs.getInt(1);
        }
        query = "SELECT Capacidad FROM Aviones WHERE ID_Avion = ?";
        checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDAvion);
        rs = checkStatement.executeQuery();
        int asientosTotales = -1;
        if (rs.next()) {
            asientosTotales = rs.getInt(1);
        }
        con.close();

        return asientosTotales - asientosOcupados;
    }


    // Funcion para conseguir los asientos libre de un Vuelo, a partir de la ID del avion que se usa y su ID del vuelo
    public ArrayList<Integer> getAsientosLibres(int IDAvion, int IDVuelo) throws SQLException {
        ArrayList<Integer> asientosOcupados = new ArrayList<>();

        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);

        // Retrieve the occupied seats for the specified flight and avion
        String query = "SELECT Asiento FROM Reservas WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDVuelo);
        ResultSet rs = checkStatement.executeQuery();

        while (rs.next()) {
            int asientoOcupado = rs.getInt("Asiento");
            asientosOcupados.add(asientoOcupado);
        }

        con.close();

        // Retrieve the total capacity of the plane
        int asientosTotales = getCapacidadAvion(IDAvion);

        // Calculate the available seats
        ArrayList<Integer> asientosLibres = new ArrayList<>();
        for (int i = 1; i <= asientosTotales; i++) {
            if (!asientosOcupados.contains(i)) {
                asientosLibres.add(i);
            }
        }

        return asientosLibres;
    }

    public int getCapacidadAvion(int IDAvion) throws SQLException {
        int capacidad = 0;

        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);

        String query = "SELECT Capacidad FROM Aviones WHERE ID_Avion = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, IDAvion);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            capacidad = rs.getInt("Capacidad");
        }

        rs.close();
        statement.close();
        con.close();

        return capacidad;
    }


    // Funcion para conseguir el ID del usuario en base a su Nombre
    public int getUsername(String Username) throws SQLException {

        int em = 0;
        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        String query = "SELECT ID_Usuario FROM Usuarios WHERE Nombre_Usuario = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setString(1, Username);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getInt(1);
        }
        con.close();

        return em;
    }

}
