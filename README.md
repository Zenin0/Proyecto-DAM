<a name="readme-top"></a>
<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/Zenin0/Proyecto-DAM">
    <img src="images/logo.png" alt="Logo" width="500" height="500">
  </a>

<h3 align="center">Manolo Airlines</h3>

  <p align="center">
    Monolo Airlines, una compañía dedicada a satisfacer el cliente
    <br />
    <a href="https://github.com/Zenin0/Proyecto-DAM/tree/main/App"><strong>Explora el Código »</strong></a>
    <br />
    <br />
    <a href="https://github.com/Zenin0/Proyecto-DAM/issues">Reportar un Bug</a>
    ·
    <a href="https://zenin0.github.io/Manolo-Airlines-JavaDoc.github.io/app/module-summary.html">Java Doc</a>
    ·
    <a href="https://github.com/Zenin0/Proyecto-DAM/issues">Pedir una función</a>
  </p>
</div>



<!-- Tabla de Contenidos -->
<details>
  <summary>Tabla de Contenidos</summary>
  <ol>
    <li>
      <a href="#acerca-del-proyecto">Acerca del Proyecto</a>
      <ul>
        <li><a href="#construido-con">Construido con</a></li>
      </ul>
    </li>
    <li><a href="#uso">Uso</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#licencia">Licencia</a></li>
    <li><a href="#contacto">Contacto</a></li>
  </ol>
</details>



<!-- Acerca del Proyecto -->

## Acerca del Proyecto

Este proyecto se ha creado para `Manolo Airlines` para facilitar el uso de su base de datos mysql, que usan para
gestionar su flota de Aeronaves, ademas de ser utilizado como proyecto del Grado Superior DAM del primer año, este se
basa en 2 apartados, usuarios administradores, que gestionarán la creación de nuevos vuelos, destinos y demás, ademas de
usuarios no administradores que se usarán para la reserva/cancelación de vuelos, y reservas

### Construido con

Este código estará creado con `Java` para el desarrollo principal de la aplicación, `MYSQL` para la gestión de la base
de datos, y `JavaFX` para el apartado de GUI.

