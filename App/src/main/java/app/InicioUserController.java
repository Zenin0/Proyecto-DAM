package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InicioUserController implements Initializable {


    private final Menus menus = new Menus();

    private final Getter getter = new Getter();
    private final Gestioner gestioner = new Gestioner();


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
        ArrayList<String> vuelos = menus.listaVuelosStrings();
        if (vuelos.size() > 0) {
            for (String vuelo : vuelos) {
                String[] parts = vuelo.replaceAll(" ", "").split("-");
                if (getter.getAsientosLibres(getter.getIDAvioFromVuelo(Integer.parseInt(parts[0])), Integer.parseInt(parts[0])) > 0) {
                    String asientosLibres = String.valueOf(getter.getAsientosLibres(getter.getIDAvioFromVuelo(Integer.parseInt(parts[0])), Integer.parseInt(parts[0])));
                    String nombreCiudadSalida = new Getter().getNombreCiudad(Integer.parseInt(parts[1]));
                    String nombreCiudadDestino = new Getter().getNombreCiudad(Integer.parseInt(parts[2]));
                    String res = parts[0] + "\nCiudad de Salida: " + nombreCiudadSalida + " \nCiuad de Destino: " + nombreCiudadDestino + "\nFecha Despegue: " + parts[5] + "/" + parts[4] + "/" + parts[3] + "\nAsientos Disponibles: " + asientosLibres;
                    this.vuelosDisponiblesReservaList.getItems().add(res);
                }
            }
        }else{
            Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.setTitle("Vuelos");
            dialog.setHeaderText("No hay vuelos Dispnibles");
            dialog.show();
            this.vuelosDisponiblesReservaList.getItems().add("No hay vuelos Dispnibles");
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
        String[] s = this.vuelosDisponiblesReservaList.getSelectionModel().getSelectedItem().split("\n");
        Alert dialog = new Alert(AlertType.INFORMATION);
        dialog.setTitle("Asientos Libres");
        dialog.setHeaderText(String.valueOf(getter.getAsientosLibres(getter.getIDAvioFromVuelo(Integer.parseInt(s[0])), Integer.parseInt(s[0]))));
        dialog.show();
        Gestioner.createPDF(this.vuelosDisponiblesReservaList.getSelectionModel().getSelectedItem());

    }
}
