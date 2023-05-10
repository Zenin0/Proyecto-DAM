package app;

import java.net.*;
import java.sql.SQLException;
import java.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;

public class InicioUserController implements Initializable {

    @FXML
    private ListView<String> listaVuelos;

    @FXML
    private Button reservarButton;

    private Menus menus = new Menus();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            loadVuelos();
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText(e.getMessage());
            dialog.show();
        }
    }

    public void loadVuelos() throws SQLException {
        for (String vuelo : menus.listaVuelosStrings()) {
            String[] parts = vuelo.replaceAll(" ", "").split("-");
            String res = "Salida: " + new Getter().getNombreCiudad(Integer.parseInt(parts[0])) + " - Destino: "
                    + new Getter().getNombreCiudad(Integer.parseInt(parts[1]));
            listaVuelos.getItems().add(res);
        }
    }
}
