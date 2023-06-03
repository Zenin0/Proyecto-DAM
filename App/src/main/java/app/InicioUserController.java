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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static app.Gestioner.createPDF;

/**
 * Controlador del FXML iniciouser
 */
public class InicioUserController implements Initializable {

    // Tablas
    //// Tabla de Vuelos
    @FXML
    private TableView<Vuelo> vuelosDisponiblesTable;

    @FXML
    private TableColumn<Vuelo, String> IDVueloColumn;

    @FXML
    private TableColumn<Vuelo, String> asientosVueloColumn;

    @FXML
    private TableColumn<Vuelo, String> fechaVueloColumn;

    @FXML
    private TableColumn<Vuelo, String> llegadaVueloColum;

    @FXML
    private TableColumn<Vuelo, String> salidaVueloColum;

    //// Tabla de Reservas
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
    private Button downloadJustificanteButton;

    @FXML
    private Button endSession;

    @FXML
    private MenuButton menuciudadDestinoReservas;

    @FXML
    private MenuButton menuciudadDestinoVuelos;

    @FXML
    private MenuButton menuciudadSalidaReservas;

    @FXML
    private MenuButton menuciudadSalidaVuelos;

    @FXML
    private Button misReservasMenuItem;

    @FXML
    private Button modificarReservaButton;

    @FXML
    private Button removeReservaButton;

    @FXML
    private Button reservarButton;

    @FXML
    private Button reservarMeuItem;

    @FXML
    private Label tittleLabel;

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
                if (!reservasDisponiblesTable.getSelectionModel().isEmpty()) {
                    Reserva selectedReserva = reservasDisponiblesTable.getSelectionModel().getSelectedItem();
                    descargarJustificante(Getter.getReservaInfo(selectedReserva.getID()));
                } else {
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
     * Descargar un justufucante del vuelo con formato PDF
     *
     * @see Gestioner#createPDF(String, String)
     */
    public static void descargarJustificante(String PDFString) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Justificante de Vuelo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {

            if (createPDF(PDFString, selectedFile.getAbsolutePath())) {
                Alert fin = new Alert(AlertType.CONFIRMATION);
                fin.setTitle("PDF");
                fin.setHeaderText("PDF descargado con éxito");
                fin.setContentText("El pdf se descargó exitosamente en: " + selectedFile.getAbsolutePath());
                fin.show();
            } else {
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("No se pudo guardar el PDF");
                error.setContentText("Hubo un error al guardar el PDF. Por favor, inténtelo de nuevo.");
                error.show();
            }
        }
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
        Image before = new Image("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/seatBefore.png");

        final StackPane[] selectedSeatPane = {null};
        final ImageView[] selectedSeatImage = {null};

        for (int col = 1; col <= numCols; col++) {
            for (int row = 1; row <= numRows; row++) {
                int seatNum = (col - 1) * numRows + row;
                ImageView seatButton = new ImageView();
                seatButton.setId(String.valueOf(seatNum));
                seatButton.setFitWidth(50);
                seatButton.setFitHeight(50);

                Label seatLabel = new Label(String.valueOf(seatNum));
                seatLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-alignment: center;");
                seatLabel.setTranslateX(-6);
                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(seatButton, seatLabel);
                StackPane.setAlignment(seatLabel, Pos.CENTER);

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
                    if (seatNum == selectedReserva.getAsiento()) {
                        seatButton.setImage(before);
                    } else {
                        seatButton.setImage(unable);
                    }
                    stackPane.setOpacity(0.5);
                    stackPane.setDisable(true);
                }
                gridPane.add(stackPane, col, row);
            }

        }


        HBox legendBox = new HBox(10);


        ImageView ableImage = new ImageView(able);
        ImageView unableImage = new ImageView(unable);
        ImageView selectedImage = new ImageView(selected);
        ImageView beforeImage = new ImageView(before);

        Label ableLabel = new Label("Disponible");
        Label unableLabel = new Label("No Disponible");
        Label selectedLabel = new Label("Selecionado");
        Label beforeLabel = new Label("Actual");

        ableImage.setFitWidth(20);
        ableImage.setFitHeight(20);
        unableImage.setFitWidth(20);
        unableImage.setFitHeight(20);
        selectedImage.setFitWidth(20);
        selectedImage.setFitHeight(20);
        beforeImage.setFitWidth(20);
        beforeImage.setFitHeight(20);

        legendBox.getChildren().addAll(ableImage, ableLabel, unableImage, unableLabel, selectedImage, selectedLabel, beforeImage, beforeLabel);

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Seleccion de Asiento");


        VBox contentBox = new VBox(10);
        contentBox.getChildren().addAll(gridPane, legendBox);

        dialog.getDialogPane().setContent(contentBox);

