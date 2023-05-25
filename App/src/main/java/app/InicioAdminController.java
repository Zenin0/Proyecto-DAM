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
    private Button aceptarButtonAvion;

    @FXML
    private Button aceptarButtonCiudad;

    @FXML
    private Button aceptarButtonVuelo;

    @FXML
    private Button addAvion;

    @FXML
    private Button addCiudad;

    @FXML
    private Button addVuelo;

    @FXML
    private Label avionVuelo;

    @FXML
    private Label capacidad;

    @FXML
    private TextField capacidadField;

    @FXML
    private Label ciudadDestinoVuelo;

    @FXML
    private Label ciudadSalidaVuelo;

    @FXML
    private Button delVuelo;

    @FXML
    private Button deleteButton;

    @FXML
    private Button endSession;

    @FXML
    private Label fechaLabel;

    @FXML
    private DatePicker fechaSelect;

    @FXML
    private SplitMenuButton menuAviones;

    @FXML
    private SplitMenuButton menuCiudadesDestino;

    @FXML
    private SplitMenuButton menuCiudadesSalida;

    @FXML
    private Label nombreAvion;

    @FXML
    private TextField nombreAvionField;

    @FXML
    private Label nombreCiudad;

    @FXML
    private TextField nombreCiudadField;

    @FXML
    private Label nombrePais;

    @FXML
    private TextField nombrePaisField;

    @FXML
    private ListView<String> vuelosList;


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
        // Cambiar a la funcion de Añadir una Ciudad
        this.addCiudad.setOnAction((event) -> {
            try {
                menuAddCiudad();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
            }
        });
        // Cambiar  la funcion de Añadir un Vuelo
        this.addVuelo.setOnAction((event) -> {
            try {
                menuAddVuelo();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }

        });
        this.addAvion.setOnAction((event) -> menuAddAvion());
        // Cambiar a la funcion de eliminar un Vuelo
        this.delVuelo.setOnAction(event -> {
            try {
                menuDelVuelo();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }
        });
        // Boton para ejecutar el borrado de un vuelo seleccionado
        this.deleteButton.setOnAction(event -> {
            try {
                delVuelo();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setContentText("Error en la bd: " + e.getErrorCode() + "-" + e.getMessage());
                dialog.show();
            }
        });
        // Botones para ejecutar el añadido de sus correspondientes
        this.aceptarButtonCiudad.setOnAction((event) -> addCiudad());
        this.aceptarButtonAvion.setOnAction((event) -> addAvion());
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
        // Boton para terminar la sesion
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
    }


    /**
     * Terminar la sesión del usuario
     */
    private void endSession() throws IOException {
        App.setRoot("login");
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Session Terminada");
        dialog.setHeaderText("Sesión terminada con éxito");
        dialog.show();
    }


    /**
     * Funcion para listar los vuelos y meterlos en la lista
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
     * Funcion para añadir una Ciudad
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
     * @see Gestioner#registrarVuelo(String, String, int, String) 
     */
    private void addVuelo() throws ParseException, SQLException {
        String[] tokens = this.menuAviones.getText().split("\\s*-\\s*");
        int numero = Integer.parseInt(tokens[0]);
        LocalDate localDate = fechaSelect.getValue();
        if (Gestioner.registrarVuelo(this.menuCiudadesSalida.getText(), this.menuCiudadesDestino.getText(), numero, String.valueOf(localDate))) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Vuelo creada correctamente");
            dialog.show();
        }

    }

    /**
     * Funcion para eliminar un Vuelo
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
     * Cambiar al modo Borrar un vuelo
     */
    private void menuDelVuelo() throws SQLException {
        listarVuelos();
        this.delVuelo.setDisable(true);
        this.addVuelo.setDisable(false);
        this.addCiudad.setDisable(false);
        this.addAvion.setDisable(false);
        // Mostrar delVuelos
        this.vuelosList.setVisible(true);
        this.deleteButton.setVisible(true);
        // Esconder Vuelo
        this.menuCiudadesDestino.setVisible(false);
        this.menuCiudadesSalida.setVisible(false);
        this.ciudadSalidaVuelo.setVisible(false);
        this.ciudadDestinoVuelo.setVisible(false);
        this.menuAviones.setVisible(false);
        this.avionVuelo.setVisible(false);
        this.aceptarButtonVuelo.setVisible(false);
        this.fechaLabel.setVisible(false);
        this.fechaSelect.setVisible(false);
        // Esconder avión
        this.nombreAvion.setVisible(false);
        this.nombreAvionField.setVisible(false);
        this.aceptarButtonAvion.setVisible(false);
        this.capacidad.setVisible(false);
        this.capacidadField.setVisible(false);
        // Esconder Ciudad
        this.nombreCiudad.setVisible(false);
        this.nombreCiudadField.setVisible(false);
        this.nombrePais.setVisible(false);
        this.nombrePaisField.setVisible(false);
        this.aceptarButtonCiudad.setVisible(false);

    }

    /**
     * Cambiar al modo Añadir un Vuelo
     * @see Getter
     */
    private void menuAddVuelo() throws SQLException {
        this.delVuelo.setDisable(false);
        this.addVuelo.setDisable(true);
        this.addCiudad.setDisable(false);
        this.addAvion.setDisable(false);

        // Cosas para hacerlo mas bonito
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

        // Mostrar Vuelo
        this.menuCiudadesDestino.setVisible(true);
        this.menuCiudadesSalida.setVisible(true);
        this.ciudadSalidaVuelo.setVisible(true);
        this.ciudadDestinoVuelo.setVisible(true);
        this.menuAviones.setVisible(true);
        this.avionVuelo.setVisible(true);
        this.aceptarButtonVuelo.setVisible(true);
        this.fechaLabel.setVisible(true);
        this.fechaSelect.setVisible(true);
        // Esconder avión
        this.nombreAvion.setVisible(false);
        this.nombreAvionField.setVisible(false);
        this.aceptarButtonAvion.setVisible(false);
        this.capacidad.setVisible(false);
        this.capacidadField.setVisible(false);
        // Esconder Ciudad
        this.nombreCiudad.setVisible(false);
        this.nombreCiudadField.setVisible(false);
        this.nombrePais.setVisible(false);
        this.nombrePaisField.setVisible(false);
        this.aceptarButtonCiudad.setVisible(false);
        // Esconder delVuelo
        this.vuelosList.setVisible(false);
        this.deleteButton.setVisible(false);

    }

    /**
     * Cambiar al modo Añadir Ciudad
     */
    private void menuAddCiudad() throws IOException {
        this.delVuelo.setDisable(false);
        this.addVuelo.setDisable(false);
        this.addCiudad.setDisable(true);
        this.addAvion.setDisable(false);
        // Mostrar ciudad
        this.nombreCiudad.setVisible(true);
        this.nombreCiudadField.setVisible(true);
        this.nombrePais.setVisible(true);
        this.nombrePaisField.setVisible(true);
        this.aceptarButtonCiudad.setVisible(true);
        // Esconder avión
        this.nombreAvion.setVisible(false);
        this.nombreAvionField.setVisible(false);
        this.aceptarButtonAvion.setVisible(false);
        this.capacidad.setVisible(false);
        this.capacidadField.setVisible(false);
        // Esconder Vuelo
        this.menuCiudadesDestino.setVisible(false);
        this.menuCiudadesSalida.setVisible(false);
        this.ciudadSalidaVuelo.setVisible(false);
        this.ciudadDestinoVuelo.setVisible(false);
        this.menuAviones.setVisible(false);
        this.avionVuelo.setVisible(false);
        this.aceptarButtonVuelo.setVisible(false);
        this.fechaLabel.setVisible(false);
        this.fechaSelect.setVisible(false);
        // Esconder delVuelo
        this.vuelosList.setVisible(false);
        this.deleteButton.setVisible(false);
    }

    /**
     * Cambiar al modo Añadir un avion
     */
    private void menuAddAvion() {
        this.delVuelo.setDisable(false);
        this.addVuelo.setDisable(false);
        this.addCiudad.setDisable(false);
        this.addAvion.setDisable(true);
        // Cosas para hacerlo mas bonito
        // Mostrar avión
        this.nombreAvion.setVisible(true);
        this.nombreAvionField.setVisible(true);
        this.aceptarButtonAvion.setVisible(true);
        this.capacidad.setVisible(true);
        this.capacidadField.setVisible(true);
        // Esconder Ciudad
        this.nombreCiudad.setVisible(false);
        this.nombreCiudadField.setVisible(false);
        this.nombrePais.setVisible(false);
        this.nombrePaisField.setVisible(false);
        this.aceptarButtonCiudad.setVisible(false);
        // Esconder Vuelo
        this.menuCiudadesDestino.setVisible(false);
        this.menuCiudadesSalida.setVisible(false);
        this.ciudadSalidaVuelo.setVisible(false);
        this.ciudadDestinoVuelo.setVisible(false);
        this.menuAviones.setVisible(false);
        this.avionVuelo.setVisible(false);
        this.aceptarButtonVuelo.setVisible(false);
        this.fechaLabel.setVisible(false);
        this.fechaSelect.setVisible(false);
        // Esconder delVuelo
        this.vuelosList.setVisible(false);
        this.deleteButton.setVisible(false);

    }

}


