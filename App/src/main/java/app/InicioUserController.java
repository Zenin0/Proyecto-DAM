package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class InicioUserController implements Initializable {

    private final Getter getter = new Getter();
    private final Gestioner gestioner = new Gestioner();


    @FXML
    private Button downloadJustificanteButton;

    @FXML
    private Button endSession;

    @FXML
    private ListView<String> misReservasList;

    @FXML
    private MenuItem misReservasMenuItem;

    @FXML
    private Button modificarReservaButton;

    @FXML
    private SplitMenuButton optionsMenu;

    @FXML
    private Button removeReservaButton;

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
        this.misReservasMenuItem.setOnAction(event -> {
            try {
                menuMisReservas();
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
        this.downloadJustificanteButton.setOnAction(event -> {
            try {
                descargarJustificante();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

    }

    // Modificar la vista para reservar un avion
    public void menuReservas() throws SQLException {
        this.optionsMenu.setText("Reservar");
        this.vuelosDisponiblesReservaList.setVisible(true);
        this.reservarButton.setVisible(true);
        this.reservarLabel.setVisible(true);
        this.misReservasList.setVisible(false);
        this.removeReservaButton.setVisible(false);
        this.modificarReservaButton.setVisible(false);
        this.downloadJustificanteButton.setVisible(false);
        loadVuelos();
    }

    // Modificar la vista para reservar un avion
    public void menuMisReservas() throws SQLException {
        this.optionsMenu.setText("Mis Reservas");
        this.misReservasList.setVisible(true);
        this.removeReservaButton.setVisible(true);
        this.modificarReservaButton.setVisible(true);
        this.downloadJustificanteButton.setVisible(true);
        this.vuelosDisponiblesReservaList.setVisible(false);
        this.reservarButton.setVisible(false);
        this.reservarLabel.setVisible(false);
        loadReservas();
    }


    // Cargar los vuelos en la ListView
    public void loadVuelos() throws SQLException {
        this.vuelosDisponiblesReservaList.getItems().clear();
        // Araylist de String con la informacion de los vuelos
        ArrayList<String> vuelos = getter.getlistaVuelosStrings();
        if (vuelos.size() > 0) {
            for (String vuelo : vuelos) {
                // Instroducirlo a la ListView formateado
                String[] parts = vuelo.replaceAll(" ", "").split("-");
                if (getter.getAsientosLibresCant(getter.getIDAvioFromVuelo(Integer.parseInt(parts[0])), Integer.parseInt(parts[0])) > 0) {
                    String res = parts[0] + "\nCiudad de Salida: " + new Getter().getNombreCiudad(Integer.parseInt(parts[1])) +
                            " \nCiuad de Destino: " + new Getter().getNombreCiudad(Integer.parseInt(parts[2])) +
                            "\nFecha Despegue: " + parts[5] + "/" + parts[4] + "/" + parts[3] +
                            "\nAsientos Disponibles: " + getter.getAsientosLibresCant(getter.getIDAvioFromVuelo(Integer.parseInt(parts[0])), Integer.parseInt(parts[0]));
                    this.vuelosDisponiblesReservaList.getItems().add(res);
                }
            }
        } else {  // No hay vuelos Disponibles
            Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.setTitle("Vuelos");
            dialog.setHeaderText("No hay vuelos dispnibles");
            dialog.show();
            this.vuelosDisponiblesReservaList.getItems().add("No hay vuelos Dispnibles");
        }

    }

    // Cargar las reservas de un usuario en la ListView
    public void loadReservas() throws SQLException {
        this.misReservasList.getItems().clear();
        // Araylist de String con la informacion de las reservas
        ArrayList<String> reservas = getter.getListaReservasUser(getter.getUsernameID(GlobalData.userName));
        if (reservas.size() > 0) {
            for (String reserva : reservas) {
                this.misReservasList.getItems().add(reserva);
            }
        } else {  // No hay reservas Disponibles
            Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.setTitle("Reservas");
            dialog.setHeaderText("No hay reservas dispnibles");
            dialog.show();
            this.misReservasList.getItems().add("No hay reservas dispnibles");
        }

    }

    // Terminar la Session
    private void endSession() throws IOException {
        App.setRoot("login");
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Session Terminada");
        dialog.setHeaderText("Sesión terminada con éxito");
        dialog.show();
    }

    // Funcion que se ejecuta al pulsar el boton de Reservar
    private void reservar() throws SQLException {
        // Sacamos los asientos libres y hacemos un ChoiceDialog con ellos
        String[] s = this.vuelosDisponiblesReservaList.getSelectionModel().getSelectedItem().split("\n");
        ArrayList<Integer> asientosLibres = getter.getAsientosLibres(getter.getIDAvioFromVuelo(Integer.parseInt(s[0])), Integer.parseInt(s[0]));
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(asientosLibres.get(0), asientosLibres);
        dialog.setTitle("Seleccionar Asiento");
        dialog.setContentText("Seleccione un asiento:");
        Optional<Integer> result = dialog.showAndWait();

        // Una vez tenemos el asiento
        if (result.isPresent()) {
            int selectedAsiento = result.get();
            // Hacemos la reserva
            int outReserva = gestioner.reservarVuelo(getter.getUsernameID(GlobalData.userName), Integer.parseInt(s[0]), selectedAsiento);
            if (outReserva != 0) {
                // Alerta de la confirmacion con opciones para descargar un PDF con al informaicion del vuelo
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Vuelo Reservado");
                alert.setHeaderText("¡Vuelo Reservado Exitosamente!");
                alert.setResizable(false);
                alert.setContentText("¿Quiere descargar un justificante del vuelo ahora?\n\nPodrá descargarlo siempre en el apartado de sus reservas.");
                ButtonType noThanksButton = new ButtonType("No, Gracias");
                ButtonType downloadButton = new ButtonType("Descargar");
                alert.getButtonTypes().setAll(noThanksButton, downloadButton);
                Optional<ButtonType> alertResult = alert.showAndWait();
                ButtonType button = alertResult.orElse(ButtonType.CANCEL);
                if (button == downloadButton) { // Aceptado
                    // Crear y descargar el PDF
                    Gestioner.createPDF(getter.getReservaInfo(outReserva));
                    Alert fin = new Alert(AlertType.CONFIRMATION);
                    fin.setTitle("PDF");
                    fin.setHeaderText("PDF descargado con exito");
                    fin.show();
                }
                loadVuelos();
            } else { // No reservado
                Alert fin = new Alert(AlertType.ERROR);
                fin.setTitle("PDF");
                fin.setHeaderText("Operacion Cancelada");
                fin.show();
            }
        }
    }

    public void descargarJustificante() throws SQLException {
        Gestioner.createPDF(this.misReservasList.getSelectionModel().getSelectedItem());
    }


}
