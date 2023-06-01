package app;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.FileOutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Gestionar las interaccciones de cambios de datos de la BDD
 */
public class Gestioner {

    /**
     * Generar un justificante del vuelo con formato PDF
     *
     * @param pdfText  Texto del PDF
     * @param savePath Ruta donde se guarda el PDF
     * @return True si el PDF se ha guardado exitosamente, False de lo contrario
     */
    public static boolean createPDF(String pdfText, String savePath) {
        // Generar objeto Documento
        Document PDFdocument = new Document();
        try {
            // Informacion del Archivo
            if (!savePath.contains(".pdf"))
                savePath += ".pdf";
            PdfWriter.getInstance(PDFdocument, new FileOutputStream(savePath));

            // Entramos en edicion
            PDFdocument.open();

            // Imagen en la parte superior
            Image topImage = Image.getInstance(new URL("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/header.png"));
            topImage.setAlignment(Element.ALIGN_CENTER);
            topImage.scaleToFit(500, 300);
            PDFdocument.add(topImage);

            // Titulo
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
            Paragraph titleParagraph = new Paragraph("Justificante de Vuelo", titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleParagraph.setSpacingBefore(60);
            titleParagraph.setSpacingAfter(60);
            PDFdocument.add(titleParagraph);

            // Insertar la informacion del vuelo
            Font infoFont = new Font(Font.FontFamily.HELVETICA, 18);
            Paragraph infoParagraph = new Paragraph(pdfText, infoFont);
            infoParagraph.setAlignment(Element.ALIGN_CENTER);
            infoParagraph.setSpacingAfter(20);
            PDFdocument.add(infoParagraph);

            // Generar el QR con un enlace
            String qrCodeUrl = "https://github.com/Zenin0/Proyecto-DAM";
            Image qrCodeImage = Image.getInstance("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + qrCodeUrl);
            qrCodeImage.setAlignment(Element.ALIGN_CENTER);
            qrCodeImage.scaleAbsolute(150, 150);
            float qrCodeX = (PDFdocument.getPageSize().getWidth() - qrCodeImage.getScaledWidth()) / 2;
            qrCodeImage.setAbsolutePosition(qrCodeX, 50);
            PDFdocument.add(qrCodeImage);

            PDFdocument.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Login de los usuarios
     *
     * @param usuario Nombre del usuario introducido
     * @param pass    Contraseña introducida
     * @return 1 Acceso de un usuario admin   0 Acceso de un usuario no admin -1  Error en el login
     * @see MD5Hasher#getMd5()
     */
    public static int login(String usuario, String pass) {
        try {

            // Objecto de encriptacion
            MD5Hasher md5 = new MD5Hasher(pass);

            // Si el usuario y la contraseña ya existen en la base de datos
            String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setString(1, usuario);
            checkStatement.setString(2, md5.getMd5());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // Si el usuario es admin o no
                query = "SELECT Is_Admin FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
                checkStatement = App.con.prepareStatement(query);
                checkStatement.setString(1, usuario);
                checkStatement.setString(2, md5.getMd5());
                resultSet = checkStatement.executeQuery();
                resultSet.next();
                // Return Acceso Admin
                if (resultSet.getInt(1) == 1) {
                    GlobalData.userName = usuario;
                    return 1;
                } else if (resultSet.getInt(1) == 0) {
                    // Return acceso usuario
                    GlobalData.userName = usuario;
                    return 0;
                } else {
                    // Return error de login
                    return -1;
                }

            }

        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        // Return error de login
        return -1;
    }

    /**
     * Registrar un usuario
     *
     * @param Usuario Nombre de usuario
     * @param Pass1   Contraseña 1
     * @param Pass2   Contraseña 2
     * @param admin   Checkbox de Administrador
     * @return Si se ha creado o si no
     * @see MD5Hasher#getMd5()
     */
    public static boolean registrar(String Usuario, String nombre, String apellidos, String Pass1, String Pass2, boolean admin) {
        int id;
        try {

            // Comprobar que las dos contraseñas coincidan
            if (!Pass1.equals(Pass2)) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Las contraseñas no coinciden");
                dialog.show();
                return false;
            } else {
                MD5Hasher md5 = new MD5Hasher(Pass1);

                // Comprobar si el usuario y la contraseña ya existen en la base de datos
                String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
                PreparedStatement checkStatement = App.con.prepareStatement(query);
                checkStatement.setString(1, Usuario);
                checkStatement.setString(2, md5.getMd5());
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Este Usuario y esta contraseña ya existen");
                    dialog.show();
                    return false;

                } else {

                    // Al marcar la casilla de crear un usuario administrador
                    // Comprobar que la contraseña admin este bien
                    if (admin) {
                        if (!new MD5Hasher(new PassDialog().showAndWait().get()).getMd5().equals(new MD5Hasher("root").getMd5())) {
                            Alert dialog = new Alert(AlertType.ERROR);
                            dialog.setTitle("ERROR");
                            dialog.setHeaderText("Contraseña de administrador incorrecta");
                            dialog.show();
                            return false;
                        }
                    }

                    String test = "SELECT max(ID_Usuario) FROM Usuarios";
                    PreparedStatement prst = App.con.prepareStatement(test);
                    ResultSet resulttest = prst.executeQuery();
                    if (resulttest.next()) {
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

                        Alert dialog = new Alert(AlertType.CONFIRMATION);
                        dialog.setTitle("Usuario");
                        dialog.setHeaderText("Usuario creado correctamente");
                        dialog.show();
                        return true;
                    }

                }
            }

        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    /**
     * Registrar un avión nuevo
     *
     * @param nombreAvion Nombre del avión
     * @param capacidad   Capacidad del avión
     * @return Si se ha creado o si no
     */
    public static boolean registrarAvion(String nombreAvion, int capacidad) {
        try {
            int id;
            String query = "SELECT COUNT(*) FROM Aviones WHERE Nombre_Avion = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setString(1, nombreAvion);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            // Comprobacion de si existe ya ese avión
            if (count > 0) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Este avión ya esta registrado");
                dialog.show();
                return false;

            } else {
                // Sacar la siguiente ID de los Aviones
                String test = "SELECT max(ID_Avion) FROM Aviones";
                PreparedStatement prst = App.con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    id = resulttest.getInt(1);

                    // Insertar los datos
                    String consulta = "INSERT INTO Aviones (ID_Avion, Nombre_Avion, capacidad) VALUES (? , ?, ?)";
                    PreparedStatement insertStatement = App.con.prepareStatement(consulta);
                    insertStatement.setInt(1, id + 1);
                    insertStatement.setString(2, nombreAvion);
                    insertStatement.setInt(3, capacidad);
                    insertStatement.executeUpdate();
                    return true;
                }

            }

        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    /**
     * Crear una nueva ciudad
     *
     * @param Ciudad Nombre de la ciudad
     * @param Pais   País al que pertenece
     * @return Si se ha creado o si no
     */
    public static boolean registrarCiudad(String Ciudad, String Pais) {
        int id;
        String ciudad = Ciudad.substring(0, 1).toUpperCase() + Ciudad.substring(1).toLowerCase();
        String pais = Pais.substring(0, 1).toUpperCase() + Pais.substring(1).toLowerCase();

        try {


            String query = "SELECT COUNT(*) FROM Ciudades WHERE Nombre_Ciudad = ? AND Pais = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setString(1, ciudad);
            checkStatement.setString(2, pais);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            // Comprobar que la ciudad no existe
            if (count > 0) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Esta ciudad ya esta registrada");
                dialog.show();
                return false;

            } else {

                // Sacar la siguiente ID de las Ciudades
                String test = "SELECT max(ID_Ciudad) FROM Ciudades";
                PreparedStatement prst = App.con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    id = resulttest.getInt(1);

                    // Insertas la nueva ciudad
                    String consulta = "INSERT INTO Ciudades (ID_Ciudad, Nombre_Ciudad, Pais) VALUES (? , ?, ?)";
                    PreparedStatement insertStatement = App.con.prepareStatement(consulta);
                    insertStatement.setInt(1, id + 1);
                    insertStatement.setString(2, ciudad);
                    insertStatement.setString(3, pais);
                    insertStatement.executeUpdate();
                    return true;
                }

            }
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    /**
     * Registrar un vuelo
     *
     * @param CiudadSalida  Nombre de la ciudad de Salida
     * @param CiudadDestino Nombre de la ciuada de Destino
     * @param idAvion       ID del avión
     * @param fecha         Fecha de salida
     * @return Si se ha creado o si no
     */
    public static boolean registrarVuelo(String CiudadSalida, String CiudadDestino, int idAvion, String fecha) throws ParseException {

        int idAvionInt;
        int id;
        try {

            // Conseguir la siguiente ID de los Vuelos
            idAvionInt = idAvion;
            String test = "SELECT max(ID_Vuelo) FROM Vuelos";
            PreparedStatement prst = App.con.prepareStatement(test);
            ResultSet resulttest = prst.executeQuery();
            if (resulttest.next()) {
                id = resulttest.getInt(1);

                // Realizar la siguiente insercion de los datos del nuevo vuelo
                String consulta = "INSERT INTO Vuelos (ID_Vuelo, Ciudad_Salida, Ciudad_Destino, ID_Avion, Fecha_Salida, Creada) VALUES (? , ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = App.con.prepareStatement(consulta);
                insertStatement.setInt(1, id + 1);
                insertStatement.setInt(2, Getter.getIDCiudad(CiudadSalida));
                insertStatement.setInt(3, Getter.getIDCiudad(CiudadDestino));
                insertStatement.setInt(4, idAvionInt);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = dateFormat.parse(fecha);
                insertStatement.setDate(5, new java.sql.Date(date.getTime()));
                insertStatement.setString(6, GlobalData.userName);
                insertStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    /**
     * Eliminar un vuelo
     *
     * @param ID ID del vuelo
     * @return si se ha creado o si no true|false
     */
    public static boolean eliminarVuelo(int ID) {
        try {
            String query = "DELETE FROM Vuelos WHERE ID_Vuelo = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setInt(1, ID);
            checkStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    /**
     * Eliminar un avion
     *
     * @param ID ID del avion
     * @return si se ha creado o si no true|false
     */
    public static boolean eliminarAvion(int ID) {
        try {
            String query = "DELETE FROM Aviones WHERE ID_Avion = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setInt(1, ID);
            checkStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    /**
     * Reservar un vuelo
     *
     * @param IDUSU           ID del usuario
     * @param IDVUELO         ID del vuelo
     * @param selectedAsiento asiento seleccionado
     * @return ID de la reserva
     */
    public static int reservarVuelo(int IDUSU, int IDVUELO, int selectedAsiento) {
        try {

            int id;
            // Sacar la siguiente ID del vuelo
            String test = "SELECT max(ID_Reserva) FROM Reservas";
            PreparedStatement prst = App.con.prepareStatement(test);
            ResultSet resulttest = prst.executeQuery();
            if (resulttest.next()) {
                // Insertarlo
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
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }
        return 0;
    }

    /**
     * Eliminar una reserva
     *
     * @param IDReserva ID de la reserva
     */
    public static void eliminarReserva(int IDReserva) {
        try {
            String query = "DELETE FROM Reservas WHERE ID_Reserva = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setInt(1, IDReserva);
            checkStatement.executeUpdate();
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

    }

    /**
     * Eliminar una reserva
     *
     * @param IDReserva  ID de la reserva
     * @param AsientoNew Nuevo asiento seleccionado
     */
    public static int modificarReserva(int IDReserva, int AsientoNew) {
        try {
            String query = "UPDATE Reservas SET Asiento = ? WHERE ID_Reserva = ?";
            PreparedStatement updateStatement = App.con.prepareStatement(query);
            updateStatement.setInt(1, AsientoNew);
            updateStatement.setInt(2, IDReserva);
            updateStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }
        return 0;
    }

}
