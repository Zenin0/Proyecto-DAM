package app;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class AdminController implements Initializable {

    @FXML
    private Button aceptarButtonDestino;

    @FXML
    private Button aceptarButtonVuelo;

    @FXML
    private MenuItem addAvion;

    @FXML
    private MenuItem addCiudad;

    @FXML
    private MenuItem addVuelo;

    @FXML
    private Label ciudad_llegada;

    @FXML
    private TextField ciudad_llegada_field;

    @FXML
    private Label ciudad_salida;

    @FXML
    private TextField ciudad_salida_field;

    @FXML
    private Label destino;

    @FXML
    private TextField destino_field;

    @FXML
    private Label fecha_salida;

    @FXML
    private TextField fecha_salida_field;

    @FXML
    private SplitMenuButton menu;

    @FXML
    private Label pasajeros;

    @FXML
    private TextField pasajeros_label;

    @FXML
    private Label salida;

    @FXML
    private TextField salida_field;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        addCiudad.setOnAction((event) -> destino());
        addVuelo.setOnAction((event) -> vuelo());
        addAvion.setOnAction((event) -> avion());
    }

    private void vuelo() {
        // Cosas para hacerlo mas bonito
        this.menu.setText("Añadir Vuelo");
        this.aceptarButtonDestino.setVisible(true);
        this.destino.setVisible(true);
        this.destino_field.setVisible(true);
        this.salida.setVisible(true);
        this.salida_field.setVisible(true);
        this.fecha_salida.setVisible(true);
        this.fecha_salida_field.setVisible(true);
        this.pasajeros.setVisible(true);
        this.pasajeros_label.setVisible(true);
        this.ciudad_llegada.setVisible(false);
        this.ciudad_salida.setVisible(false);
        this.ciudad_llegada_field.setVisible(false);
        this.ciudad_salida_field.setVisible(false);
        this.aceptarButtonVuelo.setVisible(false);
    }

    private void destino() {
        // Cosas para hacerlo mas bonito
        this.menu.setText("Añadir Ciudad");
        this.aceptarButtonVuelo.setVisible(true);
        this.ciudad_llegada.setVisible(true);
        this.ciudad_salida.setVisible(true);
        this.ciudad_llegada_field.setVisible(true);
        this.ciudad_salida_field.setVisible(true);
        this.destino.setVisible(false);
        this.destino_field.setVisible(false);
        this.salida.setVisible(false);
        this.salida_field.setVisible(false);
        this.fecha_salida.setVisible(false);
        this.fecha_salida_field.setVisible(false);
        this.pasajeros.setVisible(false);
        this.pasajeros_label.setVisible(false);
        this.aceptarButtonDestino.setVisible(false);
    }

    private void avion() {
        // Cosas para hacerlo mas bonito
        this.menu.setText("Añadir Avión");
    }

}
