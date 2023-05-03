package app;

import java.sql.*;
import java.util.ArrayList;

public class MenuAviones {

    MenuAviones() {
    }

    public ArrayList<String> menuItemsStrings() throws SQLException {

        ArrayList<String> out = new ArrayList<String>();

        Connection con = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/Manolo_Airlines", "root","admini");
        // Comprobar si el usuario y la contraseña ya existen en la base de datos
        String query = "SELECT ID_Avion,Nombre_Avion,Anyo_Fabricacion,Capacidad FROM Aviones";
        PreparedStatement checkStatement = con.prepareStatement(query);
        ResultSet rs = checkStatement.executeQuery();
        while (rs.next()) {
            out.add(rs.getString("ID_Avion") + " - " + rs.getString("Nombre_Avion") + " - " + rs.getString("Capacidad"));
        }
        return out;
    }

}
