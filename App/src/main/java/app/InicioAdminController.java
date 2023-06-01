package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controlador del FXML inicioadmin
 */
public class InicioAdminController implements Initializable {

    @FXML
    private ListView<String> avionesList;

    @FXML
    private ListView<String> vuelosList;

    @FXML
    private Button aceptarButtonAvion;

    @FXML
    private Button aceptarButtonCiudad;

    @FXML
    private Button aceptarButtonVuelo;

    @FXML
    private Button addAvionButton;

    @FXML
    private Button addCiudadButton;

    @FXML
    private Button addVueloButton;

    @FXML
    private TextField capacidadField;

    @FXML
    private Label avionVueloLabel;

    @FXML
    private Label capacidadLabel;

    @FXML
    private Label ciudadDestinoVueloLabel;

    @FXML
    private Label ciudadSalidaVueloLabel;

    @FXML
    private Button delAvionButton;

    @FXML
    private Button delVueloButton;

    @FXML
    private Button deleteAvionButton;

    @FXML
    private Button deleteVueloButton;

    @FXML
    private Button endSessionButton;

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private Label fechaLabel;

    @FXML
    private MenuButton menuAviones;

    @FXML
    private MenuButton menuCiudadesDestino;

    @FXML
    private MenuButton menuCiudadesSalida;

    @FXML
    private TextField nombreAvionField;

    @FXML
    private Label nombreAvionLabel;

    @FXML
    private TextField nombreCiudadField;

    @FXML
    private Label nombreCiudadLabel;

    @FXML
    private TextField nombrePaisField;

    @FXML
    private Label nombrePaisLabel;

    /**
     * Inicializar la ventana
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            menuAddVuelo();
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        this.addCiudadButton.setOnAction((event) -> {
            try {
                menuAddCiudad();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

        this.addVueloButton.setOnAction((event) -> {
            try {
                menuAddVuelo();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }

        });

        this.delVueloButton.setOnAction(event -> {
            try {
                menuDelVuelo();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }
        });

        this.delAvionButton.setOnAction((event) -> {
            try {
                menuDelAvion();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

        this.deleteAvionButton.setOnAction(event -> {
            try {
                delAvion();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }
        });

        this.deleteVueloButton.setOnAction(event -> {
            try {
                delVuelo();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }
        });

        this.aceptarButtonCiudad.setOnAction((event) -> addCiudad());
        this.aceptarButtonAvion.setOnAction((event) -> addAvion());
        this.addAvionButton.setOnAction((event) -> menuAddAvion());

        this.aceptarButtonVuelo.setOnAction(event -> {
            try {
                addVuelo();
            } catch (NumberFormatException | ParseException | SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

        this.endSessionButton.setOnAction((event) -> {
            try {
                endSession();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });

    }


    /**
     * Funcion para listar los vuelos y meterlos en la lista
     *
     * @see Getter
     */
    private void listarVuelos() throws SQLException {
        this.vuelosList.getItems().clear();
        for (String vuelo : Getter.getlistaVuelosStrings()) {
            String[] vueloParts = vuelo.replaceAll(" ", "").split("-");

            this.vuelosList.getItems().add(vueloParts[0] + " - " + Getter.getNombreCiudad(Integer.parseInt(vueloParts[1])) + " - " + Getter.getNombreCiudad(Integer.parseInt(vueloParts[2])) + " - " + vueloParts[4] + "/" + vueloParts[5] + "/" + vueloParts[3]);
        }
    }

    /**
     * Funcion para listar los vuelos y meterlos en la lista
     *
     * @see Getter
     */
    private void listarAviones() throws SQLException {
        this.avionesList.getItems().clear();
        for (String avion : Getter.getlistaAvionesStrings()) {
            String[] avionParts = avion.replaceAll(" ", "").split("-");
            this.avionesList.getItems().add(avionParts[0] + " - " + avionParts[1] + " - " +avionParts[2]);
        }
    }

