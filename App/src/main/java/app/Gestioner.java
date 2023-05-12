package app;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.FileOutputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// Objecto para gestionar las interaccciones de insercion/eliminacion de datos de la BDD
public class Gestioner {

    private final String DB_URL = "jdbc:mysql://172.17.0.2:3306/Manolo_Airlines";
    private final String USER = "root";
    private final String PASS = "admini";

    public Gestioner() {

    }


    // Funcion para crear el PDF para el Ticket del Vuelo
    public static void createPDF(String pdfText) {
        // Generar Objecto Documento
        Document PDFdocument = new Document();
        try {
            // Informacion del Archivo
            PdfWriter writer = PdfWriter.getInstance(PDFdocument, new FileOutputStream("JustificanteDeVuelo.pdf"));

            PDFdocument.open();
            Paragraph paragraph = new Paragraph();

            // Titulo
            paragraph = new Paragraph("Justificante de Vuelo", new Font(Font.FontFamily.TIMES_ROMAN, 25));
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            PDFdocument.add(paragraph);

            // Insertar el Logo
            Image image = Image.getInstance("src/main/resources/app/css/logoT.png");
            image.setAlignment(Image.MIDDLE);
            image.scaleAbsolute(300, 300);
            PDFdocument.add(image);

            // Insertar la informacion de el vuelo
            paragraph = new Paragraph(pdfText, new Font(Font.FontFamily.TIMES_ROMAN, 18));
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            PDFdocument.add(paragraph);

            // Generar el QR
            String qrCodeUrl = "https://github.com/Zenin0/Proyecto-DAM";
            Image qrCodeImage = Image.getInstance("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + qrCodeUrl);
            qrCodeImage.setAbsolutePosition((PDFdocument.getPageSize().getWidth() - qrCodeImage.getScaledWidth()) / 2, 50);
            PDFdocument.add(qrCodeImage);

            PDFdocument.close();
        } catch (Exception e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText(e.getMessage());
            dialog.show();
        }
    }


    // Funcion pra el login de usuarios
    // Guía returns
    // return 1 == Acceso admin
    // return 0 == Acceso NO admin
    // return -1, Error login
    public int login(String usuario, String pass) {
        try {
            String DB_URL = "jdbc:mysql://172.17.0.2:3306/Manolo_Airlines";
            String USER = "root";
            String PASS = "admini";
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            MD5Hasher md5 = new MD5Hasher(pass);

            // Comprobar si el usuario y la contraseña ya existen en la base de datos
            String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
            PreparedStatement checkStatement = conn.prepareStatement(query);
            checkStatement.setString(1, usuario);
            checkStatement.setString(2, md5.getMd5());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                // Comprobar si el usuario es admin o no
                query = "SELECT Is_Admin FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
                checkStatement = conn.prepareStatement(query);
                checkStatement.setString(1, usuario);
                checkStatement.setString(2, md5.getMd5());
                resultSet = checkStatement.executeQuery();
                resultSet.next();
                // Return Acceso Admin
                if (resultSet.getInt(1) == 1) {
                    conn.close();
                    GlobalData.userName = usuario;
                    return 1;
                } else if (resultSet.getInt(1) == 0) {
                    // Return acceso usuario
                    conn.close();
                    GlobalData.userName = usuario;
                    return 0;
                } else {
                    return -1;
                }

            }
            conn.close();

        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }


        return -1;
    }

    // Funcion para registrar un nuevo usuario
    public boolean registrar(String Usuario, String Pass1, String Pass2, boolean admin) {
        int id;
        try {
            String DB_URL = "jdbc:mysql://172.17.0.2:3306/Manolo_Airlines";
            String USER = "root";
            String PASS = "admini";
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            // Comprobar que las contraseñas coincidan
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
                PreparedStatement checkStatement = con.prepareStatement(query);
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
                    PreparedStatement prst = con.prepareStatement(test);
                    ResultSet resulttest = prst.executeQuery();
                    if (resulttest.next()) {
                        id = resulttest.getInt(1);

                        // Insert the new record into the database
                        String consulta = "INSERT INTO Usuarios (ID_Usuario, Nombre_Usuario, Pass, is_Admin) VALUES (? , ?, ?, ?)";
                        PreparedStatement insertStatement = con.prepareStatement(consulta);
                        insertStatement.setInt(1, id + 1);
                        insertStatement.setString(2, Usuario);
                        insertStatement.setString(3, md5.getMd5());
                        insertStatement.setBoolean(4, admin);
                        insertStatement.executeUpdate();

                        Alert dialog = new Alert(AlertType.CONFIRMATION);
                        dialog.setTitle("Usuario");
                        dialog.setHeaderText("Usuario creado correctamente");
                        dialog.show();
                        return true;
                    }

                }
            }
            con.close();

        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    // Funcion que registra un avion nuevo
    public boolean registrarAvion(String nombreAvion, int anyoFabricacion, int capacidad) {
        try {
            int id;
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT COUNT(*) FROM Aviones WHERE Nombre_Avion = ? AND Anyo_Fabricacion = ?";
            PreparedStatement checkStatement = con.prepareStatement(query);
            checkStatement.setString(1, nombreAvion);
            checkStatement.setInt(2, anyoFabricacion);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            // Comprobacion de si existe ya ese avion
            if (count > 0) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Este avión ya esta registrado");
                dialog.show();
                return false;

            } else {
                // Sacar la siguiente ID de los Aviones
                String test = "SELECT max(ID_Avion) FROM Aviones";
                PreparedStatement prst = con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    id = resulttest.getInt(1);

                    // Insertar los datos
                    String consulta = "INSERT INTO Aviones (ID_Avion, Nombre_Avion, Anyo_Fabricacion, capacidad) VALUES (? , ?, ?, ?)";
                    PreparedStatement insertStatement = con.prepareStatement(consulta);
                    insertStatement.setInt(1, id + 1);
                    insertStatement.setString(2, nombreAvion);
                    insertStatement.setInt(3, anyoFabricacion);
                    insertStatement.setInt(4, capacidad);
                    insertStatement.executeUpdate();
                    return true;
                }

            }

