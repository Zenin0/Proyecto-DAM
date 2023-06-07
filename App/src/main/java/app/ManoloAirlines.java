package app;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Gestionar codigo logico de la App
 *
 * @version 1.2.0
 */
public class ManoloAirlines {

    /**
     * Obtener una lista de String con las Ciduades
     *
     * @return ArrayList con las ciudades
     * @throws SQLException Error con la consulta
     */
    public static ArrayList<String> getlistaCiudadesStrings() throws SQLException {
        ArrayList<String> out = new ArrayList<>();
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
     * Obtener una lista de Strings de los aviones
     *
     * @return ArrayList de los Aviones
     * @throws SQLException Error en la consulta
     */
    public static ArrayList<String> getlistaAvionesStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<>();
        String query = "SELECT * FROM Aviones";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            out.add(rs.getString("ID_Avion") + " - " + rs.getString("Nombre_Avion") + " - " + rs.getInt("Capacidad"));
        }
        return out;
    }

    /**
     * Obtener una lista de Strings de los Vuelos
     *
     * @return ArrayList de los vuelos
     * @throws SQLException Error en la consulta
     */
    public static ArrayList<String> getlistaVuelosStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<>();
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
     * Obtener el nombre de la ciudad en base a su ID
     *
     * @param IDCiudad ID de la ciudad
     * @return Nombre de la ciudad
     * @throws SQLException Error en la consulta
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
     * Obtener el ID de la ciudad en base a su nombre
     *
     * @param Nombre Nombre de la ciudad
     * @return ID de la ciudad
     * @throws SQLException Error en la consulta
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
     * Obtener el ID del vuelo en base a la ID de una reserva
     *
     * @param IDReserva ID de la reserva
     * @return ID del vuelo
     * @throws SQLException Error en la consulta
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
     * Obtener el nombre del avion en base a su ID
     *
     * @param IDAvion ID del avion
     * @return Nombre del avion
     * @throws SQLException Error en la consulta
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
     * Obterner la ID del avion en base a un ID de vuelo
     *
     * @param IDVuelo ID del vuelo
     * @return ID del avion
     * @throws SQLException Error en la consulta
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
     * Obtener la cantidad de asientos libres de un avion en un vuelo en base a la ID de un avion y la ID de un vuelo
     *
     * @param IDAvion ID del avion
     * @param IDVuelo ID del vuelo
     * @return Asientos libres
     * @throws SQLException Error en la consulta
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
     * Obtener una ArrayList de numeros (ID) de los asientos libres en base a la ID de un avion y la ID de un vuelo
     *
     * @param IDAvion ID del avion
     * @param IDVuelo ID del vuelo
     * @return ArrayList con los asientos libres
     * @throws SQLException Error en la consulta
     */
    public static ArrayList<Integer> getAsientosLibres(int IDAvion, int IDVuelo) throws SQLException {

        ArrayList<Integer> asientosOcupados = new ArrayList<>();
        String query = "SELECT Asiento FROM Reservas WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDVuelo);
        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            int asientoOcupado = rs.getInt("Asiento");
            asientosOcupados.add(asientoOcupado);
        }
        int asientosTotales = getCapacidadAvion(IDAvion);
        ArrayList<Integer> asientosLibres = new ArrayList<>();
        for (int i = 1; i <= asientosTotales; i++) {
            if (!asientosOcupados.contains(i)) {
                asientosLibres.add(i);
            }
        }

        return asientosLibres;
    }

    /**
     * Obtener la capacidad total del avion en base a su ID
     *
     * @param IDAvion ID del avion
     * @return Capacidad del avion
     * @throws SQLException Error en la consulta
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
     * Obtener la ID de un usuario en base a su nombre de usuario
     *
     * @param Username Nombre del usuario
     * @return ID del usuario
     * @throws SQLException Error en la consulta
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
     * Obtener el nombre y apellidos de un usuario en base a su ID
     *
     * @param IDUser ID del usuario
     * @return Nombre y Apellidos
     * @throws SQLException Error en la consulta
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
     * Obtener información de un vuelo en base a su ID
     *
     * @param IDVuelo ID del vuelo
     * @return Informacion del vuelo
     * @throws SQLException Error en la consulta
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
     * Obtener informacion de una reserva en base a su ID
     *
     * @param IDReserva ID de la reserva
     * @return Informacion de la reserva
     * @throws SQLException Error en la consulta
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
     * Obtener las columnas de asientos que "deberia" tener un avion
     *
     * @param numSeats Numero de asientos del avion
     * @return Cantidad de columnas
     * @see #getNumRows(int, int)
     */
    public static int getNumCols(int numSeats) {
        int maxCols = 40;
        int numCols = 2;
        while (numCols <= maxCols) {
            int numRows = (int) Math.ceil((double) numSeats / numCols);
            if (numRows <= 8) {
                return numCols;
            }
            numCols++;
        }
        return maxCols;
    }

    /**
     * Obtener el numero de filas que "deberia" tener un avion
     *
     * @param numSeats Numero de asientos
     * @param numCols  Numero de columnas
     * @return Numero de filas
     * @see #getNumCols(int)
     */
    public static int getNumRows(int numSeats, int numCols) {
        int maxRows = 8;
        int numRows = (int) Math.ceil((double) numSeats / numCols);
        if (numRows > maxRows) {
            numRows = maxRows;
        }
        return numRows;
    }

    /**
     * Obtener una lista de objectos vuelos, para añadirlo a un tableview
     *
     * @param ciudadSalida  ID de la ciudad de salida
     * @param ciudadLlegada ID de la ciudad de llegada
     * @return Lista de los vuelos
     * @throws SQLException Error en la conulsta
     * @see #getReservasObservableList(int, int, int)
     */
    public static ObservableList<Vuelo> getVuelosObservableList(int ciudadSalida, int ciudadLlegada) throws SQLException {
        ObservableList<Vuelo> out = FXCollections.observableArrayList();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM Vuelos");

        if (ciudadSalida > 0) { // ? Han seleccionado una ciudad de salida
            queryBuilder.append(" WHERE Ciudad_Salida = ?");
        }
        if (ciudadLlegada > 0) {  // ? Han seleccionado una ciudad de destino

            if (ciudadSalida <= 0) { // ? Han seleccionado una ciudad de salida
                queryBuilder.append(" WHERE Ciudad_Destino = ?");
            } else {
                queryBuilder.append(" AND Ciudad_Destino = ?");
            }
        }

        String query = queryBuilder.toString();
        PreparedStatement checkStatement = App.con.prepareStatement(query);

        int index = 1;
        if (ciudadSalida > 0) {
            checkStatement.setInt(index++, ciudadSalida);
        }
        if (ciudadLlegada > 0) {
            checkStatement.setInt(index++, ciudadLlegada);
        }


        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            java.sql.Date fechaSalida = rs.getDate("Fecha_Salida");
            String fechaSalidaStr = null;

            // Formatear la fecha
            if (fechaSalida != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                fechaSalidaStr = dateFormat.format(fechaSalida);
            }

            // * Añadir el vuelo a la lista
            out.add(new Vuelo(rs.getInt("ID_Vuelo"), ManoloAirlines.getNombreCiudad(rs.getInt("Ciudad_Salida")), ManoloAirlines.getNombreCiudad(rs.getInt("Ciudad_Destino")), fechaSalidaStr, getAsientosLibresCant(ManoloAirlines.getIDAvioFromVuelo(rs.getInt("ID_Vuelo")), rs.getInt("ID_Vuelo"))));
        }

        return out;
    }

    /**
     * Obtener una lista de objectos reserva, para añadirlo a un tableview
     *
     * @param IDUsuario     ID del usuario
     * @param ciudadSalida  ID de la Ciudad de salida
     * @param ciudadDestino Id de la Ciudad de destino
     * @return Lista de las Reservas
     * @throws SQLException Error en la consulta
     * @see #getVuelosObservableList(int, int)
     */
    public static ObservableList<Reserva> getReservasObservableList(int IDUsuario, int ciudadSalida, int ciudadDestino) throws SQLException {
        ObservableList<Reserva> out = FXCollections.observableArrayList();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT R.ID_Reserva, R.ID_Usuario, R.ID_Vuelo, R.Asiento, V.Fecha_Salida, V.Ciudad_Salida, V.Ciudad_Destino, V.ID_Avion ");
        queryBuilder.append("FROM Reservas R ");
        queryBuilder.append("JOIN Vuelos V ON R.ID_Vuelo = V.ID_Vuelo ");
        queryBuilder.append("WHERE R.ID_Usuario = ?");


        if (ciudadSalida > 0) { // ? Han seleccionado una ciudad de salida
            queryBuilder.append(" AND V.Ciudad_Salida = ?");
        }

        if (ciudadDestino > 0) { // ? Han seleccionado una ciduad de destino
            queryBuilder.append(" AND V.Ciudad_Destino = ?");
        }
        queryBuilder.append(" ORDER BY R.ID_Reserva");

        String query = queryBuilder.toString();

        try (PreparedStatement statement = App.con.prepareStatement(query)) {
            statement.setInt(1, IDUsuario);
            int index = 2;

            if (ciudadSalida > 0) {
                statement.setInt(index++, ciudadSalida);
            }
            if (ciudadDestino > 0) {
                statement.setInt(index++, ciudadDestino);
            }

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int reservaID = rs.getInt("ID_Reserva");
                    int usuarioID = rs.getInt("ID_Usuario");
                    int asiento = rs.getInt("Asiento");
                    java.sql.Date fechaSalida = rs.getDate("Fecha_Salida");
                    int ciudadSalidaID = rs.getInt("Ciudad_Salida");
                    int ciudadDestinoID = rs.getInt("Ciudad_Destino");
                    int avionID = rs.getInt("ID_Avion");

                    String fechaSalidaStr = null;
                    if (fechaSalida != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        fechaSalidaStr = dateFormat.format(fechaSalida);
                    }

                    // * Añadir la reserva a la lista
                    out.add(new Reserva(reservaID, getNombreAndApellidos(usuarioID), asiento, getNombreCiudad(ciudadSalidaID), getNombreCiudad(ciudadDestinoID), getNombreAvion(avionID), fechaSalidaStr));
                }
            }
        }

        return out;
    }

    /**
     * Obtener si el avion estara disponible para ese vuelo
     *
     * @param fecha        Fecha del vuelo
     * @param IDAvion      ID del avion
     * @param CiudadSalida ID de Ciudad de salida
     * @return Si esta disponible
     * @throws SQLException Error en la conulsta
     */
    public static boolean getDispnibilidad(String fecha, int IDAvion, int CiudadSalida) throws SQLException {
        String query = "SELECT COUNT(*) FROM Vuelos WHERE Fecha_Salida >= ? AND ID_Avion = ? AND Ciudad_Salida = ?";
        PreparedStatement stmt = App.con.prepareStatement(query);
        stmt.setString(1, fecha);
        stmt.setInt(2, IDAvion);
        stmt.setInt(3, CiudadSalida);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            // ? Esta disponible
            return rs.getInt(1) < 1;
        } else return true;
    }


    /**
     * Crear el PDF de justificante
     *
     * @param pdfText  Texto del PDF
     * @param savePath Ruta seleccionada
     * @return Si se ha creado el PDF
     * @see #getReservaInfo(int)
     */
    public static boolean createPDF(String pdfText, String savePath) throws IOException, DocumentException {
        Document PDFdocument = new Document();
        if (!savePath.contains(".pdf")) savePath += ".pdf";
        PdfWriter.getInstance(PDFdocument, new FileOutputStream(savePath));
        PDFdocument.open();
        Image topImage = Image.getInstance(new URL("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/header.png"));
        topImage.setAlignment(Element.ALIGN_CENTER);
        topImage.scaleToFit(500, 300);
        PDFdocument.add(topImage);
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        Paragraph titleParagraph = new Paragraph("Justificante de Vuelo", titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingBefore(60);
        titleParagraph.setSpacingAfter(60);
        PDFdocument.add(titleParagraph);
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 18);
        Paragraph infoParagraph = new Paragraph(pdfText, infoFont);
        infoParagraph.setAlignment(Element.ALIGN_CENTER);
        infoParagraph.setSpacingAfter(20);
        PDFdocument.add(infoParagraph);
        Image qrCodeImage = Image.getInstance("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://github.com/Zenin0/Proyecto-DAM");
        qrCodeImage.setAlignment(Element.ALIGN_CENTER);
        qrCodeImage.scaleAbsolute(150, 150);
        float qrCodeX = (PDFdocument.getPageSize().getWidth() - qrCodeImage.getScaledWidth()) / 2;
        qrCodeImage.setAbsolutePosition(qrCodeX, 50);
        PDFdocument.add(qrCodeImage);
        PDFdocument.close();

        return true;
    }


    /**
     * Login del usuario
     *
     * @param usuario Nombre del usuario
     * @param pass    Contraseña
     * @return Si el login es correcto
     * @see #registrar(String, String, String, String, String, boolean)
     * @see MD5Hasher#getMd5()
     */
    public static int login(String usuario, String pass) throws SQLException {

        MD5Hasher md5 = new MD5Hasher(pass);
        String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setString(1, usuario);
        checkStatement.setString(2, md5.getMd5());
        ResultSet resultSet = checkStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        if (count > 0) { // ? Existe el usuario
            query = "SELECT Is_Admin FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
            checkStatement = App.con.prepareStatement(query);
            checkStatement.setString(1, usuario);
            checkStatement.setString(2, md5.getMd5());
            resultSet = checkStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 1) { // ? Es un usuario administrador
                GlobalData.userName = usuario;
                return 1;
            } else if (resultSet.getInt(1) == 0) { // ? Es un usuario normal
                GlobalData.userName = usuario;
                return 0;
            } else { // ! Error de login
                return -1;
            }
        }
        return -1;
    }


    /**
     * Registrar un usuario nuevo
     *
     * @param Usuario   Nombre de usuario
     * @param nombre    Nombre
     * @param apellidos Apellidos
     * @param Pass1     Contraseña 1
     * @param Pass2     Contraseña 2
     * @param admin     ? Es administrador
     * @see #login(String, String)
     * @see MD5Hasher#getMd5()
     */
    public static void registrar(String Usuario, String nombre, String apellidos, String Pass1, String Pass2, boolean admin) throws SQLException {
        int id;
        if (!Pass1.equals(Pass2)) { // ? Las contraseñas coinciden
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setHeaderText("ERROR");
            dialog.setHeaderText("Las contraseñas no coinciden");
            dialog.show();
        } else {
            MD5Hasher md5 = new MD5Hasher(Pass1);
            String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setString(1, Usuario);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) { // ? El usuario existe
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setHeaderText("ERROR");
                dialog.setHeaderText("Este usuario ya esta registrado");
                dialog.show();

            } else {

                if (admin) { // ? Esta marcada la casilla admin
                    if (!new MD5Hasher(new PassDialog().showAndWait().get()).getMd5().equals(new MD5Hasher("root").getMd5())) { // ? Sabe la contraseña de admin
                        Alert dialog = new Alert(Alert.AlertType.ERROR);
                        dialog.setTitle("ERROR");
                        dialog.setHeaderText("Contraseña de administrador incorrecta");
                        dialog.show();
                        return;
                    }
                }

                String test = "SELECT max(ID_Usuario) FROM Usuarios";
                PreparedStatement prst = App.con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    // * Obtener la siguiente ID
                    id = resulttest.getInt(1);
                    String consulta = "INSERT INTO Usuarios (ID_Usuario, Nombre_Usuario, Nombre, Apellidos, Pass, is_Admin) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = App.con.prepareStatement(consulta);
                    insertStatement.setInt(1, id + 1);
                    insertStatement.setString(2, Usuario);
                    insertStatement.setString(3, nombre);
                    insertStatement.setString(4, apellidos);
                    insertStatement.setString(5, md5.getMd5());
                    insertStatement.setBoolean(6, admin);
                    insertStatement.executeUpdate();
                    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                    // * Usuario registrado
                    dialog.setTitle("Usuario");
                    dialog.setHeaderText("Usuario creado correctamente");
                    dialog.show();
                }

            }
        }


    }

    /**
     * Registrar un avion nuevo
     *
     * @param nombreAvion Nombre del avion
     * @param capacidad   Capacidad del avion
     * @return Si se ha registrado
     * @see #registrarCiudad(String, String)
     * @see #registrarVuelo(String, String, int, String)
     */
    public static boolean registrarAvion(String nombreAvion, int capacidad) throws SQLException {
        int id;
        String query = "SELECT COUNT(*) FROM Aviones WHERE Nombre_Avion = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setString(1, nombreAvion);
        ResultSet resultSet = checkStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        if (count > 0) { // ? El avion existe
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText("Este avión ya esta registrado");
            dialog.show();
            return false;
        } else {
            String test = "SELECT max(ID_Avion) FROM Aviones";
            PreparedStatement prst = App.con.prepareStatement(test);
            ResultSet resulttest = prst.executeQuery();
            if (resulttest.next()) {
                // * Obtener la siguiente ID
                id = resulttest.getInt(1);
                String consulta = "INSERT INTO Aviones (ID_Avion, Nombre_Avion, capacidad) VALUES (? , ?, ?)";
                PreparedStatement insertStatement = App.con.prepareStatement(consulta);
                insertStatement.setInt(1, id + 1);
                insertStatement.setString(2, nombreAvion);
                insertStatement.setInt(3, capacidad);
                insertStatement.executeUpdate();
                // * Avion creado
                return true;
            }

        }


        return false;
    }

    /**
     * Registrar una nueva ciudad
     *
     * @param Ciudad Nombre de la Ciudad
     * @param Pais   Nombre del Pais
     * @return Si se ha creado la ciudad
     * @see #registrarAvion(String, int)
     * @see #registrarVuelo(String, String, int, String)
     */
    public static boolean registrarCiudad(String Ciudad, String Pais) throws SQLException {
        int id;
        String ciudad = Ciudad.substring(0, 1).toUpperCase() + Ciudad.substring(1).toLowerCase();
        String pais = Pais.substring(0, 1).toUpperCase() + Pais.substring(1).toLowerCase();
        String query = "SELECT COUNT(*) FROM Ciudades WHERE Nombre_Ciudad = ? AND Pais = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setString(1, ciudad);
        checkStatement.setString(2, pais);
        ResultSet resultSet = checkStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        if (count > 0) { // ? La ciudad existe
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText("Esta ciudad ya esta registrada");
            dialog.show();

        } else {
            String test = "SELECT max(ID_Ciudad) FROM Ciudades";
            PreparedStatement prst = App.con.prepareStatement(test);
            ResultSet resulttest = prst.executeQuery();
            if (resulttest.next()) {
                // * Obtener la siguiente ID
                id = resulttest.getInt(1);
                String consulta = "INSERT INTO Ciudades (ID_Ciudad, Nombre_Ciudad, Pais) VALUES (? , ?, ?)";
                PreparedStatement insertStatement = App.con.prepareStatement(consulta);
                insertStatement.setInt(1, id + 1);
                insertStatement.setString(2, ciudad);
                insertStatement.setString(3, pais);
                insertStatement.executeUpdate();
                // * Ciudad creada
                return true;
            }

        }


        return false;
    }

    /**
     * Registrar un nuevo vuelo
     *
     * @param CiudadSalida  Nombre de la ciudad de Salida
     * @param CiudadDestino Nombre de la ciudad de Destino
     * @param idAvion       ID del avion para el vuelo
     * @param fecha         Fecha del vuelo
     * @return Si se ha creado
     * @throws ParseException Error en la consulta/insercion
     * @see #registrarAvion(String, int)
     * @see #registrarCiudad(String, String)
     */
    public static boolean registrarVuelo(String CiudadSalida, String CiudadDestino, int idAvion, String fecha) throws ParseException, SQLException {

        int idAvionInt;
        int id;
        idAvionInt = idAvion;
        String test = "SELECT max(ID_Vuelo) FROM Vuelos";
        PreparedStatement prst = App.con.prepareStatement(test);
        ResultSet resulttest = prst.executeQuery();
        if (resulttest.next()) {
            id = resulttest.getInt(1);
            String consulta = "INSERT INTO Vuelos (ID_Vuelo, Ciudad_Salida, Ciudad_Destino, ID_Avion, Fecha_Salida, Creada) VALUES (? , ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = App.con.prepareStatement(consulta);
            insertStatement.setInt(1, id + 1);
            insertStatement.setInt(2, getIDCiudad(CiudadSalida));
            insertStatement.setInt(3, getIDCiudad(CiudadDestino));
            insertStatement.setInt(4, idAvionInt);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = dateFormat.parse(fecha);
            insertStatement.setDate(5, new Date(date.getTime()));
            insertStatement.setString(6, GlobalData.userName);
            insertStatement.executeUpdate();
            // * Vuelo creado
            return true;
        }


        return false;
    }

    /**
     * Eliminar un vuelo
     *
     * @param ID ID del vuelo
     * @see #registrarVuelo(String, String, int, String)
     */
    public static void eliminarVuelo(int ID) throws SQLException {
        String query = "DELETE FROM Vuelos WHERE ID_Vuelo = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, ID);
        checkStatement.executeUpdate();

    }

    /**
     * Eliminar un avion
     *
     * @param ID ID del avion
     */
    public static void eliminarAvion(int ID) throws SQLException {

        String query = "DELETE FROM Aviones WHERE ID_Avion = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, ID);
        checkStatement.executeUpdate();

    }

    /**
     * Reservar un vuelo
     *
     * @param IDUSU           ID del usuario
     * @param IDVUELO         ID del vuelo
     * @param selectedAsiento Asiento seleccionado
     * @return ID del nuevo asiento
     * @throws SQLException Error en el select/inserto
     * @see Vuelo
     * @see #eliminarReserva(int)
     * @see #modificarReserva(int, int)
     */
    public static int reservarVuelo(int IDUSU, int IDVUELO, int selectedAsiento) throws SQLException {

        int id;
        String test = "SELECT max(ID_Reserva) FROM Reservas";
        PreparedStatement prst = App.con.prepareStatement(test);
        ResultSet resulttest = prst.executeQuery();
        if (resulttest.next()) {
            id = resulttest.getInt(1);
            String consulta = "INSERT INTO Reservas (ID_Reserva, ID_Usuario, ID_Vuelo, Asiento) VALUES (? , ?, ?, ?)";
            PreparedStatement insertStatement = App.con.prepareStatement(consulta);
            insertStatement.setInt(1, id + 1);
            insertStatement.setInt(2, IDUSU);
            insertStatement.setInt(3, IDVUELO);
            insertStatement.setInt(4, selectedAsiento);
            insertStatement.executeUpdate();
            return id + 1;

        }

        return 0;
    }

    /**
     * Eliminar una reserva
     *
     * @param IDReserva ID de la reserva
     * @see Reserva
     * @see #reservarVuelo(int, int, int)
     * @see #modificarReserva(int, int)
     */
    public static void eliminarReserva(int IDReserva) throws SQLException {
        String query = "DELETE FROM Reservas WHERE ID_Reserva = ?";
        PreparedStatement checkStatement = App.con.prepareStatement(query);
        checkStatement.setInt(1, IDReserva);
        checkStatement.executeUpdate();


    }

    /**
     * Modificar una reserva
     *
     * @param IDReserva  ID de la reserva
     * @param AsientoNew Nuevo asiento seleccionado
     * @see Reserva
     * @see #reservarVuelo(int, int, int)
     * @see #eliminarReserva(int)
     */
    public static int modificarReserva(int IDReserva, int AsientoNew) throws SQLException {

        String query = "UPDATE Reservas SET Asiento = ? WHERE ID_Reserva = ?";
        PreparedStatement updateStatement = App.con.prepareStatement(query);
        updateStatement.setInt(1, AsientoNew);
        updateStatement.setInt(2, IDReserva);
        updateStatement.executeUpdate();
        return IDReserva;
    }

    /**
     * Actualiar un usuario
     *
     * @param username          Nombre de usuario
     * @param newUsername       Nuevo nombre de usuario
     * @param password          Contraseña nueva
     * @param selectedImageFile Imagen de perfil
     * @return Si se ha creado
     */
    public static boolean actualizar(String username, String newUsername, String password, File selectedImageFile) {
        try {
            String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setString(1, username);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                String updateQuery = "UPDATE Usuarios SET Nombre_Usuario = ?, Image = ? WHERE Nombre_Usuario = ?";
                if (!password.isEmpty()) {
                    updateQuery = "UPDATE Usuarios SET Nombre_Usuario = ?, Pass = ?, Image = ? WHERE Nombre_Usuario = ?";
                }

                PreparedStatement updateStatement = App.con.prepareStatement(updateQuery);
                updateStatement.setString(1, newUsername);

                int parameterIndex = 2;
                if (!password.isEmpty()) {
                    MD5Hasher md5 = new MD5Hasher(password);
                    updateStatement.setString(parameterIndex++, md5.getMd5());
                }

                if (selectedImageFile != null) {
                    FileInputStream fileInputStream = new FileInputStream(selectedImageFile);
                    updateStatement.setBinaryStream(parameterIndex++, fileInputStream, selectedImageFile.length());
                } else {
                    updateStatement.setBinaryStream(parameterIndex++, null);
                }

                updateStatement.setString(parameterIndex, username);
                updateStatement.executeUpdate();

                Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                dialog.setTitle("Usuario");
                dialog.setHeaderText("Usuario actualizado correctamente");
                dialog.show();
                return true;
            } else {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setHeaderText("ERROR");
                dialog.setHeaderText("Este usuario no existe");
                dialog.show();
                return false;
            }
        } catch (SQLException e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        } catch (IOException e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText(e.getMessage());
            dialog.show();
        }
        return false;
    }

}
