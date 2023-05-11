package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InicioUserController implements Initializable {


    private final Menus menus = new Menus();

    private final Getter getter = new Getter();


    @FXML
    private Button endSession;

    @FXML
    private SplitMenuButton optionsMenu;

    @FXML
    private Button reservarButton;

    @FXML
    private Label reservarLabel;

    @FXML
    private MenuItem reservarMeuItem;

    @FXML
    private ListView<String> vuelosDisponiblesReservaList;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        reservarMeuItem.setOnAction(event -> {
            try {
                menuReservas();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });
        this.endSession.setOnAction((event) -> {
            try {
                endSession();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });
        this.reservarButton.setOnAction(event -> {
            try {
                reservar();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });
    }

    public void menuReservas() throws SQLException {
        this.optionsMenu.setText("Reservar");
        this.vuelosDisponiblesReservaList.setVisible(true);
        this.reservarButton.setVisible(true);
        this.reservarLabel.setVisible(true);

        loadVuelos();
    }


    public void loadVuelos() throws SQLException {
        for (String vuelo : menus.listaVuelosStrings()) {
            String[] parts = vuelo.replaceAll(" ", "").split("-");
            String res = parts[0] + " - Salida: " + new Getter().getNombreCiudad(Integer.parseInt(parts[1])) + " \nDestino: "
                    + new Getter().getNombreCiudad(Integer.parseInt(parts[2])) + "\nFecha Salida: " + parts[5] + "/" + parts[4] + "/" + parts[3];
            vuelosDisponiblesReservaList.getItems().add(res);
        }
    }

    private void endSession() throws IOException {
        App.setRoot("login");
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Session Terminada");
        dialog.setHeaderText("Sesión terminada con éxito");
        dialog.show();
    }

    private void reservar() throws SQLException {
        String[] s = this.vuelosDisponiblesReservaList.getSelectionModel().getSelectedItem().replace(" ", "").split("-");
        Alert dialog = new Alert(AlertType.INFORMATION);
        dialog.setTitle("Asientos Libres");
        dialog.setHeaderText(String.valueOf(getter.getAsientosLibres(getter.getIDAvioFromVuelo(Integer.parseInt(s[0])), Integer.parseInt(s[0]))));
        dialog.show();

    }
}