* [![Java](https://img.shields.io/badge/java-ED8B00?style=for-the-badge)](https://www.java.com)
* [![JavaFX](https://img.shields.io/badge/javafx-ED8B00?style=for-the-badge)](https://openjfx.io/)
* [![MySql](https://img.shields.io/badge/MYsql-3670A0?style=for-the-badge)](https://www.mysql.com/)

Ademas de `JavaDoc` con explicaciones del codigo y sus funciones

<!-- Ejemplos de uso -->

## Uso

### Ventana de Registro

Apartado de la App donde se gestionará el `registro de nuevos usuarios`, tanto `usuarios normales`
como `usuarios administradores` (Marcando la casilla administrador) haciendo que pida la contraseña de
administradores (root).

  <p align="center">
    <img src="./images/Register-Screen.png" alt="Login Screen">
  </p>

#### Parte del controlador donde ejecutamos el registro

``` java
    /**
     * Funcion para registrar un usuario
     *
     * @see Gestioner#registrar(String, String, String, String, String, boolean)
     */
    private void registrar() {
        // Todo los campos llenos?
        if (!this.usuReg.getText().isEmpty() || !this.usuNombre.getText().isEmpty() || !this.usuApellidos.getText().isEmpty() || !this.passReg1.getText().isEmpty() || !this.passReg2.getText().isEmpty()) {
            // Se ha registrado?
            if (Gestioner.registrar(this.usuReg.getText(), this.usuNombre.getText(), this.usuApellidos.getText(), this.passReg1.getText(), this.passReg2.getText(), this.adminCheckBox.isSelected())) {
                Vaciar los inputs
                this.usuReg.setText("");
                this.usuNombre.setText("");
                this.usuApellidos.setText("");
                this.passReg1.setText("");
                this.passReg2.setText("");
                Alert dialog = new Alert(AlertType.CONFIRMATION);
                dialog.setTitle("Usuario");
                dialog.setHeaderText("Usuario creado correctamente");
                dialog.show();
            } else {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Este Usuario y esta contraseña ya existen");
                dialog.show();
            }
        } else {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setHeaderText("Rellene todos los campos");
            dialog.show();
        }
    }
```


### Ventana de Login

Apartado de la App donde se hará y gestionará el `inicio de sesión`, en el que en función de el `tipo de usuario` que
hace
login se redirigirá al `apartado de los administradores` o al `apartado de usuarios normales`.

  <p align="center">
    <img src="./images/Login-Screen.png" alt="Login Screen">
  </p>

#### Parte del controlador donde ejecutamos el login

``` java
    /**
     * Login de los usuarios
     *
     * @see Gestioner#login(String, String)
     */
    private void login() throws IOException, SQLException {
        // Estan todos los campos llenos?
        if (this.usuLog.getText().isEmpty() || this.passLog.getText().isEmpty()) {
            Alert error = new Alert(AlertType.ERROR);
            error.setTitle("Login");
            error.setHeaderText("Rellene todos los campost");
            error.show();
        } else {
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            int res = Gestioner.login(this.usuLog.getText(), this.passLog.getText());
            // Es Administrador, o Usuario normal?
            if (res == 1) {
                dialog.setTitle("¡Login correcto!");
                dialog.setHeaderText("¡Bienvenido " + Getter.getNombreAndApellidos(Getter.getUsernameID(this.usuLog.getText())) + "!");
                dialog.show();
                App.setRoot("inicio_admin");
            } else if (res == 0) {
                dialog.setTitle("¡Login correcto!");
                dialog.setHeaderText("¡Bienvenido " + Getter.getNombreAndApellidos(Getter.getUsernameID(this.usuLog.getText())) + "!");
                dialog.show();
                App.setRoot("inicio_user");
            } else {
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle("Login Incorrecto");
                error.setHeaderText("Inicio de sesión incorrecto");
                error.show();
            }
        }
    }
```

### Ventana de añadir un vuelo

Apartado donde se gestionará el `añadido de nuevos vuelos`.

  <p align="center">
    <img src="./images/Add-Vuelo.png" alt="Login Screen">
  </p>

#### Parte del controlador donde ejecutamos el añadido del vuelo

``` java
    /**
     * Funcion para añadir un vuelo
     *
     * @see Gestioner#registrarVuelo(String, String, int, String)
     */
    private void addVuelo() throws ParseException, SQLException {
        // Estan los campos llenos?
        if (this.menuCiudadesSalida.getText().equals("Ciudades Salida") || this.menuCiudadesDestino.getText().equals("Ciudades Destino") || this.menuAviones.getText().equals("Aviones") || this.fechaDatePicker.getValue() == null) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Rellene todos los campos");
            dialog.show();
        } else {
            String[] tokens = this.menuAviones.getText().split("\\s*-\\s*");
            int IDAvion = Integer.parseInt(tokens[0]);
            LocalDate localDate = fechaDatePicker.getValue();
            // Esta el avión disponible para el vuelo?
            if (Getter.getDispnibilidad(String.valueOf(localDate), IDAvion, Getter.getIDCiudad(this.menuCiudadesSalida.getText()))) {
                // Se ha registrado el vuelo?
                if (Gestioner.registrarVuelo(this.menuCiudadesSalida.getText(), this.menuCiudadesDestino.getText(), IDAvion, String.valueOf(localDate))) {
                    Alert dialog = new Alert(AlertType.CONFIRMATION);
                    dialog.setTitle("Vuelo");
                    dialog.setHeaderText("Vuelo creada correctamente");
                    dialog.show();
                } else {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("Vuelo");
                    dialog.setHeaderText("Algo ha fallado");
                    dialog.show();
                }
            } else {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("Vuelo");
                dialog.setHeaderText("Este avión no estará disponible en esa ciudad de salida (" + this.menuCiudadesSalida.getText() + ") en esa fecha");
                dialog.show();
            }

        }
    }

```

### Ventana para elminar un vuelo

Apartado donde se gestionará el `borrado de vuelos`.

  <p align="center">
    <img src="./images/Del-Vuelo.png" alt="Login Screen">
  </p>


#### Parte del controlador que ejecuta el borrado

``` java
    /**
     * Funcion para eliminar un Vuelo
     *
     * @see Gestioner#eliminarVuelo(int)
     */
    public void delVuelo() throws SQLException {
        // Vuelo seleccionado?
        if (this.vuelosList.getSelectionModel().isEmpty()) {
            Alert dialog = new Alert(AlertType.WARNING);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Seleccione un vuelo");
            dialog.show();
        } else {
            String[] vueloParts = this.vuelosList.getSelectionModel().getSelectedItem().replaceAll(" ", "").split("-");
            // Se ha borrado?
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
    }
```

### Ventana para añadir una ciudad

Apartado donde se gestionará el `añadido de nuevas ciudades`.

  <p align="center">
    <img src="./images/Add-Ciudad.png" alt="Login Screen">
  </p>


#### Parte del controlador donde ejecutamos el añadido de ciudad

``` java
    /**
     * Funcion para añadir una Ciudad
     *
     * @see Gestioner#registrarCiudad(String, String)
     */
    private void addCiudad() {
        // Estan los campos llenos?
        if (this.nombreCiudadField.getText().isEmpty() || this.nombrePaisField.getText().isEmpty()) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("Ciudad");
            dialog.setHeaderText("Rellene toos los campos");
            dialog.show();
        } else {
            // Se ha creado?
            if (Gestioner.registrarCiudad(this.nombreCiudadField.getText(), this.nombrePaisField.getText())) {
                Alert dialog = new Alert(AlertType.CONFIRMATION);
                dialog.setTitle("Ciudad");
                dialog.setHeaderText("Ciudad creada correctamente");
                dialog.show();
                this.nombreCiudadField.setText("");
            }
        }
    }
```


### Ventana para añadir un avión

Apartado donde se gestionará el `añadido de nueva avion`.

  <p align="center">
    <img src="./images/Add-Avion.png" alt="Login Screen">
  </p>


#### Parte del controlador donde ejecutamos el añadido del avion

``` java
    /**
     * Funcion para añadir un Avion
     *
     * @see Gestioner#registrarAvion(String, int)
     */
    private void addAvion() {
        // Estan los campos llenos?
        if (this.nombreAvionField.getText().isEmpty() || this.capacidadField.getText().isEmpty()) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("Avion");
            dialog.setHeaderText("Rellene todos los campos.");
            dialog.show();
        } else {
            // Manolo airlines no puede tener aviones con capacodad > 200
            if (Integer.parseInt(this.capacidadField.getText()) > 200) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("Capacidad");
                dialog.setHeaderText("Lo siento, nuestras aerolienas no pueden disponer de aviones de mas de 200 pasajeros,");
                dialog.show();
            } else {
                // Se ha creado el avión?
                if (Gestioner.registrarAvion(this.nombreAvionField.getText(), Integer.parseInt(this.capacidadField.getText()))) {
                    Alert dialog = new Alert(AlertType.CONFIRMATION);
                    dialog.setTitle("Avión");
                    dialog.setHeaderText("Avión creada correctamente");
                    dialog.show();
                }
            }
        }
    }
```


### Ventana para elminar un avión

Apartado donde se gestionará el `eliminado de un avión`.

  <p align="center">
    <img src="./images/Del-Avion.png" alt="Login Screen">
  </p>

#### Apartado donde se ejecuta la opcion del borrado

``` java
    /**
     * Funcion para eliminar un avión
     *
     * @see Gestioner#eliminarAvion(int)
     */
    public void delAvion() throws SQLException {
        // Hay algun avión selecionado?
        if (this.avionesList.getSelectionModel().isEmpty()) {
            Alert dialog = new Alert(AlertType.WARNING);
            dialog.setTitle("Vuelo");
            dialog.setHeaderText("Rellene todos los campos");
            dialog.show();
        } else {
            String[] vueloParts = this.avionesList.getSelectionModel().getSelectedItem().replaceAll(" ", "").split("-");
            // Se ha borrado el avion?
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
    }
```


### Ventana para reservar

Apartado donde se gestionará el `la reserva de un vuelo`.

  <p align="center">
    <img src="./images/Reservar-Screen.png" alt="Login Screen">
  </p>


#### Parte que reserva el vuelo

``` java
    /**
     * Reservar un vuelo seleccionado
     *
     * @see #modificarReserva()
     * @see Getter
     * @see Gestioner
     */
    private void reservar() throws SQLException {

        Vuelo selectedVuelo = vuelosDisponiblesTable.getSelectionModel().getSelectedItem();
        // Hay algun vuelo selecionado?
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
            
            // Cargar la ventana de la seleccion de asientos
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

                    // El asiento esta libre?
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
```

  <p align="center"> <br>
    <img src="./images/Asiento-Reserva-Seleccion.png" alt="Login Screen">
  </p> <br>

``` java
            dialog.setResultConverter(buttonType -> {
                // Han aceptado la reserva?
                if (buttonType == reservar) {
                    int selectedAsiento = selectedSeat[0];
                    
                    // Han seleccionado un asiento?
                    if (selectedAsiento != -1) {
                        try {
                            int outReserva = Gestioner.reservarVuelo(Getter.getUsernameID(GlobalData.userName), vueloID, selectedAsiento);
                            // Se ha reservado?
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
                                // Quieren descargar el PDF?
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
```


### Mis Reservas Screen

Apartado simiplar al de reservar un vuelo, pero para listar `nuestras reservas` y asi poder `eliminar una reserva`
`Modificar una Reserva` o `Desarcar un justificante`

  <p align="center">
    <img src="./images/Mis-Reservas-Screen.png" alt="Login Screen">
  </p>

#### Apartado donde se ejecuta la opcion de modificar una reserva

``` java
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
        
```

<p align="center">
    <img src="./images/Modificar-Reserva-Asiento-Screen.png" alt="Login Screen">
</p>

``` java

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
```



#### Generar el PDF

``` java
    /**
     * Generar un justificante del vuelo con formato PDF
     *
     * @param pdfText  Texto del PDF
     * @param savePath Ruta donde se guarda el PDF
     * @return True si el PDF se ha guardado exitosamente, False de lo contrario
     */
    public static boolean createPDF(String pdfText, String savePath) {
        Document PDFdocument = new Document();
        try {
            if (!savePath.contains(".pdf"))
                savePath += ".pdf";
            PdfWriter.getInstance(PDFdocument, new FileOutputStream(savePath));
            PDFdocument.open();
            Image topImage = Image.getInstance(new URL("https://raw.githubusercontent.com/Zenin0/Proyecto-DAM/main/App/src/main/resources/app/css/header.png"));
            topImage.setAlignment(Element.ALIGN_CENTER);
            topImage.scaleToFit(500, 300);
            PDFdocument.add(topImage);
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
            Paragraph titleParagraph = new Paragraph("Justificante de Vuelo", titleFont);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleParagraph.setSpacingBefore(60);
            titleParagraph.setSpacingAfter(60);
            PDFdocument.add(titleParagraph);
            Font infoFont = new Font(Font.FontFamily.HELVETICA, 18);
            Paragraph infoParagraph = new Paragraph(pdfText, infoFont);
            infoParagraph.setAlignment(Element.ALIGN_CENTER);
            infoParagraph.setSpacingAfter(20);
            PDFdocument.add(infoParagraph);
            Image qrCodeImage = Image.getInstance("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://github.com/Zenin0/Proyecto-DAM");
            qrCodeImage.setAlignment(Element.ALIGN_CENTER);
            qrCodeImage.scaleAbsolute(150, 150);
            float qrCodeX = (PDFdocument.getPageSize().getWidth() - qrCodeImage.getScaledWidth()) / 2;
            qrCodeImage.setAbsolutePosition(qrCodeX, 50);
            PDFdocument.add(qrCodeImage);
            PDFdocument.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
```

#### PDF de ejemplo

  <p align="center">
    <img src="./images/PDF-Example.png" alt="Login Screen">
  </p>

<!-- ROADMAP -->

## Roadmap

- [X] Crear y configurar la BDD
- [X] Código App
    - [X] Login / Registro
        - [X] Creación de Usuarios
            - [X] Administradores y no Administradores
        - [X] Login de Usuarios
            - [X] Redirigir a los Administradores al inicio de administración
            - [X] Redirigir a los no Administradores a el inicio de Usuariosç
    - [X] Administración
        - [X] Añadir Ciudades con sus respectivos paises
        - [X] Añadir Aviones con su nombr y capacidad
        - [X] Añadir Vuelos
            - [X] Mostrar las ciudades con un desplegable
            - [X] Mostrar Aviones con un desplegable
            - [X] Añadir fecha con un calendario
    - [X] Usuarios
        - [X] Reservar vuelos
            - [X] Mostrar los vuelos con una tabla
                - [X] Filtrar los vuelos por ciudad de salida y de destino
            - [X] Asientos seleccionables
            - [X] Generar PDF con los datos de la reserva
        - [X] Mis Reservas
            - [X] Mostrar las Reservas con una tabla
            - [X] Filtrar Reservas por Ciudad de Destino y Ciudad de Salida
                - [X] Eliminar una Reserva
                - [X] Modificar una Reserva
                    - [X] Asientos seleccionables
                    - [X] Generar PDF con los datos de la reserva
                - [X] Descargar justificante de vuelo como PDF

Mira los  [problemas abiertos](https://github.com/Zenin0/Proyecto-DAM/issues) para una lista completa de las
propuestas (y errores conocidos).




<!-- LICENCIA --> 

## Licencia

Distribuida por la licencia CC0 (Creative Commons Zero). Mira `LICENSE` para mas información.



<!-- CONTACTO -->

## Contacto

Isaac - isaacsanzgomez102125@gmail.com

Project Link: [https://github.com/Zenin0/Proyecto-DAM](https://github.com/Zenin0/Proyecto-DAM)
