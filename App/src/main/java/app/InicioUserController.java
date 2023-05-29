package app;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador del FXML iniciouser
 */
public class InicioUserController implements Initializable {

    // Tabla Vuelos
    @FXML
    private TableView<Vuelo> vuelosDisponiblesTable;

    @FXML
    private TableColumn<Vuelo, String> IDVueloColumn;

    @FXML
    private TableColumn<Vuelo, String> salidaVueloColum;

    @FXML
    private TableColumn<Vuelo, String> asientosVueloColumn;

    @FXML
    private TableColumn<Vuelo, String> fechaVueloColumn;

    @FXML
    private TableColumn<Vuelo, String> llegadaVueloColum;

    // Tabla Reservas
    @FXML
    private TableView<Reserva> reservasDisponiblesTable;

    @FXML
    private TableColumn<Reserva, String> IDReservaColum;

    @FXML
    private TableColumn<Reserva, String> asientoReservaColum;

    @FXML
    private TableColumn<Reserva, String> avionReservaColumn;

    @FXML
    private TableColumn<Reserva, String> ciudadDestinoReservaColumn;

    @FXML
    private TableColumn<Reserva, String> ciudadSaludaReservaColumn;

    @FXML
    private TableColumn<Reserva, String> fechaReservaColum;

    @FXML
    private TableColumn<Reserva, String> nameReservaColum;

    @FXML
    private Label ciudadesDestinoLabel;

    @FXML
    private Label ciudadesSalidaLabel;

    @FXML
    private Button downloadJustificanteButton;

    @FXML
    private Button endSession;

    @FXML
    private MenuButton menuciudadDestino;

    @FXML
    private MenuButton menuciudadSalida;

    @FXML
    private Label misReservasLabel;

    @FXML
    private Button misReservasMenuItem;

    @FXML
    private Button modificarReservaButton;

    @FXML
    private Button removeReservaButton;

    @FXML
    private Button reservarButton;

    @FXML
    private Label reservarLabel;

    @FXML
    private Button reservarMeuItem;

    public InicioUserController() {
    }

