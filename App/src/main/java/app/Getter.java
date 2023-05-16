package app;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Getter {

    Getter() {
    }

    // Funcion para obtener una ArrayList de las Ciudades
    public ArrayList<String> getlistaCiudadesStrings() throws SQLException {

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
    public ArrayList<String> getlistaAvionesStrings() throws SQLException {

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
    public ArrayList<String> getlistaVuelosStrings() throws SQLException {

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

    // Funcion para obtener una ArrayList de las Reservas de un Usuario
    public ArrayList<String> getListaReservasUser(int IDUser) throws SQLException {

        ArrayList<Integer> reservas = new ArrayList<>();
        ArrayList<String> out = new ArrayList<>();
        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT ID_Reserva FROM Reservas WHERE ID_Usuario = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDUser);
        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            reservas.add(rs.getInt(1));
        }
        for (int IDReserva: reservas) {
            out.add(getReservaInfo(IDReserva));
        }
        con.close();
        return out;
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

    // Funcion para conseguir el nombre del AVion en funcion de su ID
    public String getNombreAvion(int IDAvion) throws SQLException {

        String em = "";
        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        String query = "SELECT Nombre_Avion FROM Aviones WHERE ID_Avion = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDAvion);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getString(1);
        }
        con.close();

        return em;
    }

    // Funcion para conseguir el ID del a avion a parti del ID del vuelo
    public int getIDAvioFromVuelo(int IDVuelo) throws SQLException {

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
    public int getUsernameID(String Username) throws SQLException {

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

    // Funcion para conseguir el Nombre de un Usuario por su ID
    public String getUsernameNombre(int IDUser) throws SQLException {

        String em = "";
        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        String query = "SELECT Nombre_Usuario FROM Usuarios WHERE ID_Usuario = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDUser);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getString(1);
        }
        con.close();

        return em;
    }


    public String getVueloInfo(int IDVuelo) throws SQLException {
        String em = "";
        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        String query = "SELECT Ciudad_Salida, Ciudad_Destino, ID_Avion, Fecha_Salida FROM Vuelos WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDVuelo);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            Date date = rs.getDate(4);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(date);
            em = "Ciudad Saldida: " + getNombreCiudad(rs.getInt(1)) + "\nCiudad Destino: " + getNombreCiudad(rs.getInt(2)) + "\nAvion: " + getNombreAvion(rs.getInt(3)) + "\nFecha Salida: " + formattedDate;
        }
        return em;
    }


    public String getReservaInfo(int IDReserva) throws SQLException {
        String em = "";
        int ID_Usuario = 0, ID_Vuelo = 0, Asiento = 0;
        Connection con = DriverManager.getConnection(GlobalData.DB_URL, GlobalData.DBUSER, GlobalData.DBPASS);
        String query = "SELECT ID_Usuario, ID_Vuelo, Asiento FROM Reservas WHERE ID_Reserva = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, IDReserva);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            ID_Usuario = rs.getInt("ID_Usuario");
            ID_Vuelo = rs.getInt("ID_Vuelo");
            Asiento = rs.getInt("Asiento");
        }
        em += "Nombre de Usuario: " + getUsernameNombre(ID_Usuario) + "\n";
        em += "Asiento: " + Asiento + "\n";
        em += getVueloInfo(ID_Vuelo);
        return em;
    }

}