        ButtonType noReservar = new ButtonType("Cancelar Modificación", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType reservar = new ButtonType("Modifcar", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().add(noReservar);
        dialog.getDialogPane().getButtonTypes().add(reservar);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == reservar) {
                int selectedAsiento = selectedSeat[0];

                if (selectedAsiento != -1) {
                    try {
                        int outReserva = Gestioner.modificarReserva(idreserva, selectedAsiento);
                        if (outReserva != 0) {

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Reserva Modificada");
                            alert.setHeaderText("¡Reserva Modificada Exitosamente!");
                            alert.setResizable(false);
                            alert.setContentText("¿Quiere descargar un justificante del vuelo ahora?\n\nPodrá descargarlo siempre en el apartado de sus reservas.");
                            ButtonType downloadButton = new ButtonType("Descargar");
                            ButtonType noThanksButton = new ButtonType("No, Gracias");
                            alert.getButtonTypes().setAll(noThanksButton, downloadButton);
                            Optional<ButtonType> alertResult = alert.showAndWait();
                            ButtonType button = alertResult.orElse(ButtonType.CANCEL);
                            if (button == downloadButton) {

                                descargarJustificante(Getter.getReservaInfo(outReserva));
                            }
                            loadVuelos();
                        } else {
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
        ObservableList<Vuelo> vuelos = Getter.getVuelosObservableList(Getter.getIDCiudad(this.menuciudadSalidaVuelos.getText()), Getter.getIDCiudad(this.menuciudadDestinoVuelos.getText()));
        if (vuelos.size() < 1) {
            Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.setTitle("No Encontrado");
            dialog.setHeaderText("Ningún vuelo encontrado");
            dialog.show();
            this.vuelosDisponiblesTable.getItems().clear();
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

        ObservableList<Reserva> reservas = Getter.getReservasObservableList(Getter.getUsernameID(GlobalData.userName), Getter.getIDCiudad(this.menuciudadSalidaReservas.getText()), Getter.getIDCiudad(this.menuciudadDestinoReservas.getText()));
        if (reservas.size() < 1) {
            Alert dialog = new Alert(AlertType.INFORMATION);
            dialog.setTitle("No Encontrado");
            dialog.setHeaderText("Ningúna reserva encontrada");
            dialog.show();
            this.reservasDisponiblesTable.getItems().clear();
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
        GlobalData.userName = "";
        App.setRoot("login");
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Session Terminada");
        dialog.setHeaderText("¡Sesión terminada con éxito!");
        dialog.show();
    }

    /**
     * Reservar un vuelo seleccionado
     *
     * @see #modificarReserva()
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

                    ImageView seatButton = new ImageView();
                    seatButton.setId(String.valueOf(seatNum));
                    seatButton.setFitWidth(50);
                    seatButton.setFitHeight(50);

                    Label seatLabel = new Label(String.valueOf(seatNum));
                    seatLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-alignment: center;");
                    seatLabel.setTranslateX(-6);
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(seatButton, seatLabel);
                    StackPane.setAlignment(seatLabel, Pos.CENTER);

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

            HBox legendBox = new HBox(10);


            ImageView ableImage = new ImageView(able);
            ImageView unableImage = new ImageView(unable);
            ImageView selectedImage = new ImageView(selected);


            Label ableLabel = new Label("Disponible");
            Label unableLabel = new Label("No Disponible");
            Label selectedLabel = new Label("Seleccionado");


            ableImage.setFitWidth(20);
            ableImage.setFitHeight(20);
            unableImage.setFitWidth(20);
            unableImage.setFitHeight(20);
            selectedImage.setFitWidth(20);
            selectedImage.setFitHeight(20);


            legendBox.getChildren().addAll(ableImage, ableLabel, unableImage, unableLabel, selectedImage, selectedLabel);


            Dialog<Integer> dialog = new Dialog<>();
            dialog.setTitle("Seleccion de Asiento");


            VBox contentBox = new VBox(10);
            contentBox.getChildren().addAll(gridPane, legendBox);

            dialog.getDialogPane().setContent(contentBox);

            ButtonType noReservar = new ButtonType("Cancelar Reserva", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType reservar = new ButtonType("Reservar", ButtonBar.ButtonData.OK_DONE);

            dialog.getDialogPane().getButtonTypes().add(noReservar);
            dialog.getDialogPane().getButtonTypes().add(reservar);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == reservar) {
                    int selectedAsiento = selectedSeat[0];

                    if (selectedAsiento != -1) {
                        try {
                            int outReserva = Gestioner.reservarVuelo(Getter.getUsernameID(GlobalData.userName), vueloID, selectedAsiento);
                            if (outReserva != 0) {

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
                                if (button == downloadButton) {

                                    descargarJustificante(Getter.getReservaInfo(outReserva));
                                }
                                loadVuelos();
                            } else {
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
     * Modificar la vista para reservar un vueloi
     *
     * @see #menuMisReservas()
     */
    public void menuReservar() throws SQLException {

        this.menuciudadSalidaVuelos.getItems().clear();
        this.menuciudadDestinoVuelos.getItems().clear();
        this.menuciudadSalidaVuelos.setText("Cualquiera");
        this.menuciudadDestinoVuelos.setText("Cualquiera");

        MenuItem emptyItem = new MenuItem("Cualquiera");
        emptyItem.setOnAction(event -> {
            menuciudadDestinoVuelos.setText("Cualquiera");
            try {
                loadVuelos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuciudadDestinoVuelos.getItems().add(emptyItem);

        for (String ciudad : Getter.getlistaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> {
                menuciudadDestinoVuelos.setText(item.getText());
                try {
                    loadVuelos();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            menuciudadDestinoVuelos.getItems().add(item);
        }
        menuciudadDestinoVuelos.setPopupSide(Side.BOTTOM);


        emptyItem = new MenuItem("Cualquiera");
        emptyItem.setOnAction(event -> {
            menuciudadSalidaVuelos.setText("Cualquiera");
            try {
                loadVuelos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuciudadSalidaVuelos.getItems().add(emptyItem);

        for (String ciudad : Getter.getlistaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> {
                menuciudadSalidaVuelos.setText(item.getText());
                try {
                    loadVuelos();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            menuciudadSalidaVuelos.getItems().add(item);
        }

        menuciudadSalidaVuelos.setPopupSide(Side.BOTTOM);
        this.tittleLabel.setText("Vuelos");
        this.menuciudadDestinoVuelos.setVisible(true);
        this.menuciudadSalidaVuelos.setVisible(true);
        this.misReservasMenuItem.setVisible(true);
        this.vuelosDisponiblesTable.setVisible(true);
        this.reservarButton.setVisible(true);
        this.reservasDisponiblesTable.setVisible(false);
        this.removeReservaButton.setVisible(false);
        this.modificarReservaButton.setVisible(false);
        this.downloadJustificanteButton.setVisible(false);
        this.reservarMeuItem.setVisible(false);
        this.menuciudadSalidaReservas.setVisible(false);
        this.menuciudadDestinoReservas.setVisible(false);
        loadVuelos();
    }

    /**
     * Modificar la vista para ver mis reservas
     */
    public void menuMisReservas() throws SQLException {

        this.menuciudadDestinoReservas.getItems().clear();
        this.menuciudadSalidaReservas.getItems().clear();
        this.menuciudadDestinoReservas.setText("Cualquiera");
        this.menuciudadSalidaReservas.setText("Cualquiera");

        MenuItem emptyItem = new MenuItem("Cualquiera");
        emptyItem.setOnAction(event -> {
            menuciudadDestinoReservas.setText("Cualquiera");
            try {
                loadReservas();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuciudadDestinoReservas.getItems().add(emptyItem);

        for (String ciudad : Getter.getlistaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> {
                menuciudadDestinoReservas.setText(item.getText());
                try {
                    loadReservas();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            menuciudadDestinoReservas.getItems().add(item);
        }
        menuciudadDestinoReservas.setPopupSide(Side.BOTTOM);


        emptyItem = new MenuItem("Cualquiera");
        emptyItem.setOnAction(event -> {
            menuciudadSalidaReservas.setText("Cualquiera");
            try {
                loadReservas();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        menuciudadSalidaReservas.getItems().add(emptyItem);

        for (String ciudad : Getter.getlistaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> {
                menuciudadSalidaReservas.setText(item.getText());
                try {
                    loadReservas();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            menuciudadSalidaReservas.getItems().add(item);
        }
        this.tittleLabel.setText("Mis Reservas");
        this.menuciudadSalidaReservas.setVisible(true);
        this.menuciudadDestinoReservas.setVisible(true);
        this.menuciudadSalidaVuelos.getItems().clear();
        this.menuciudadDestinoVuelos.getItems().clear();
        this.reservarMeuItem.setVisible(true);
        this.reservasDisponiblesTable.setVisible(true);
        this.removeReservaButton.setVisible(true);
        this.modificarReservaButton.setVisible(true);
        this.downloadJustificanteButton.setVisible(true);
        this.vuelosDisponiblesTable.setVisible(false);
        this.reservarButton.setVisible(false);
        this.misReservasMenuItem.setVisible(false);
        this.menuciudadDestinoVuelos.setVisible(false);
        this.menuciudadSalidaVuelos.setVisible(false);
        loadReservas();
    }


}
