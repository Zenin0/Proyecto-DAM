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

Este proyecto se ha creado para `Manolo Airlines` para facilitar el uso de su base de datos mysql, que usan para gestionar su flota de Aeronaves, ademas de ser utilizado como proyecto del Grado Superior DAM del primer año, este se basa en 2 apartados, usuarios administradores, que gestionarán la creación de nuevos vuelos, destinos y demás, ademas de usuarios no administradores que se usarán para la reserva/cancelación de vuelos, y reservas



### Construido con

Este código estará creado con `Java` para el desarrollo principal de la aplicación, `MYSQL` para la gestión de la base de datos, y `JavaFX` para el apartado de GUI.

* [![Java](https://img.shields.io/badge/java-ED8B00?style=for-the-badge)](https://www.java.com)
* [![JavaFX](https://img.shields.io/badge/javafx-ED8B00?style=for-the-badge)](https://openjfx.io/)
* [![MySql](https://img.shields.io/badge/MYsql-3670A0?style=for-the-badge)](https://www.mysql.com/)


<!-- Ejemplos de uso -->
## Uso

### Login Screen

  <p align="center">
    <img src="./images/Login-Screen.png" alt="Login Screen">
  </p>

### Register Screen

  <p align="center">
    <img src="./images/Register-Screen.png" alt="Login Screen">
  </p>

### Añadir Vuelo Screen

  <p align="center">
    <img src="./images/AddVuelo.png" alt="Login Screen">
  </p>

### Eliminar Vuelo Screen

  <p align="center">
    <img src="./images/DelVuelo.png" alt="Login Screen">
  </p>

### Añadir Ciudad Screen

  <p align="center">
    <img src="./images/AddCiudad.png" alt="Login Screen">
  </p>

### Añadir Avion Screen

  <p align="center">
    <img src="./images/AddAvion.png" alt="Login Screen">
  </p>


### Reservar Screen

  <p align="center">
    <img src="./images/ReservarScreen.png" alt="Login Screen">
  </p>

### Seleccion de Asientos Screen

  <p align="center">
    <img src="./images/AsientoReservaSeleccion.png" alt="Login Screen">
  </p>

  ### PDF Descarga Screen

  <p align="center">
    <img src="./images/PDFDownloadScreeb.png" alt="Login Screen">
  </p>

### Ejemplo PDF

  <p align="center">
    <img src="./images/PDF-Example.png" alt="Login Screen">
  </p>

  ### Mis Reservas Screen

  <p align="center">
    <img src="./images/MisReservasScreen.png" alt="Login Screen">
  </p>

### Modificación de Reserva Screen

  <p align="center">
    <img src="./images/ModificarReservaAsientoScreen.png" alt="Login Screen">
  </p>


<!-- ROADMAP -->
## Roadmap

- [X] [Crear y configurar la BDD](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/bdd.sql)
- [ ] [Código App](https://github.com/Zenin0/Proyecto-DAM/tree/main/App/src/main/java/app)
  - [X] Gestión de los usuarios
    - [X] [Creación de los usuarios](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
      - [X] [Gestión de creación de usuarios administradores](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
      - [X] [Gestión de creación de usuarios no administradores](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
    - [X] [Login de los usuarios](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
      - [X] [Login de usuarios admin al inicio admin](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
      - [X] [Login de usuarios no admin al inicio usuario](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
  - [X]  Apartado Administradores 
    - [X] [Crear ciudades de destino, solo con usuarios administradores](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
    - [X] [Crear vuelos, solo con usuarios administradores](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
      - [X] [Listar ciudades de salida con un menú](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Getter.java)
      - [X] [Listar ciudades de destino con un menú](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Menus.java)
      - [X] [Listar aviones con un menú](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Getter.java)
      - [X] [Almacenar que usuario a creado ese vuelo](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/GlobalData.java)
      - [X] Selector de fechas 
    - [X] [Borrar vuelos ya creados](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
      - [X] [Listar los vuelos creados](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Getter.java)
  - [X] Apartado Usuarios 
    - [X] [Reservar vuelos](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
      - [X] [Funciones para filtrar la busqueda de vuelos](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Getter.java)
        - [X] Por Ciudad de Destino
        - [X] Por Ciuad de Salida
      - [X] Mostrar grid con imagenes de asientos para reservar el asiento
      - [X] Mostrar información de cada vuelo
        - [X] [Generar PDF con los datos de la Reserva](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
          - [X] Mostrar ruta donde descargar el fichero PDF
    - [X] Mis Reservas
      - [X] Listar vuelos de los usuarios normales con una tabla
        - [ ] Habilitar Filtrado por Ciudades
        - [X] [Mostrar información de cada uno de esos vuelos](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Getter.java)
          - [X] [Descargar PDF con la información del vuelo seleccionado](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
            - [X] [Mostrar ruta donde descargar el fichero PDF](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
      - [X] [Habilitar función de cancelación del vuelo](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
        - [X] [Listar vuelos disponibles con una lista](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Getter.java)
          - [X] [Mostrar información de cada vuelo](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Getter.java)
      - [X]  [Habilitar función de modificación del vuelo](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Gestioner.java)
      - [X] [Listar vuelos disponibles con una lista](https://github.com/Zenin0/Proyecto-DAM/blob/main/App/src/main/java/app/Getter.java)
      - [X] Mostrar grid con imagenes de asientos para reservar el asiento
      - [X] Modificar asiento
      - [X] Mostrar información de cada vuelo


Mira los  [problemas abiertos](https://github.com/Zenin0/Proyecto-DAM/issues) para una lista completa de las propuestas (y errores conocidos).




<!-- LICENCIA --> 
## Licencia

Distribuida por la licencia CC0 (Creative Commons Zero). Mira `LICENSE` para mas información.



<!-- CONTACTO -->
## Contacto

Isaac - isaacsanzgomez102125@gmail.com

Project Link: [https://github.com/Zenin0/Proyecto-DAM](https://github.com/Zenin0/Proyecto-DAM)
