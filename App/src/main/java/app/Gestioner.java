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

import java.io.*;


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
        Document PDFdocument = new Document();
        try {
            if (!savePath.contains(".pdf"))
                savePath += ".pdf";
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

            MD5Hasher md5 = new MD5Hasher(pass);
            String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setString(1, usuario);
            checkStatement.setString(2, md5.getMd5());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count > 0) {
                query = "SELECT Is_Admin FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
                checkStatement = App.con.prepareStatement(query);
                checkStatement.setString(1, usuario);
                checkStatement.setString(2, md5.getMd5());
                resultSet = checkStatement.executeQuery();
                resultSet.next();
                if (resultSet.getInt(1) == 1) {
                    GlobalData.userName = usuario;
                    return 1;
                } else if (resultSet.getInt(1) == 0) {
                    GlobalData.userName = usuario;
                    return 0;
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }
        return -1;
    }

    /**
     * Registrar un usuario
     *
     * @param Usuario Nombre de usuario
     * @param Pass1   Contraseña 1
     * @param Pass2   Contraseña 2
     * @param admin   Checkbox de Administrador
     * @see MD5Hasher#getMd5()
     */
    public static void registrar(String Usuario, String nombre, String apellidos, String Pass1, String Pass2, boolean admin) {
        int id;
        try {
            if (!Pass1.equals(Pass2)) {
                Alert dialog = new Alert(AlertType.ERROR);
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

                if (count > 0) {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setHeaderText("ERROR");
                    dialog.setHeaderText("Este usuario ya existe");
                    dialog.show();

                } else {

                    if (admin) {
                        if (!new MD5Hasher(new PassDialog().showAndWait().get()).getMd5().equals(new MD5Hasher("root").getMd5())) {
                            Alert dialog = new Alert(AlertType.ERROR);
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
                    }

                }
            }

        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }
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
            if (count > 0) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Este avión ya esta registrado");
                dialog.show();
                return false;

            } else {
                String test = "SELECT max(ID_Avion) FROM Aviones";
                PreparedStatement prst = App.con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    id = resulttest.getInt(1);
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
            if (count > 0) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Esta ciudad ya esta registrada");
                dialog.show();

            } else {
                String test = "SELECT max(ID_Ciudad) FROM Ciudades";
                PreparedStatement prst = App.con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    id = resulttest.getInt(1);
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
            idAvionInt = idAvion;
            String test = "SELECT max(ID_Vuelo) FROM Vuelos";
            PreparedStatement prst = App.con.prepareStatement(test);
            ResultSet resulttest = prst.executeQuery();
            if (resulttest.next()) {
                id = resulttest.getInt(1);
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
     * Modificar una reserva
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
