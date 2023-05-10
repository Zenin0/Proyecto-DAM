package app;

import java.sql.*;

public class Getter {

    private final String DB_URL = "jdbc:mysql://172.17.0.2:3306/Manolo_Airlines";
    private final String USER = "root";
    private final String PASS = "admini";

    Getter() {
    }

    public String getNombreCiudad(int ID) throws SQLException {

        String em = "";
        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT Nombre_Ciudad FROM Ciudades WHERE ID_Ciudad = ?";
        PreparedStatement checkStatement = con.prepareStatement(query);
        checkStatement.setInt(1, ID);
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            em = rs.getString(1);
        }
        con.close();

        return em;
    }

}