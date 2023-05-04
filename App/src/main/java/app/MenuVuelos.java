package app;

import java.sql.*;
import java.util.ArrayList;

public class MenuVuelos {

    MenuVuelos() {
    }

    public ArrayList<String> menuItemsStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<String>();

        Connection con = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/Manolo_Airlines", "root", "admini");
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT ID_Vuelo FROM Vuelos";
        PreparedStatement checkStatement = con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        String arr = null;
        while (rs.next()) {
            String em = rs.getString("ID_Vuelo");
            arr = em.replace("\n", ",");
            out.add(arr);
        }
        con.close();
        return out;
    }

}
