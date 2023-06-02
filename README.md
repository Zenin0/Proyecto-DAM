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

<!-- Ejemplos de uso -->

## Uso

### Login Screen

  <p align="center">
    <img src="./images/Login-Screen.png" alt="Login Screen">
  </p>

Apartado donde se hara el `inicio de sesión`, en el que en funcion de el tipo de usuario que hace login se redirigira al
apartado de los administradores o al apartado de usuarios normales

#### Parte del controlador
``` java
    /**
     * Login de los usuarios
     *
     * @see Gestioner#login(String, String)
     */
    private void login() throws IOException, SQLException {

        Alert dialog = new Alert(AlertType.CONFIRMATION);
        // Login de un usuario Administrador
        if (Gestioner.login(this.usuLog.getText(), this.passLog.getText()) == 1) {
            dialog.setTitle("¡Login correcto!");
            dialog.setHeaderText("¡Bienvenido " + Getter.getNombreAndApellidos(Getter.getUsernameID(this.usuLog.getText())) + "!");
            dialog.show();
            App.setRoot("inicio_admin");
        // Login de un usuario Normal
        } else if (Gestioner.login(this.usuLog.getText(), this.passLog.getText()) == 0) {
            dialog.setTitle("¡Login correcto!");
            dialog.setHeaderText("¡Bienvenido " + Getter.getNombreAndApellidos(Getter.getUsernameID(this.usuLog.getText())) + "!");
            dialog.show();
            App.setRoot("inicio_user");
        // Fallo del Login
        } else {
            dialog.setTitle("Login Incorrecto");
            dialog.setHeaderText("Inicio de sesión incorrecto");
            dialog.show();
        }
    }
```

#### Parte logica del login
``` java
    /**
     * Login de los usuarios
     *
     * @param usuario Nombre del usuario introducido
     * @param pass    Contraseña introducida
     * @return 1 Acceso de un usuario admin   0 Acceso de un usuario no admin -1  Error en el login
     * @see MD5Hasher#getMd5()
     */
    public static int login(String usuario, String pass) {
        try {

            MD5Hasher md5 = new MD5Hasher(pass);
            String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
            PreparedStatement checkStatement = App.con.prepareStatement(query);
            checkStatement.setString(1, usuario);
            checkStatement.setString(2, md5.getMd5());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count > 0) {
                query = "SELECT Is_Admin FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
                checkStatement = App.con.prepareStatement(query);
                checkStatement.setString(1, usuario);
                checkStatement.setString(2, md5.getMd5());
                resultSet = checkStatement.executeQuery();
                resultSet.next();
                if (resultSet.getInt(1) == 1) {
                    GlobalData.userName = usuario;
                    return 1;
                } else if (resultSet.getInt(1) == 0) {
                    GlobalData.userName = usuario;
                    return 0;
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }
        return -1;
    }
```
### Register Screen

  <p align="center">
    <img src="./images/Register-Screen.png" alt="Login Screen">
  </p>



