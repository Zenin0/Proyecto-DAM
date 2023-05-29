package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Obtener datos de la Base de Datos
 */
public class Getter {

    /**
     * Construcor de la clase
     */
    Getter() {
    }

    /**
     * Obtener una lista de ciuades
     *
     * @return una ArrayList de las ciudades
     */
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

    /**
     * Obtener una lista de aviones
     *
     * @return una ArrayList de los Aviones
     */
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

    /**
     * Obtener una lista de los vuelos
     *
     * @return una ArrayList de los vuelos
     */
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

    /**
     * Obtener una lista de las Reservas de un Usuario
     *
     * @param IDUser ID del usuario
     * @return ArrayList de las reservas de un usuario
     */
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

    /**
     * Obtener el nombre de una ciudad en base a su ID
     *
     * @param IDCiudad ID de la ciudad
     * @return Nombre de la ciudad
     */
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

    /**
     * Obtener la ID de la ciudad en base a su nombre
     *
     * @param Nombre Nombre de la ciudad
     * @return ID de la ciudad
     */
    public static int getIDCiudad(String Nombre) throws SQLException {

        String query = "SELECT ID_Ciudad FROM Ciudades WHERE Nombre_Ciudad = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setString(1, Nombre);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    /**
     * Obtener la ID de la ciudad en base a su nombre
     *
     * @param IDReserva ID de la reserva
     * @return ID del vuelo
     */
    public static int getIDVueloFromIDReserva(int IDReserva) throws SQLException {

        String query = "SELECT ID_Vuelo FROM Reservas WHERE ID_Reserva = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDReserva);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    /**
     * Obtener el nombre del avion en base de su ID
     *
     * @param IDAvion ID del avión
     * @return nombe del avión
     */
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

    /**
     * Obtener ID del avión en base a la ID del vuelo
     *
     * @param IDVuelo ID del vuelo
     * @return ID del avión
     */
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

    /**
     * Obtener cantidad de los asientos libres de un Vuelo
     *
     * @param IDAvion ID del avión
     * @param IDVuelo ID del vuelo
     * @return Cantidad de asientos libres
     */
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

    /**
     * Obtener asientos libres
     *
     * @param IDAvion ID del avión
     * @param IDVuelo ID del vuelo
     * @return Arraylist de numeros con los asientos libres
     */
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

    /**
     * Obtener la capacidad del avión
     *
     * @param IDAvion ID del avion
     * @return Capaciad del avion
     */
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

    /**
     * Obtener el ID de un usuario en base a su nombre
     *
     * @param Username Nombre del usuario
     * @return ID del usuario
     */
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


    /**
     * Obtener el nombre de un usuario en base a si ID
     *
     * @param IDUser ID del usuario
     * @return Nombre del usuario
     */
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

    /**
     * Obtener el nombre y los apellidos de un usuario en base a su ID
     *
     * @param IDUser ID del usuario
     * @return Nombre del usuario
     */
    public static String getNombreAndApellidos(int IDUser) throws SQLException {

        String em = "";
        String query = "SELECT Nombre, Apellidos FROM Usuarios WHERE ID_Usuario = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDUser);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getString(1) + " ";
            em += rs.getString(2);
        }

        return em;
    }

    /**
     * Obtener una String de la informacion del vuelo
     *
     * @param IDVuelo ID del Vuelo
     * @return Informacion del Vuelo
     */
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

    /**
     * Obtener informacion de la reserva
     *
     * @param IDReserva ID de la reserva
     * @return Informacion de la reserva
     */
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
        em += "Nombre y Apellidos: " + getNombreAndApellidos(ID_Usuario) + "\n";
        em += "Asiento asignado: " + Asiento + "\n";
        em += getVueloInfo(ID_Vuelo);
        return em;
    }


    /**
     * Obtener un numero de columnas y filas con una proporcion
     *
     * @param numSeats Numero de asientos
     * @return numero de columnas
     * @see #getNumRows(int, int)
     */
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

    /**
     * Obtener el numero de filas y columnas con una proporcion
     *
     * @param numSeats Numero de asientos
     * @param numCols  Numero de columnas
     * @return Numero de filas
     * @see #getNumCols(int)
     */
    public static int getNumRows(int numSeats, int numCols) {
        int maxRows = 8; // Maximum number of rows
        int numRows = (int) Math.ceil((double) numSeats / numCols);
        if (numRows > maxRows) {
            numRows = maxRows;
        }
        return numRows;
    }

    public static ObservableList<Vuelo> getVuelosObservableList(int ciudadSalida, int ciudadLlegada) throws SQLException {
        ObservableList<Vuelo> out = FXCollections.observableArrayList();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM Vuelos");

        if (ciudadSalida > 0) {
            queryBuilder.append(" WHERE Ciudad_Salida = ?");
        }
        if (ciudadLlegada > 0) {
            if (ciudadSalida <= 0) {
                queryBuilder.append(" WHERE Ciudad_Destino = ?");
            } else {
                queryBuilder.append(" AND Ciudad_Destino = ?");
            }
        }

        String query = queryBuilder.toString();
        PreparedStatement checkStatement = App.con.prepareStatement(query);

        int indiceParametro = 1;
        if (ciudadSalida > 0) {
            checkStatement.setInt(indiceParametro++, ciudadSalida);
        }
        if (ciudadLlegada > 0) {
            checkStatement.setInt(indiceParametro++, ciudadLlegada);
        }


        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            java.sql.Date fechaSalida = rs.getDate("Fecha_Salida");
            String fechaSalidaStr = null;

            if (fechaSalida != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                fechaSalidaStr = dateFormat.format(fechaSalida);
            }

            out.add(new Vuelo(
                    rs.getInt("ID_Vuelo"),
                    Getter.getNombreCiudad(rs.getInt("Ciudad_Salida")),
                    Getter.getNombreCiudad(rs.getInt("Ciudad_Destino")),
                    fechaSalidaStr,
                    getAsientosLibresCant(Getter.getIDAvioFromVuelo(rs.getInt("ID_Vuelo")), rs.getInt("ID_Vuelo"))
            ));
        }

        return out;
    }


}
