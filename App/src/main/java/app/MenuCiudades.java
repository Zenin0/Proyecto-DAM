package app;

import java.sql.*;
import java.util.ArrayList;

public class MenuCiudades {

    MenuCiudades() {
    }

    public ArrayList<String> menuItemsStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<String>();

        Connection con = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/Manolo_Airlines", "root", "admini");
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT Nombre_Ciudad FROM Ciudades";
        PreparedStatement checkStatement = con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        String arr = null;
        while (rs.next()) {
            String em = rs.getString("Nombre_Ciudad");
            arr = em.replace("\n", ",");
            out.add(arr);
        }
        return out;
    }


}