            con.close();
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    // Funcion para registrar una nueva ciudad
    public boolean registrarCiudad(String Ciudad, String Pais) {
        int id;
        String ciudad = Ciudad.substring(0, 1).toUpperCase() + Ciudad.substring(1).toLowerCase();
        String pais = Pais.substring(0, 1).toUpperCase() + Pais.substring(1).toLowerCase();

        try {
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "SELECT COUNT(*) FROM Ciudades WHERE Nombre_Ciudad = ? AND Pais = ?";
            PreparedStatement checkStatement = con.prepareStatement(query);
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
                PreparedStatement prst = con.prepareStatement(test);
                ResultSet resulttest = prst.executeQuery();
                if (resulttest.next()) {
                    id = resulttest.getInt(1);

                    // Insertas la nueva ciudad
                    String consulta = "INSERT INTO Ciudades (ID_Ciudad, Nombre_Ciudad, Pais) VALUES (? , ?, ?)";
                    PreparedStatement insertStatement = con.prepareStatement(consulta);
                    insertStatement.setInt(1, id + 1);
                    insertStatement.setString(2, ciudad);
                    insertStatement.setString(3, pais);
                    insertStatement.executeUpdate();
                    return true;
                }

            }
            con.close();
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    // Funcion para registrar un nuevo vuelo
    public boolean registrarVuelo(String CiudadSalida, String CiudadDestino, int idAvion, String fecha) throws ParseException {
        int ciudadSalida = 0;
        int ciudadDestino = 0;
        int idAvionInt;
        int id;
        try {

            // Conseguir la ID de la Ciudad en funcion del Nombre de la ciudad de destino
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT ID_Ciudad FROM Ciudades WHERE Nombre_Ciudad = ?";
            PreparedStatement checkStatement = con.prepareStatement(query);
            checkStatement.setString(1, CiudadDestino);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                ciudadDestino = resultSet.getInt(1);
            }
            // Conseguir la ID de la Ciudad en funcion del Nombre de la ciudad de salida
            query = "SELECT ID_Ciudad FROM Ciudades WHERE Nombre_Ciudad = ?";
            checkStatement = con.prepareStatement(query);
            checkStatement.setString(1, CiudadSalida);
            resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                ciudadSalida = resultSet.getInt(1);
            }

            // Conseguir la siguiente ID de los Vuelos
            idAvionInt = idAvion;
            String test = "SELECT max(ID_Vuelo) FROM Vuelos";
            PreparedStatement prst = con.prepareStatement(test);
            ResultSet resulttest = prst.executeQuery();
            if (resulttest.next()) {
                id = resulttest.getInt(1);

                // Realizar la siguiente insercion de los datos del nuevo vuelo
                String consulta = "INSERT INTO Vuelos (ID_Vuelo, Ciudad_Salida, Ciudad_Destino, ID_Avion, Fecha_Salida, Creada) VALUES (? , ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = con.prepareStatement(consulta);
                insertStatement.setInt(1, id + 1);
                insertStatement.setInt(2, ciudadSalida);
                insertStatement.setInt(3, ciudadDestino);
                insertStatement.setInt(4, idAvionInt);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = dateFormat.parse(fecha);
                insertStatement.setDate(5, new java.sql.Date(date.getTime()));
                insertStatement.setString(6, GlobalData.userName);
                insertStatement.executeUpdate();
                return true;
            }


            con.close();
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

    // Funcion para eliminar un vuelo de la BDD
    public boolean eliminarVuelo(int ID) {
        try {
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "DELETE FROM Vuelos WHERE ID_Vuelo = ?";
            PreparedStatement checkStatement = con.prepareStatement(query);
            checkStatement.setInt(1, ID);
            checkStatement.executeUpdate();
            con.close();
            return true;
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }


}