    /**
     * Funcion para añadir una Ciudad
     *
     * @see Gestioner#registrarCiudad(String, String)
     */
    private void addCiudad() {
        if (Gestioner.registrarCiudad(this.nombreCiudadField.getText(), this.nombrePaisField.getText())) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Ciudad");
            dialog.setHeaderText("Ciudad creada correctamente");
            dialog.show();
            this.nombreCiudadField.setText("");
        }

    }

    /**
     * Funcion para añadir un Avion
     *
     * @see Gestioner#registrarAvion(String, int)
     */
    private void addAvion() {
        if (Integer.parseInt(this.capacidadField.getText()) > 200) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("Capacidad");
            dialog.setHeaderText("Lo siento, nuestras aerolienas no pueden disponer de aviones de mas de 200 pasajeros,");
            dialog.show();
        } else {
            if (Gestioner.registrarAvion(this.nombreAvionField.getText(), Integer.parseInt(this.capacidadField.getText()))) {
                Alert dialog = new Alert(AlertType.CONFIRMATION);
                dialog.setTitle("Avión");
                dialog.setHeaderText("Avión creada correctamente");
                dialog.show();

            }
        }

    }


    /**
     * Funcion para añadir un vuelo
     *
     * @see Gestioner#registrarVuelo(String, String, int, String)
     */
    private void addVuelo() throws ParseException, SQLException {
        String[] tokens = this.menuAviones.getText().split("\\s*-\\s*");
        int numero = Integer.parseInt(tokens[0]);
        LocalDate localDate = fechaDatePicker.getValue();
        if (Gestioner.registrarVuelo(this.menuCiudadesSalida.getText(), this.menuCiudadesDestino.getText(), numero, String.valueOf(localDate))) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Vuelo creada correctamente");
            dialog.show();
        }


    }

    /**
     * Funcion para eliminar un Vuelo
     *
     * @see Gestioner#eliminarVuelo(int)
     */
    public void delVuelo() throws SQLException {
        String[] vueloParts = this.vuelosList.getSelectionModel().getSelectedItem().replaceAll(" ", "").split("-");
        if (Gestioner.eliminarVuelo(Integer.parseInt(vueloParts[0]))) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Vuelo eliminado correctamente");
            dialog.show();
        } else {
            Alert dialog = new Alert(AlertType.WARNING);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Algo ha fallado");
            dialog.show();
        }
        listarVuelos();
    }

    /**
     * Funcion para eliminar un avión
     *
     * @see Gestioner#eliminarAvion(int)
     */
    public void delAvion() throws SQLException {
        String[] vueloParts = this.avionesList.getSelectionModel().getSelectedItem().replaceAll(" ", "").split("-");
        if (Gestioner.eliminarAvion(Integer.parseInt(vueloParts[0]))) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Vuelo eliminado correctamente");
            dialog.show();
        } else {
            Alert dialog = new Alert(AlertType.WARNING);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Algo ha fallado");
            dialog.show();
        }
        listarAviones();
    }

    /**
     * Terminar la sesión del usuario
     */
    private void endSession() throws IOException {
        GlobalData.userName = "";
        App.setRoot("login");
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Session Terminada");
        dialog.setHeaderText("Sesión terminada con éxito");
        dialog.show();
    }

    /**
     * Cambiar al modo a Borrar un vuelo
     */
    private void menuDelVuelo() throws SQLException {
        listarVuelos();
        this.avionesList.setVisible(false);
        this.delAvionButton.setDisable(false);
        this.delVueloButton.setDisable(true);
        this.addVueloButton.setDisable(false);
        this.addCiudadButton.setDisable(false);
        this.addAvionButton.setDisable(false);
        this.vuelosList.setVisible(true);
        this.deleteVueloButton.setVisible(true);
        this.menuCiudadesDestino.setVisible(false);
        this.menuCiudadesSalida.setVisible(false);
        this.ciudadSalidaVueloLabel.setVisible(false);
        this.ciudadDestinoVueloLabel.setVisible(false);
        this.menuAviones.setVisible(false);
        this.avionVueloLabel.setVisible(false);
        this.aceptarButtonVuelo.setVisible(false);
        this.fechaLabel.setVisible(false);
        this.fechaDatePicker.setVisible(false);
        this.nombreAvionLabel.setVisible(false);
        this.nombreAvionField.setVisible(false);
        this.aceptarButtonAvion.setVisible(false);
        this.capacidadLabel.setVisible(false);
        this.capacidadField.setVisible(false);
        this.nombreCiudadLabel.setVisible(false);
        this.nombreCiudadField.setVisible(false);
        this.nombrePaisLabel.setVisible(false);
        this.nombrePaisField.setVisible(false);
        this.aceptarButtonCiudad.setVisible(false);
        this.deleteAvionButton.setVisible(false);

    }

    /**
     * Cambiar al modo a Borrar un vuelo
     */
    private void menuDelAvion() throws SQLException {
        listarAviones();
        this.avionesList.setVisible(true);
        this.delAvionButton.setDisable(true);
        this.delVueloButton.setDisable(false);
        this.addVueloButton.setDisable(false);
        this.addCiudadButton.setDisable(false);
        this.addAvionButton.setDisable(false);
        this.vuelosList.setVisible(true);
        this.deleteVueloButton.setVisible(true);
        this.menuCiudadesDestino.setVisible(false);
        this.menuCiudadesSalida.setVisible(false);
        this.ciudadSalidaVueloLabel.setVisible(false);
        this.ciudadDestinoVueloLabel.setVisible(false);
        this.menuAviones.setVisible(false);
        this.avionVueloLabel.setVisible(false);
        this.aceptarButtonVuelo.setVisible(false);
        this.fechaLabel.setVisible(false);
        this.fechaDatePicker.setVisible(false);
        this.nombreAvionLabel.setVisible(false);
        this.nombreAvionField.setVisible(false);
        this.aceptarButtonAvion.setVisible(false);
        this.capacidadLabel.setVisible(false);
        this.capacidadField.setVisible(false);
        this.nombreCiudadLabel.setVisible(false);
        this.nombreCiudadField.setVisible(false);
        this.nombrePaisLabel.setVisible(false);
        this.nombrePaisField.setVisible(false);
        this.aceptarButtonCiudad.setVisible(false);
        this.deleteAvionButton.setVisible(true);

    }

    /**
     * Cambiar al modo a añadir un Vuelo
     *
     * @see Getter
     */
    private void menuAddVuelo() throws SQLException {
        this.avionesList.setVisible(false);
        this.delAvionButton.setDisable(false);
        this.delVueloButton.setDisable(false);
        this.delVueloButton.setDisable(false);
        this.addVueloButton.setDisable(true);
        this.addCiudadButton.setDisable(false);
        this.addAvionButton.setDisable(false);
        this.menuCiudadesSalida.getItems().clear();
        this.menuCiudadesDestino.getItems().clear();
        this.menuAviones.getItems().clear();
        for (String ciudad : Getter.getlistaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> menuCiudadesDestino.setText(item.getText()));
            menuCiudadesDestino.getItems().add(item);
        }
        menuCiudadesDestino.setPopupSide(Side.BOTTOM);
        for (String ciudad : Getter.getlistaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> menuCiudadesSalida.setText(item.getText()));
            menuCiudadesSalida.getItems().add(item);
        }
        menuCiudadesSalida.setPopupSide(Side.BOTTOM);
        for (String avion : Getter.getlistaAvionesStrings()) {
            MenuItem item = new MenuItem(avion);
            item.setOnAction(event -> menuAviones.setText(item.getText()));
            menuAviones.getItems().add(item);
        }
        menuAviones.setPopupSide(Side.BOTTOM);
        this.menuCiudadesDestino.setVisible(true);
        this.menuCiudadesSalida.setVisible(true);
        this.ciudadSalidaVueloLabel.setVisible(true);
        this.ciudadDestinoVueloLabel.setVisible(true);
        this.menuAviones.setVisible(true);
        this.avionVueloLabel.setVisible(true);
        this.aceptarButtonVuelo.setVisible(true);
        this.fechaLabel.setVisible(true);
        this.fechaDatePicker.setVisible(true);
        this.nombreAvionLabel.setVisible(false);
        this.nombreAvionField.setVisible(false);
        this.aceptarButtonAvion.setVisible(false);
        this.capacidadLabel.setVisible(false);
        this.capacidadField.setVisible(false);
        this.nombreCiudadLabel.setVisible(false);
        this.nombreCiudadField.setVisible(false);
        this.nombrePaisLabel.setVisible(false);
        this.nombrePaisField.setVisible(false);
        this.aceptarButtonCiudad.setVisible(false);
        this.vuelosList.setVisible(false);
        this.deleteVueloButton.setVisible(false);
        this.deleteAvionButton.setVisible(false);

    }

    /**
     * Cambiar al modo a añadir Ciudad
     */
    private void menuAddCiudad() throws IOException {
        this.avionesList.setVisible(false);
        this.delAvionButton.setDisable(false);
        this.delVueloButton.setDisable(false);
        this.addVueloButton.setDisable(false);
        this.addCiudadButton.setDisable(true);
        this.addAvionButton.setDisable(false);
        this.nombreCiudadLabel.setVisible(true);
        this.nombreCiudadField.setVisible(true);
        this.nombrePaisLabel.setVisible(true);
        this.nombrePaisField.setVisible(true);
        this.aceptarButtonCiudad.setVisible(true);
        this.nombreAvionLabel.setVisible(false);
        this.nombreAvionField.setVisible(false);
        this.aceptarButtonAvion.setVisible(false);
        this.capacidadLabel.setVisible(false);
        this.capacidadField.setVisible(false);
        this.menuCiudadesDestino.setVisible(false);
        this.menuCiudadesSalida.setVisible(false);
        this.ciudadSalidaVueloLabel.setVisible(false);
        this.ciudadDestinoVueloLabel.setVisible(false);
        this.menuAviones.setVisible(false);
        this.avionVueloLabel.setVisible(false);
        this.aceptarButtonVuelo.setVisible(false);
        this.fechaLabel.setVisible(false);
        this.fechaDatePicker.setVisible(false);
        this.vuelosList.setVisible(false);
        this.deleteVueloButton.setVisible(false);
        this.deleteAvionButton.setVisible(false);
    }

    /**
     * Cambiar al modo Añadir un avion
     */
    private void menuAddAvion() {
        this.avionesList.setVisible(false);
        this.delVueloButton.setDisable(false);
        this.delAvionButton.setDisable(false);
        this.addVueloButton.setDisable(false);
        this.addCiudadButton.setDisable(false);
        this.addAvionButton.setDisable(true);
        this.nombreAvionLabel.setVisible(true);
        this.nombreAvionField.setVisible(true);
        this.aceptarButtonAvion.setVisible(true);
        this.capacidadLabel.setVisible(true);
        this.capacidadField.setVisible(true);
        this.nombreCiudadLabel.setVisible(false);
        this.nombreCiudadField.setVisible(false);
        this.nombrePaisLabel.setVisible(false);
        this.nombrePaisField.setVisible(false);
        this.aceptarButtonCiudad.setVisible(false);
        this.menuCiudadesDestino.setVisible(false);
        this.menuCiudadesSalida.setVisible(false);
        this.ciudadSalidaVueloLabel.setVisible(false);
        this.ciudadDestinoVueloLabel.setVisible(false);
        this.menuAviones.setVisible(false);
        this.avionVueloLabel.setVisible(false);
        this.aceptarButtonVuelo.setVisible(false);
        this.fechaLabel.setVisible(false);
        this.fechaDatePicker.setVisible(false);
        this.vuelosList.setVisible(false);
        this.deleteVueloButton.setVisible(false);
        this.deleteAvionButton.setVisible(false);

    }

}


