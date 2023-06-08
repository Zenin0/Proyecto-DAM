package app;

import com.itextpdf.text.DocumentException;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static app.ManoloAirlines.createPDF;

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


    @FXML
    private ImageView imagenUsuario;

    /**
     * Descargar un justufucante del vuelo con formato PDF
     *
     * @see ManoloAirlines#createPDF(String, String)
     */
    public static void descargarJustificante(String PDFString) throws SQLException, DocumentException, IOException {
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
     * Inicializar la ventana
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        try {
            loadImagen();
            menuReservar();
        } catch (SQLException e) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        this.reservarMeuItem.setOnAction(event -> {
            try {
                menuReservar();
            } catch (SQLException e) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }
        });

        this.misReservasMenuItem.setOnAction(event -> {
            try {
                menuMisReservas();
            } catch (SQLException e) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
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
                if (!vuelosDisponiblesTable.getSelectionModel().isEmpty()) reservar();
                else {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Seleccione un vuelo antes de intentar reservar");
                    dialog.show();
                }
            } catch (SQLException e) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }
        });

        this.downloadJustificanteButton.setOnAction(event -> {
            try {
                if (!reservasDisponiblesTable.getSelectionModel().isEmpty()) {
                    Reserva selectedReserva = reservasDisponiblesTable.getSelectionModel().getSelectedItem();
                    descargarJustificante(ManoloAirlines.getReservaInfo(selectedReserva.getID()));
                } else {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Seleccione una reserva antes de poder descargar un justificante");
                    dialog.show();
                }
            } catch (SQLException e) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            } catch (DocumentException | IOException e) {
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("No se pudo guardar el PDF");
                error.setContentText("Hubo un error al guardar el PDF. Por favor, inténtelo de nuevo.");
                error.show();
            }
        });

        this.removeReservaButton.setOnAction(event -> {
            try {
                if (!reservasDisponiblesTable.getSelectionModel().isEmpty()) eliminarReserva();
                else {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Seleccione una reserva antes de eliminarla");
                    dialog.show();
                }
            } catch (SQLException e) {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
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
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }
        });

        this.vuelosDisponiblesTable.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (vuelosDisponiblesTable.getSelectionModel().getSelectedItem() != null) {
                    try {
                        reservar();
                    } catch (SQLException e) {
                        Alert dialog = new Alert(Alert.AlertType.ERROR);
                        dialog.setTitle("ERROR");
                        dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
                        dialog.show();
                    }
                }
            }
        });

        this.reservasDisponiblesTable.setOnKeyPressed((event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (reservasDisponiblesTable.getSelectionModel().getSelectedItem() != null) {
                    try {
                        modificarReserva();
                    } catch (SQLException e) {
                        Alert dialog = new Alert(Alert.AlertType.ERROR);
                        dialog.setTitle("ERROR");
                        dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
                        dialog.show();
                    }
                }
            }

        });
        this.imagenUsuario.setOnMouseClicked(event -> {
            try {
                App.setRoot("myAccount");
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });


    }

    /**
     * Cargar la imagen del usuario
     *
     * @throws SQLException Error en la consulta
     */
    private void loadImagen() throws SQLException {
        String query = "SELECT Image FROM Usuarios WHERE Nombre_Usuario = ?";
        PreparedStatement statement = App.con.prepareStatement(query);
        statement.setString(1, GlobalData.userName);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            InputStream imageFile = rs.getBinaryStream("Image");
            if (imageFile != null) {
                imagenUsuario.setImage(new Image(imageFile));
                applyCircularMask(imagenUsuario);
            } else {
                imagenUsuario.setImage(new Image("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/user.png"));
            }
        }
    }

    /**
     * Aplicar una mascara de ciruclo a la imagen de perfil
     *
     * @param imageView ImageView
     * @see #loadImagen()
     */
    private void applyCircularMask(ImageView imageView) {
        Circle clip = new Circle();
        clip.setCenterX(imageView.getFitWidth() / 2);
        clip.setCenterY(imageView.getFitHeight() / 2);
        clip.setRadius(imageView.getFitWidth() / 2);
        imageView.setClip(clip);
    }

    /**
     * Modificar una reserva
     *
     * @throws SQLException Error en la consulta
     * @see #reservar()
     * @see #eliminarReserva()
     * @see Reserva
     */
    private void modificarReserva() throws SQLException {
        Reserva selectedReserva = this.reservasDisponiblesTable.getSelectionModel().getSelectedItem();

        // * Cargar la ventana de seleccion de asiento
        int idreserva = selectedReserva.getID();

        int vueloID = ManoloAirlines.getIDVueloFromIDReserva(idreserva);
        int numAsientos = ManoloAirlines.getCapacidadAvion(ManoloAirlines.getIDAvioFromVuelo(vueloID));
        int numCols = ManoloAirlines.getNumCols(numAsientos);
        int numRows = ManoloAirlines.getNumRows(numAsientos, numCols);

        ArrayList<Integer> asientosLibres = ManoloAirlines.getAsientosLibres(ManoloAirlines.getIDAvioFromVuelo(vueloID), vueloID);
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

        // ? Quiere reservar
        dialog.setResultConverter(buttonType -> {
            if (buttonType == reservar) {
                int selectedAsiento = selectedSeat[0];

                // ? Asiento seleccionado
                if (selectedAsiento != -1) {
                    try {
                        // * ID de la reserva
                        ManoloAirlines.modificarReserva(idreserva, selectedAsiento);
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

                            descargarJustificante(ManoloAirlines.getReservaInfo(idreserva));
                        }
                        loadReservas();
                        return idreserva;
                    } catch (SQLException e) {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("ERROR");
                        error.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
                        error.show();
                    } catch (DocumentException | IOException e) {
                        Alert error = new Alert(AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText("No se pudo guardar el PDF");
                        error.setContentText("Hubo un error al guardar el PDF. Por favor, inténtelo de nuevo.");
                        error.show();
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
     * Eliminar una reserva
     *
     * @throws SQLException Error en la eliminacion
     * @see #modificarReserva()
     * @see #reservar()
     */
    public void eliminarReserva() throws SQLException {
        Reserva selectedReserva = this.reservasDisponiblesTable.getSelectionModel().getSelectedItem();
        ManoloAirlines.eliminarReserva(selectedReserva.getID());
        loadReservas();
    }

    /**
     * Cargar la lista de los vuelos para reservarlos
     *
     * @throws SQLException Error en la consulta
     * @see #loadReservas()
     */
    public void loadVuelos() throws SQLException {
        ObservableList<Vuelo> vuelos = ManoloAirlines.getVuelosObservableList(ManoloAirlines.getIDCiudad(this.menuciudadSalidaVuelos.getText()), ManoloAirlines.getIDCiudad(this.menuciudadDestinoVuelos.getText()));
        // ? Hay vuelos
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
            this.vuelosDisponiblesTable.getSelectionModel().selectFirst();
        }

    }

    /**
     * Cargar la lista de reservas del usuario
     *
     * @throws SQLException Error en la consulta
     * @see #loadVuelos()
     */
    public void loadReservas() throws SQLException {

        ObservableList<Reserva> reservas = ManoloAirlines.getReservasObservableList(ManoloAirlines.getUsernameID(GlobalData.userName), ManoloAirlines.getIDCiudad(this.menuciudadSalidaReservas.getText()), ManoloAirlines.getIDCiudad(this.menuciudadDestinoReservas.getText()));
        // ? Hay reservas
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
            this.reservasDisponiblesTable.getSelectionModel().selectFirst();
        }

    }

    /**
     * Cerrar la sesion
     *
     * @throws IOException Error al cambiar de FXML
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
     * Reservar un vuelo
     *
     * @throws SQLException Error en la consulta
     * @see #modificarReserva()
     * @see #eliminarReserva()
     * @see Reserva
     */
    private void reservar() throws SQLException {

        Vuelo selectedVuelo = vuelosDisponiblesTable.getSelectionModel().getSelectedItem();
        // * Cargar la ventana de seleccion de asiento
        if (selectedVuelo != null) {
            int vueloID = selectedVuelo.getId();
            int numAsientos = ManoloAirlines.getCapacidadAvion(ManoloAirlines.getIDAvioFromVuelo(vueloID));
            int numCols = ManoloAirlines.getNumCols(numAsientos);
            int numRows = ManoloAirlines.getNumRows(numAsientos, numCols);

            ArrayList<Integer> asientosLibres = ManoloAirlines.getAsientosLibres(ManoloAirlines.getIDAvioFromVuelo(vueloID), vueloID);
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

            // ? Quiere reservar
            dialog.setResultConverter(buttonType -> {
                if (buttonType == reservar) {
                    int selectedAsiento = selectedSeat[0];

                    // ? Ha seleccionado un asiento
                    if (selectedAsiento != -1) {
                        try {
                            int outReserva = ManoloAirlines.reservarVuelo(ManoloAirlines.getUsernameID(GlobalData.userName), vueloID, selectedAsiento);
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

                                descargarJustificante(ManoloAirlines.getReservaInfo(outReserva));
                            }
                            loadVuelos();
                            return outReserva;
                        } catch (SQLException e) {
                            Alert sqlerror = new Alert(AlertType.ERROR);
                            sqlerror.setTitle("ERROR");
                            sqlerror.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
                            sqlerror.show();
                        } catch (DocumentException | IOException e) {
                            Alert error = new Alert(AlertType.ERROR);
                            error.setTitle("Error");
                            error.setHeaderText("No se pudo guardar el PDF");
                            error.setContentText("Hubo un error al guardar el PDF. Por favor, inténtelo de nuevo.");
                            error.show();
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
     * Modificar la vista para reservar un vuelo
     *
     * @throws SQLException Error en la consutla
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

        for (String ciudad : ManoloAirlines.getlistaCiudadesStrings()) {
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

        for (String ciudad : ManoloAirlines.getlistaCiudadesStrings()) {
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
     * Modificar la vista para mis reservas
     *
     * @throws SQLException Error en la consulta
     * @see #menuReservar()
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

        for (String ciudad : ManoloAirlines.getlistaCiudadesStrings()) {
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

        for (String ciudad : ManoloAirlines.getlistaCiudadesStrings()) {
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
