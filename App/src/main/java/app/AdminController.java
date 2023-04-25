package app;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;

public class AdminController implements Initializable {

    @FXML
    private Button aceptarButtonCiudad;

    @FXML
    private MenuItem addAvion;

    @FXML
    private MenuItem addCiudad;

    @FXML
    private MenuItem addVuelo;

    @FXML
    private SplitMenuButton menu;

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
        addCiudad.setOnAction((event) -> ciudad());
        addVuelo.setOnAction((event) -> vuelo());
        addAvion.setOnAction((event) -> avion());
        aceptarButtonCiudad.setOnAction((event) -> addCiudad());
    }

    private void vuelo() {
        // Cosas para hacerlo mas bonito
        this.menu.setText("Añadir Vuelo");

    }

    private void ciudad() {
        this.menu.setText("Añadir Ciudad");
        this.nombreCiudad.setVisible(true);
        this.nombreCiudadField.setVisible(true);
        this.nombrePais.setVisible(true);
        this.nombrePaisField.setVisible(true);
        this.aceptarButtonCiudad.setVisible(true);
    }

    private void avion() {
        // Cosas para hacerlo mas bonito
        this.menu.setText("Añadir Avión");
    }

    private void addCiudad() {
        if (new AddCiudad(this.nombreCiudadField.getText(), this.nombrePaisField.getText()).registrar()){
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.setTitle("Ciudad");
            dialog.setHeaderText("Ciudad creada correctamente");
            dialog.show();
            this.nombreCiudadField.setText("");
        }

    }

}