``` java
    /**
     * Registrar un usuario
     *
     * @param Usuario Nombre de usuario
     * @param Pass1   Contraseña 1
     * @param Pass2   Contraseña 2
     * @param admin   Checkbox de Administrador
     * @return Si se ha creado o si no
     * @see MD5Hasher#getMd5()
     */
    public static boolean registrar(String Usuario, String nombre, String apellidos, String Pass1, String Pass2, boolean admin) {
        int id;
        try {
            if (!Pass1.equals(Pass2)) {
                Alert dialog = new Alert(AlertType.ERROR);
                dialog.setTitle("ERROR");
                dialog.setHeaderText("Las contraseñas no coinciden");
                dialog.show();
                return false;
            } else {
                MD5Hasher md5 = new MD5Hasher(Pass1);
                String query = "SELECT COUNT(*) FROM Usuarios WHERE Nombre_Usuario = ? AND Pass = ?";
                PreparedStatement checkStatement = App.con.prepareStatement(query);
                checkStatement.setString(1, Usuario);
                checkStatement.setString(2, md5.getMd5());
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    Alert dialog = new Alert(AlertType.ERROR);
                    dialog.setTitle("ERROR");
                    dialog.setHeaderText("Este Usuario y esta contraseña ya existen");
                    dialog.show();
                    return false;

                } else {

                    if (admin) {
                        if (!new MD5Hasher(new PassDialog().showAndWait().get()).getMd5().equals(new MD5Hasher("root").getMd5())) {
                            Alert dialog = new Alert(AlertType.ERROR);
                            dialog.setTitle("ERROR");
                            dialog.setHeaderText("Contraseña de administrador incorrecta");
                            dialog.show();
                            return false;
                        }
                    }

                    String test = "SELECT max(ID_Usuario) FROM Usuarios";
                    PreparedStatement prst = App.con.prepareStatement(test);
                    ResultSet resulttest = prst.executeQuery();
                    if (resulttest.next()) {
                        id = resulttest.getInt(1);

                        String consulta = "INSERT INTO Usuarios (ID_Usuario, Nombre_Usuario, Nombre, Apellidos, Pass, is_Admin) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement insertStatement = App.con.prepareStatement(consulta);
                        insertStatement.setInt(1, id + 1);
                        insertStatement.setString(2, Usuario);
                        insertStatement.setString(3, nombre);
                        insertStatement.setString(4, apellidos);
                        insertStatement.setString(5, md5.getMd5());
                        insertStatement.setBoolean(6, admin);
                        insertStatement.executeUpdate();

                        Alert dialog = new Alert(AlertType.CONFIRMATION);
                        dialog.setTitle("Usuario");
                        dialog.setHeaderText("Usuario creado correctamente");
                        dialog.show();
                        return true;
                    }

                }
            }

        } catch (SQLException e) {
            Alert dialog = new Alert(AlertType.ERROR);
            dialog.setTitle("ERROR");
            dialog.setContentText("Error en la BDD: " + e.getErrorCode() + "-" + e.getMessage());
            dialog.show();
        }

        return false;
    }

```

Apartado donde se hará el `registro de un usuario`, marcando o no la casilla de admnistrador, lo que hará que salte un
prompt que pedira la contraseña de administrador de la aplicacion (`root`)

### Añadir Vuelo Screen

  <p align="center">
    <img src="./images/Add-Vuelo.png" alt="Login Screen">
  </p>


Apartado donde `crearemos un nuevo vuelo`, en base a la `ciudad de salida`
la `ciudad de destino` un `Avion` y una `fecha`

### Eliminar Vuelo Screen

  <p align="center">
    <img src="./images/Del-Vuelo.png" alt="Login Screen">
  </p>

Apartado donde podremos `borrar un vuelo` de la aplicación

### Añadir Ciudad Screen

  <p align="center">
    <img src="./images/Add-Ciudad.png" alt="Login Screen">
  </p>

Apartado donde `crearemos una ciudad` para la aplicación

### Añadir Avion Screen

  <p align="center">
    <img src="./images/Add-Avion.png" alt="Login Screen">
  </p>

Apartado donde `crearemos un avion` para la app

### Eliminar Avion Screen

  <p align="center">
    <img src="./images/Del-Avion.png" alt="Login Screen">
  </p>

Apartado donde podremos `borrar un avion` de la aplicación

### Reservar Screen

  <p align="center">
    <img src="./images/Reservar-Screen.png" alt="Login Screen">
  </p>

Apartado donde como un usuario podremos `reservar un vuelo` de los listados lo que conllevara la siguiente imagen

### Seleccion de Asientos Screen

  <p align="center">
    <img src="./images/Asiento-Reserva-Seleccion.png" alt="Login Screen">
  </p>

Apartado que aparecera al hacer una reserva para `seleccionar nuestro asiento` preferido del avion, incluyendo
una leyenda que indica el significado de los Iconos

### PDF Descarga Screen

  <p align="center">
    <img src="./images/PDF-Download-Screen.png" alt="Login Screen">
  </p>

Ventana de alerta que nos preguntara si queremos `descargar un PDF como justificante`

### Ejemplo PDF

  <p align="center">
    <img src="./images/PDF-Example.png" alt="Login Screen">
  </p>

`PDF` de ejemplo generado a partir de una reserva

### Mis Reservas Screen

  <p align="center">
    <img src="./images/Mis-Reservas-Screen.png" alt="Login Screen">
  </p>

Apartado simiplar al de reservar un vuelo, pero para listar `nuestras reservas` y asi poder `eliminar una reserva`
`Modificar una Reserva` o `Desarcar un justificante`

### Modificación de Reserva Screen

  <p align="center">
    <img src="./images/Modificar-Reserva-Asiento-Screen.png" alt="Login Screen">
  </p>

Apartado que aparecera al modificar una reserva para `seleccionar nuestro asiento` preferido del avion, incluyendo
una leyenda que indica el significado de los Iconos


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
