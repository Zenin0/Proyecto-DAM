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

public class AdminController implements Initializable {

    private final Menus menus = new Menus();
    private final Gestioner gestioner = new Gestioner();

    private final Getter getter = new Getter();
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
    private Button deleteButton;
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
    @FXML
    private ListView<String> vuelosList;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.addCiudad.setOnAction((event) -> menuAddCiudad());
        this.addVuelo.setOnAction((event) -> {
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
        this.addAvion.setOnAction((event) -> menuAddAvion());
        this.delVuelo.setOnAction(event -> {
            try {
                menuDelVuelo();
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
                this.nombreCiudadField.setText("");
            }
        });
        this.deleteButton.setOnAction(event -> {
            try {
                delVuelo(this.vuelosList.getSelectionModel().getSelectedItem());
            } catch (SQLException e) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText(e.getMessage());
                dialog.show();
                this.nombreCiudadField.setText("");
            }
        });
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
                this.nombreCiudadField.setText("");
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

    private void listarVuelos() throws SQLException {
        this.vuelosList.getItems().clear();
        for (String vuelo : menus.listaVuelosStrings()) {
            String[] vueloParts = vuelo.replaceAll(" ", "").split("-");

            this.vuelosList.getItems().add(vueloParts[0] + " - " + getter.getNombreCiudad(Integer.parseInt(vueloParts[1])) + " - " + getter.getNombreCiudad(Integer.parseInt(vueloParts[2])) + " - " + vueloParts[4] + "/" + vueloParts[5] + "/" + vueloParts[3]);
        }
    }

    private void addCiudad() {
        if (gestioner.registrarCiudad(this.nombreCiudadField.getText(), this.nombrePaisField.getText())) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Ciudad");
            dialog.setHeaderText("Ciudad creada correctamente");
            dialog.show();
            this.nombreCiudadField.setText("");
        }

    }

    private void addAvion() {
        if (gestioner.registrarAvion(this.nombreAvionField.getText(),
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
        if (gestioner.registrarVuelo(this.menuCiudadesSalida.getText(), this.menuCiudadesDestino.getText(),
                numero, String.valueOf(localDate))) {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Vuelo creada correctamente");
            dialog.show();
            this.nombreCiudadField.setText("");
        }

    }

    public void delVuelo(String vuelo) throws SQLException {
        String[] vueloParts = vuelo.replaceAll(" ", "").split("-");
        if (gestioner.eliminarVuelo(Integer.parseInt(vueloParts[0]))) {
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

    private void menuDelVuelo() throws SQLException {
        this.menu.setText("Eliminar Vuelo");
        listarVuelos();
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

    private void menuAddVuelo() throws SQLException {
        // Cosas para hacerlo mas bonito
        this.menu.setText("Añadir Vuelo");
        this.menuCiudadesSalida.getItems().clear();
        this.menuCiudadesDestino.getItems().clear();
        this.menuAviones.getItems().clear();

        for (String ciudad : menus.listaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> menuCiudadesDestino.setText(item.getText()));
            menuCiudadesDestino.getItems().add(item);
        }
        menuCiudadesDestino.setPopupSide(Side.BOTTOM);

        for (String ciudad : menus.listaCiudadesStrings()) {
            MenuItem item = new MenuItem(ciudad);
            item.setOnAction(event -> menuCiudadesSalida.setText(item.getText()));
            menuCiudadesSalida.getItems().add(item);
        }
        menuCiudadesSalida.setPopupSide(Side.BOTTOM);

        for (String avion : menus.listaAvionesStrings()) {
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
        // Esconder delVuelo
        this.vuelosList.setVisible(false);
        this.deleteButton.setVisible(false);

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
        // Esconder delVuelo
        this.vuelosList.setVisible(false);
        this.deleteButton.setVisible(false);
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
        // Esconder delVuelo
        this.vuelosList.setVisible(false);
        this.deleteButton.setVisible(false);

    }

}


