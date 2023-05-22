package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class InicioUserController implements Initializable {

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
        this.reservarMeuItem.setOnAction(event -> {
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

        this.removeReservaButton.setOnAction(event -> {
            try {
                eliminarReserva();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

    }


    public void eliminarReserva() throws SQLException {
        String idreserva = this.misReservasList.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "").substring(0, 1);
        Gestioner.eliminarReserva(Integer.parseInt(idreserva));
        loadReservas();
    }


    // Cargar los vuelos en la ListView
    public void loadVuelos() throws SQLException {
        this.vuelosDisponiblesReservaList.getItems().clear();
        // Araylist de String con la informacion de los vuelos
        ArrayList<String> vuelos = Getter.getlistaVuelosStrings();
        if (vuelos.size() > 0) {
            for (String vuelo : vuelos) {
                // Instroducirlo a la ListView formateado
                String[] parts = vuelo.replaceAll(" ", "").split("-");
                if (Getter.getAsientosLibresCant(Getter.getIDAvioFromVuelo(Integer.parseInt(parts[0])), Integer.parseInt(parts[0])) > 0) {
                    String res = parts[0] + "\nCiudad de Salida: " + Getter.getNombreCiudad(Integer.parseInt(parts[1])) +
                            " \nCiuad de Destino: " + Getter.getNombreCiudad(Integer.parseInt(parts[2])) +
                            "\nFecha Despegue: " + parts[5] + "/" + parts[4] + "/" + parts[3] +
                            "\nAsientos Disponibles: " + Getter.getAsientosLibresCant(Getter.getIDAvioFromVuelo(Integer.parseInt(parts[0])), Integer.parseInt(parts[0]));
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
        ArrayList<String> reservas = Getter.getListaReservasUser(Getter.getUsernameID(GlobalData.userName));
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
        String[] s = this.vuelosDisponiblesReservaList.getSelectionModel().getSelectedItem().split("\n");
        int vueloID = Integer.parseInt(s[0]);
        int numAsientos = Getter.getAsientosLibres(Getter.getIDAvioFromVuelo(vueloID), vueloID).size();
        int numCols = Getter.getNumCols(numAsientos);
        int numRows = Getter.getNumRows(numAsientos, numCols);

        ArrayList<Integer> asientosLibres = Getter.getAsientosLibres(Getter.getIDAvioFromVuelo(vueloID), vueloID);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        final int[] selectedSeat = {-1};

        for (int row = 1; row <= numRows; row++) {
            for (int col = 1; col <= numCols; col++) {
                int seatNum = (row - 1) * numCols + col;
                Button seatButton = new Button(String.valueOf(seatNum));
                seatButton.setPrefSize(50, 50);

                /* Añadir Imagenes de Asientos
                Image image = new Image("https://cdn-icons-png.flaticon.com/512/99/99342.png");
                ImageView seatButton = new ImageView(image);
                seatButton.setId(String.valueOf(seatNum));
                seatButton.setFitWidth(50);
                seatButton.setFitHeight(50);
                 */


                if (asientosLibres.contains(seatNum)) {
                    seatButton.setOnAction(event -> selectedSeat[0] = Integer.parseInt(seatButton.getText()));
                } else {
                    seatButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #000000");
                    seatButton.setDisable(true);
                }
                gridPane.add(seatButton, col, row);
            }
        }

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Seleccionar Asiento");
        dialog.getDialogPane().setContent(gridPane);

        ButtonType noReservar = new ButtonType("Cancelar Reserva", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType reservar = new ButtonType("Reservar", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().add(noReservar);
        dialog.getDialogPane().getButtonTypes().add(reservar);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == reservar) {
                int selectedAsiento = selectedSeat[0];
                // Hacemos la reserva
                if (selectedAsiento != -1) {
                    try {
                        int outReserva = Gestioner.reservarVuelo(Getter.getUsernameID(GlobalData.userName), Integer.parseInt(s[0]), selectedAsiento);

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
                                Gestioner.createPDF(Getter.getReservaInfo(outReserva));
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
                        return outReserva;
                    } catch (SQLException e) {
                        Alert sqlerror = new Alert(AlertType.ERROR);
                        sqlerror.setTitle("ERROR");
                        sqlerror.setHeaderText(e.getMessage());
                        sqlerror.show();
                    }
                } else {
                    Alert fin = new Alert(AlertType.ERROR);
                    fin.setTitle("Seleccion de Asiento");
                    fin.setHeaderText("Operacion Cancelada");
                    fin.show();
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    public void descargarJustificante() throws SQLException {
        Gestioner.createPDF(this.misReservasList.getSelectionModel().getSelectedItem());
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


}
