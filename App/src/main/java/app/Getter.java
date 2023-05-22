package app;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public  class Getter {

    Getter() {
    }


    // Funcion para obtener una ArrayList de las Ciudades
    public static ArrayList<String> getlistaCiudadesStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<>();

        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT Nombre_Ciudad FROM Ciudades";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        String arr;
        while (rs.next()) {
            String em = rs.getString("Nombre_Ciudad");
            arr = em.replace("\n", ",");
            out.add(arr);
        }
        return out;
    }

    // Funcion para obtener una ArrayList de los Aviones
    public static ArrayList<String> getlistaAvionesStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<>();
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT ID_Avion,Nombre_Avion,Capacidad FROM Aviones";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            out.add(rs.getString("ID_Avion") + " - " + rs.getString("Nombre_Avion") + " - "
                    + rs.getString("Capacidad"));
        }
        return out;
    }

    // Funcion para obtener una ArrayList de los Vuelos
    public static ArrayList<String> getlistaVuelosStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<>();

        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT * FROM Vuelos";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        String arr;
        while (rs.next()) {
            String em = rs.getString("ID_Vuelo") + " - " + rs.getString("Ciudad_Salida") + " - " + rs.getString("Ciudad_Destino") + " - " + rs.getDate("Fecha_Salida");
            arr = em.replace("\n", ",");
            out.add(arr);
        }
        return out;
    }

    // Funcion para obtener una ArrayList de las Reservas de un Usuario
    public static ArrayList<String> getListaReservasUser(int IDUser) throws SQLException {

        ArrayList<Integer> reservas = new ArrayList<>();
        ArrayList<String> out = new ArrayList<>();
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT ID_Reserva FROM Reservas WHERE ID_Usuario = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDUser);
        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            reservas.add(rs.getInt(1));
        }
        for (int IDReserva : reservas) {
            out.add(getReservaInfo(IDReserva));
        }
        return out;
    }

    // Funcion para conseguir el nombre de la Ciudad en funcion de su ID
    public static String getNombreCiudad(int IDCiudad) throws SQLException {

        String em = "";
        String query = "SELECT Nombre_Ciudad FROM Ciudades WHERE ID_Ciudad = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDCiudad);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getString(1);
        }

        return em;
    }

    // Funcion para conseguir el nombre del AVion en funcion de su ID
    public static String getNombreAvion(int IDAvion) throws SQLException {

        String em = "";
        String query = "SELECT Nombre_Avion FROM Aviones WHERE ID_Avion = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDAvion);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getString(1);
        }

        return em;
    }

    // Funcion para conseguir el ID del a avion a parti del ID del vuelo
    public static int getIDAvioFromVuelo(int IDVuelo) throws SQLException {

        String query = "SELECT ID_Avion FROM Vuelos WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDVuelo);
        ResultSet rs = checkStatement.executeQuery();
        int IDAvion = -1;
        if (rs.next()) {
            IDAvion = rs.getInt(1);
        }
        return IDAvion;
    }

    // Funcion para conseguir los asientos libre de un Vuelo, a partir de la ID del avion que se usa y su ID del vuelo
    public static int getAsientosLibresCant(int IDAvion, int IDVuelo) throws SQLException {

        String query = "SELECT count(*) FROM Reservas WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDVuelo);
        ResultSet rs = checkStatement.executeQuery();
        int asientosOcupados = -1;
        if (rs.next()) {
            asientosOcupados = rs.getInt(1);
        }
        query = "SELECT Capacidad FROM Aviones WHERE ID_Avion = ?";
        checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDAvion);
        rs = checkStatement.executeQuery();
        int asientosTotales = -1;
        if (rs.next()) {
            asientosTotales = rs.getInt(1);
        }

        return asientosTotales - asientosOcupados;
    }

    // Funcion para conseguir los asientos libre de un Vuelo, a partir de la ID del avion que se usa y su ID del vuelo
    public static ArrayList<Integer> getAsientosLibres(int IDAvion, int IDVuelo) throws SQLException {
        ArrayList<Integer> asientosOcupados = new ArrayList<>();

        // Retrieve the occupied seats for the specified flight and avion
        String query = "SELECT Asiento FROM Reservas WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDVuelo);
        ResultSet rs = checkStatement.executeQuery();

        while (rs.next()) {
            int asientoOcupado = rs.getInt("Asiento");
            asientosOcupados.add(asientoOcupado);
        }

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

    public static int getCapacidadAvion(int IDAvion) throws SQLException {
        int capacidad = 0;

        String query = "SELECT Capacidad FROM Aviones WHERE ID_Avion = ?";
        PreparedStatement statement = App.con.prepareStatement(query);
        statement.setInt(1, IDAvion);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            capacidad = rs.getInt("Capacidad");
        }
        return capacidad;
    }

    // Funcion para conseguir el ID del usuario en base a su Nombre
    public static int getUsernameID(String Username) throws SQLException {

        int em = 0;
        String query = "SELECT ID_Usuario FROM Usuarios WHERE Nombre_Usuario = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setString(1, Username);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getInt(1);
        }

        return em;
    }

    // Funcion para conseguir el Nombre de un Usuario por su ID
    public static String getUsernameNombre(int IDUser) throws SQLException {

        String em = "";
        String query = "SELECT Nombre_Usuario FROM Usuarios WHERE ID_Usuario = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDUser);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getString(1);
        }

        return em;
    }

    public static String getVueloInfo(int IDVuelo) throws SQLException {
        String em = "";
        String query = "SELECT Ciudad_Salida, Ciudad_Destino, ID_Avion, Fecha_Salida FROM Vuelos WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDVuelo);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            Date date = rs.getDate(4);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(date);
            em = "Ciudad de Saldida: " + getNombreCiudad(rs.getInt(1)) + " | Ciudad de Destino: " + getNombreCiudad(rs.getInt(2)) + "\nAvión: " + getNombreAvion(rs.getInt(3)) + "\nFecha Salida: " + formattedDate;
        }
        return em;
    }

    public static String getReservaInfo(int IDReserva) throws SQLException {
        String em = "";
        int ID_Reserva = 0, ID_Usuario = 0, ID_Vuelo = 0, Asiento = 0;
        String query = "SELECT ID_Reserva, ID_Usuario, ID_Vuelo, Asiento FROM Reservas WHERE ID_Reserva = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDReserva);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            ID_Reserva = rs.getInt("ID_Reserva");
            ID_Usuario = rs.getInt("ID_Usuario");
            ID_Vuelo = rs.getInt("ID_Vuelo");
            Asiento = rs.getInt("Asiento");
        }
        em += "Numero de la Reserva: " + ID_Reserva + "\n";
        em += "Nombre de Usuario: " + getUsernameNombre(ID_Usuario) + "\n";
        em += "Asiento asignado: " + Asiento + "\n";
        em += getVueloInfo(ID_Vuelo);
        return em;
    }


    public static int getNumCols(int numSeats) {
        int maxCols = 40; // Maximum number of columns
        int numCols = 4; // Default number of columns
        while (numCols <= maxCols) {
            int numRows = (int) Math.ceil((double) numSeats / numCols);
            if (numRows <= 8) { // Maximum number of rows
                return numCols;
            }
            numCols++;
        }
        return maxCols; // Return maximum number of columns if no suitable number is found
    }

    public static int getNumRows(int numSeats, int numCols) {
        int maxRows = 8; // Maximum number of rows
        int numRows = (int) Math.ceil((double) numSeats / numCols);
        if (numRows > maxRows) {
            numRows = maxRows;
        }
        return numRows;
    }

}