    /**
     * Inicializar la ventana
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        try {
            menuReservar();
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText(e.getMessage());
            dialog.show();
        }
        this.reservarMeuItem.setOnAction(event -> {
            try {
                menuReservar();
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
                if (!vuelosDisponiblesTable.getSelectionModel().isEmpty())
                    reservar();
                else {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Seleccione un vuelo antes de intentar reservar");
                    dialog.show();
                }
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

        this.downloadJustificanteButton.setOnAction(event -> {
            try {
                if (!reservasDisponiblesTable.getSelectionModel().isEmpty())
                    descargarJustificante();
                else {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Seleccione una reserva antes de poder descargar un justificante");
                    dialog.show();
                }
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

        this.removeReservaButton.setOnAction(event -> {
            try {
                if (!reservasDisponiblesTable.getSelectionModel().isEmpty())
                    eliminarReserva();
                else {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Seleccione una reserva antes de eliminarla");
                    dialog.show();
                }
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });
        this.modificarReservaButton.setOnAction(event -> {
            try {
                if (!reservasDisponiblesTable.getSelectionModel().isEmpty()) {
                    modificarReserva();
                    this.reservasDisponiblesTable.getItems().clear();
                    loadReservas();
                } else {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Seleccione una reserva antes de modificarla");
                    dialog.show();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * Modificar una Reserva
     */
    private void modificarReserva() throws SQLException {
        Reserva selectedReserva = this.reservasDisponiblesTable.getSelectionModel().getSelectedItem();
        int idreserva = selectedReserva.getID();

        int vueloID = Getter.getIDVueloFromIDReserva(idreserva);
        int numAsientos = Getter.getAsientosLibres(Getter.getIDAvioFromVuelo(vueloID), vueloID).size();
        int numCols = Getter.getNumCols(numAsientos);
        int numRows = Getter.getNumRows(numAsientos, numCols);

        ArrayList<Integer> asientosLibres = Getter.getAsientosLibres(Getter.getIDAvioFromVuelo(vueloID), vueloID);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        final int[] selectedSeat = {-1};
        Image able = new Image("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/seatAble.png");
        Image unable = new Image("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/seatUnable.png");
        Image selected = new Image("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/seatSelected.png");

        final StackPane[] selectedSeatPane = {null};
        final ImageView[] selectedSeatImage = {null};

        for (int col = 1; col <= numCols; col++) {
            for (int row = 1; row <= numRows; row++) {
                int seatNum = (col - 1) * numRows + row;
                // Añadir Imagenes
                ImageView seatButton = new ImageView();
                seatButton.setId(String.valueOf(seatNum));
                seatButton.setFitWidth(50);
                seatButton.setFitHeight(50);

                // Añadir el numero de asiento (ID)
                Label seatLabel = new Label(String.valueOf(seatNum));
                seatLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-alignment: center;");
                seatLabel.setTranslateX(-6);
                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(seatButton, seatLabel);
                StackPane.setAlignment(seatLabel, Pos.CENTER);


                // Asignar que ID hemos pulsado, y cambiar opacidades
                if (asientosLibres.contains(seatNum)) {
                    seatButton.setImage(able);
                    stackPane.setOnMouseClicked(event -> {
                        if (selectedSeatPane[0] != null) {
                            selectedSeatImage[0].setImage(able);
                        }
                        selectedSeat[0] = Integer.parseInt(seatButton.getId());
                        selectedSeatPane[0] = stackPane;
                        selectedSeatImage[0] = seatButton;
                        selectedSeatImage[0].setImage(selected);
                    });
                    stackPane.setOnMouseEntered(event -> stackPane.setOpacity(0.5));
                    stackPane.setOnMouseExited(event -> stackPane.setOpacity(1));
                    stackPane.cursorProperty().set(Cursor.HAND);
                } else {
                    seatButton.setImage(unable);
                    stackPane.setOpacity(0.5);
                    stackPane.setDisable(true);
                }

                gridPane.add(stackPane, col, row);
            }

        }


        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Seleccion de  Asiento");
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
                        int outReserva = Gestioner.modificarReserva(idreserva, selectedAsiento);
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
                    fin.setTitle("Selección de Asiento");
                    fin.setHeaderText("Operación Cancelada");
                    fin.show();
                }
            }
            return null;
        });
        dialog.showAndWait();
    }


    /**
     * Elimina una reserva seleccionada
     *
     * @see Gestioner#eliminarReserva(int)
     */
    public void eliminarReserva() throws SQLException {
        Reserva selectedReserva = this.reservasDisponiblesTable.getSelectionModel().getSelectedItem();
        Gestioner.eliminarReserva(selectedReserva.getID());
        loadReservas();
    }


    /**
     * Cargar los vuelos en la lista
     *
     * @see #loadReservas()
     */
    public void loadVuelos() throws SQLException {
        ObservableList<Vuelo> vuelos = Getter.getVuelosObservableList(Getter.getIDCiudad(this.menuciudadSalida.getText()), Getter.getIDCiudad(this.menuciudadDestino.getText()));
        if (vuelos.size() < 1) {
            Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.setTitle("No Encontrado");
            dialog.setHeaderText("Ningún vuelo encontrado con esos parámetros");
            dialog.show();
        } else {
            this.IDVueloColumn.setStyle("-fx-alignment: CENTER");
            this.vuelosDisponiblesTable.setStyle("-fx-alignment: CENTER");
            this.salidaVueloColum.setStyle("-fx-alignment: CENTER");
            this.llegadaVueloColum.setStyle("-fx-alignment: CENTER");
            this.fechaVueloColumn.setStyle("-fx-alignment: CENTER");
            this.asientosVueloColumn.setStyle("-fx-alignment: CENTER");

            this.IDVueloColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            this.salidaVueloColum.setCellValueFactory(new PropertyValueFactory<>("Ciudad_Salida"));
            this.llegadaVueloColum.setCellValueFactory(new PropertyValueFactory<>("Ciudad_Llegada"));
            this.fechaVueloColumn.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
            this.asientosVueloColumn.setCellValueFactory(new PropertyValueFactory<>("Asientos"));
            this.vuelosDisponiblesTable.setItems(vuelos);
        }

    }

    /**
     * Cargar las reservas de un usuario
     *
     * @see #loadVuelos()
     */
    public void loadReservas() throws SQLException {

        ObservableList<Reserva> reservas = Getter.getReservasObservableList(Getter.getUsernameID(GlobalData.userName));
        if (reservas.size() < 1) {
            Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.setTitle("No Encontrado");
            dialog.setHeaderText("Ningún vuelo encontrado con esos parámetros");
            dialog.show();
        } else {
            this.IDReservaColum.setStyle("-fx-alignment: CENTER");
            this.nameReservaColum.setStyle("-fx-alignment: CENTER");
            this.asientoReservaColum.setStyle("-fx-alignment: CENTER");
            this.ciudadSaludaReservaColumn.setStyle("-fx-alignment: CENTER");
            this.ciudadDestinoReservaColumn.setStyle("-fx-alignment: CENTER");
            this.avionReservaColumn.setStyle("-fx-alignment: CENTER");
            this.fechaReservaColum.setStyle("-fx-alignment: CENTER");

            this.IDReservaColum.setCellValueFactory(new PropertyValueFactory<>("ID"));
            this.nameReservaColum.setCellValueFactory(new PropertyValueFactory<>("nameSubname"));
            this.asientoReservaColum.setCellValueFactory(new PropertyValueFactory<>("Asiento"));
            this.ciudadSaludaReservaColumn.setCellValueFactory(new PropertyValueFactory<>("CiudadSalida"));
            this.ciudadDestinoReservaColumn.setCellValueFactory(new PropertyValueFactory<>("CiudadDestino"));
            this.avionReservaColumn.setCellValueFactory(new PropertyValueFactory<>("Avion"));
            this.fechaReservaColum.setCellValueFactory(new PropertyValueFactory<>("Fecha"));
            this.reservasDisponiblesTable.setItems(reservas);
        }

    }

    /**
     * Cerrar la sesión
     */
    private void endSession() throws IOException {
        App.setRoot("login");
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Session Terminada");
        dialog.setHeaderText("¡Sesión terminada con éxito!");
        dialog.show();
    }

    /**
     * Reservar un vuelo seleccionado
     *
     * @see #reservar()
     * @see Getter
     * @see Gestioner
     */
    private void reservar() throws SQLException {

        Vuelo selectedVuelo = vuelosDisponiblesTable.getSelectionModel().getSelectedItem();

        if (selectedVuelo != null) {
            int vueloID = selectedVuelo.getId();
            int numAsientos = Getter.getAsientosLibres(Getter.getIDAvioFromVuelo(vueloID), vueloID).size();
            int numCols = Getter.getNumCols(numAsientos);
            int numRows = Getter.getNumRows(numAsientos, numCols);

            ArrayList<Integer> asientosLibres = Getter.getAsientosLibres(Getter.getIDAvioFromVuelo(vueloID), vueloID);
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            final int[] selectedSeat = {-1};
            Image able = new Image("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/seatAble.png");
            Image unable = new Image("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/seatUnable.png");
            Image selected = new Image("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/seatSelected.png");

            final StackPane[] selectedSeatPane = {null};
            final ImageView[] selectedSeatImage = {null};

            for (int col = 1; col <= numCols; col++) {
                for (int row = 1; row <= numRows; row++) {
                    int seatNum = (col - 1) * numRows + row;
                    // Añadir Imagenes
                    ImageView seatButton = new ImageView();
                    seatButton.setId(String.valueOf(seatNum));
                    seatButton.setFitWidth(50);
                    seatButton.setFitHeight(50);

                    // Añadir el numero de asiento (ID)
                    Label seatLabel = new Label(String.valueOf(seatNum));
                    seatLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-alignment: center;");
                    seatLabel.setTranslateX(-6);
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(seatButton, seatLabel);
                    StackPane.setAlignment(seatLabel, Pos.CENTER);


                    // Asignar que ID hemos pulsado, y cambiar opacidades
                    if (asientosLibres.contains(seatNum)) {
                        seatButton.setImage(able);
                        stackPane.setOnMouseClicked(event -> {
                            if (selectedSeatPane[0] != null) {
                                selectedSeatImage[0].setImage(able);
                            }
                            selectedSeat[0] = Integer.parseInt(seatButton.getId());
                            selectedSeatPane[0] = stackPane;
                            selectedSeatImage[0] = seatButton;
                            selectedSeatImage[0].setImage(selected);
                        });
                        stackPane.setOnMouseEntered(event -> stackPane.setOpacity(0.5));
                        stackPane.setOnMouseExited(event -> stackPane.setOpacity(1));
                        stackPane.cursorProperty().set(Cursor.HAND);
                    } else {
                        seatButton.setImage(unable);
                        stackPane.setOpacity(0.5);
                        stackPane.setDisable(true);
                    }

                    gridPane.add(stackPane, col, row);
                }
            }


            Dialog<Integer> dialog = new Dialog<>();
            dialog.setTitle("Seleccion de Asiento");
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
                            int outReserva = Gestioner.reservarVuelo(Getter.getUsernameID(GlobalData.userName), vueloID, selectedAsiento);
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
                                fin.setHeaderText("Operación Cancelada");
                                fin.show();
                            }
                            return outReserva;
                        } catch (SQLException e) {
                            Alert sqlerror = new Alert(AlertType.ERROR);
                            sqlerror.setTitle("ERROR");
                            sqlerror.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
                            sqlerror.show();
                        }
                    } else {
                        Alert fin = new Alert(AlertType.ERROR);
                        fin.setTitle("Selección de Asiento");
                        fin.setHeaderText("Operación Cancelada");
                        fin.show();
                    }
                }
                return null;
            });
            dialog.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se ha seleccionado ningún vuelo.");
            alert.show();
        }

    }


    /**
     * Descargar un justufucante del vuelo
     *
     * @see Gestioner#createPDF(String)
     */
    public void descargarJustificante() throws SQLException {
        Reserva selectedReserva = reservasDisponiblesTable.getSelectionModel().getSelectedItem();
        if (selectedReserva != null) {
            int selectedID = selectedReserva.getID();
        Gestioner.createPDF(Getter.getReservaInfo(selectedReserva.getID()));
        }
    }


    /**
     * Modificar la vista para reservar un vueloi
     */
    public void menuReservar() throws SQLException {


        MenuItem emptyItem = new MenuItem("Cualquiera");
        emptyItem.setOnAction(event -> {
            menuciudadDestino.setText("Cualquiera");
            try {
                loadVuelos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuciudadDestino.getItems().add(emptyItem);

        for (String ciudad : Getter.getlistaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> {
                menuciudadDestino.setText(item.getText());
                try {
                    loadVuelos();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            menuciudadDestino.getItems().add(item);
        }
        menuciudadDestino.setPopupSide(Side.BOTTOM);


        emptyItem = new MenuItem("Cualquiera");
        emptyItem.setOnAction(event -> {
            menuciudadSalida.setText("Cualquiera");
            try {
                loadVuelos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuciudadSalida.getItems().add(emptyItem);

        for (String ciudad : Getter.getlistaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> {
                menuciudadSalida.setText(item.getText());
                try {
                    loadVuelos();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            menuciudadSalida.getItems().add(item);
        }

        menuciudadSalida.setPopupSide(Side.BOTTOM);
        this.ciudadesDestinoLabel.setVisible(true);
        this.ciudadesSalidaLabel.setVisible(true);
        this.menuciudadDestino.setVisible(true);
        this.menuciudadSalida.setVisible(true);
        this.misReservasMenuItem.setVisible(true);
        this.vuelosDisponiblesTable.setVisible(true);
        this.reservarButton.setVisible(true);
        this.reservarLabel.setVisible(true);
        this.reservasDisponiblesTable.setVisible(false);
        this.removeReservaButton.setVisible(false);
        this.modificarReservaButton.setVisible(false);
        this.downloadJustificanteButton.setVisible(false);
        this.misReservasLabel.setVisible(false);
        this.reservarMeuItem.setVisible(false);
        loadVuelos();
    }

    /**
     * Modificar la vista para ver mis reservas
     */
    public void menuMisReservas() throws SQLException {
        this.menuciudadSalida.getItems().clear();
        this.menuciudadDestino.getItems().clear();
        this.reservarMeuItem.setVisible(true);
        this.reservasDisponiblesTable.setVisible(true);
        this.misReservasLabel.setVisible(true);
        this.removeReservaButton.setVisible(true);
        this.modificarReservaButton.setVisible(true);
        this.downloadJustificanteButton.setVisible(true);
        this.vuelosDisponiblesTable.setVisible(false);
        this.reservarButton.setVisible(false);
        this.reservarLabel.setVisible(false);
        this.misReservasMenuItem.setVisible(false);
        this.menuciudadDestino.setVisible(false);
        this.menuciudadSalida.setVisible(false);
        this.ciudadesDestinoLabel.setVisible(false);
        this.ciudadesSalidaLabel.setVisible(false);
        loadReservas();
    }


}
