package app;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;

public class AdminController implements Initializable {

    @FXML
    private Button aceptarButtonAvion;

    @FXML
    private Button aceptarButtonCiudad;

    @FXML
    private Button aceptarButtonVuelo;

    @FXML
    private MenuItem addAvion;

    @FXML
    private MenuItem addCiudad;

    @FXML
    private MenuItem addVuelo;

    @FXML
    private Label anyoFabricacion;

    @FXML
    private TextField anyoFabricacionField;

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
    private MenuItem delVuelo;

    @FXML
    private Button endSession;

    @FXML
    private Label fechaLabel;

    @FXML
    private DatePicker fechaSelect;

    @FXML
    private SplitMenuButton menu;

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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        addCiudad.setOnAction((event) -> menuAddCiudad());
        addVuelo.setOnAction((event) -> {
            try {
                menuAddVuelo();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
                this.nombreCiudadField.setText("");
            }
        });
        addAvion.setOnAction((event) -> menuAddAvion());
        delVuelo.setOnAction(event -> menuDelVuelo());
        aceptarButtonCiudad.setOnAction((event) -> addCiudad());
        aceptarButtonAvion.setOnAction((event) -> addAvion());
        aceptarButtonVuelo.setOnAction(event -> {
            try {
                addVuelo();
            } catch (NumberFormatException | ParseException | SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
                this.nombreCiudadField.setText("");
            }
        });
        endSession.setOnAction((event) -> {
            try {
                endSession();
            } catch (IOException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
                this.nombreCiudadField.setText("");
            }
        });
    }

    private void endSession() throws IOException {
        App.setRoot("login");
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Session Terminada");
        dialog.setHeaderText("Sesión terminada con éxito");
        dialog.show();
        this.nombreCiudadField.setText("");
    }

    private void menuDelVuelo() {
        this.menu.setText("Eliminar Vuelo");
    }

    private void menuAddVuelo() throws SQLException {
        // Cosas para hacerlo mas bonito
        this.menu.setText("Añadir Vuelo");
        this.menuCiudadesSalida.getItems().clear();
        this.menuCiudadesDestino.getItems().clear();
        this.menuAviones.getItems().clear();

        ArrayList<String> cities = new MenuCiudades().menuItemsStrings();
        for (String city : cities) {
            MenuItem item = new MenuItem(city);
            item.setOnAction(event -> menuCiudadesDestino.setText(item.getText()));
            menuCiudadesDestino.getItems().add(item);
        }
        menuCiudadesDestino.setPopupSide(Side.BOTTOM);

        cities = new MenuCiudades().menuItemsStrings();
        for (String city : cities) {
            MenuItem item = new MenuItem(city);
            item.setOnAction(event -> menuCiudadesSalida.setText(item.getText()));
            menuCiudadesSalida.getItems().add(item);
        }
        menuCiudadesSalida.setPopupSide(Side.BOTTOM);

        ArrayList<String> airplanes = new MenuAviones().menuItemsStrings();
        for (String airplane : airplanes) {
            MenuItem item = new MenuItem(airplane);
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
        this.anyoFabricacion.setVisible(false);
        this.anyoFabricacionField.setVisible(false);
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

    private void menuAddCiudad() {
        this.menu.setText("Añadir Ciudad");
        // Mostrar ciudad
        this.nombreCiudad.setVisible(true);
        this.nombreCiudadField.setVisible(true);
        this.nombrePais.setVisible(true);
        this.nombrePaisField.setVisible(true);
        this.aceptarButtonCiudad.setVisible(true);
        // Esconder avión
        this.nombreAvion.setVisible(false);
        this.nombreAvionField.setVisible(false);
        this.anyoFabricacion.setVisible(false);
        this.anyoFabricacionField.setVisible(false);
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
    }

    private void menuAddAvion() {
        // Cosas para hacerlo mas bonito
        this.menu.setText("Añadir Avión");
        // Mostrar avión
        this.nombreAvion.setVisible(true);
        this.nombreAvionField.setVisible(true);
        this.anyoFabricacion.setVisible(true);
        this.anyoFabricacionField.setVisible(true);
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

    }

    private void addCiudad() {
        if (new AddCiudad().registrar(this.nombreCiudadField.getText(), this.nombrePaisField.getText())) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Ciudad");
            dialog.setHeaderText("Ciudad creada correctamente");
            dialog.show();
            this.nombreCiudadField.setText("");
        }

    }

    private void addAvion() {
        if (new AddAvion().registrar(this.nombreAvionField.getText(),
                Integer.parseInt(this.anyoFabricacionField.getText()),
                Integer.parseInt(this.capacidadField.getText()))) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Avión");
            dialog.setHeaderText("Avión creada correctamente");
            dialog.show();
            this.nombreCiudadField.setText("");
        }

    }

    private void addVuelo() throws ParseException, SQLException {
        String[] tokens = this.menuAviones.getText().split("\\s*-\\s*");
        int numero = Integer.parseInt(tokens[0]);
        LocalDate localDate = fechaSelect.getValue();
        if (new AddVuelo().registrar(this.menuCiudadesSalida.getText(), this.menuCiudadesDestino.getText(),
                numero, String.valueOf(localDate))) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Vuelo creada correctamente");
            dialog.show();
            this.nombreCiudadField.setText("");
        }

    }

}
