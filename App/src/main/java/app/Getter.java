package app;

import java.sql.*;

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

        rs.close();
        checkStatement.close();
        con.close();

        return IDAvion;
    }

    // Funcion para conseguir los asientos libre de un Vuelo, a partir de la ID del avion que se usa y su ID del vuelo
    public int getAsientosLibres(int IDAvion, int IDVuelo) throws SQLException {

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

}
