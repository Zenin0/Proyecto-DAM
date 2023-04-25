DROP DATABASE IF EXISTS `Manolo_Airlines`;
CREATE DATABASE `Manolo_Airlines`;

USE `Manolo_Airlines`;

CREATE TABLE `Usuarios`(
    ID_Usuario INT NOT NULL PRIMARY KEY,
    Nombre_Usuario VARCHAR(255) NOT NULL,
    Pass VARCHAR(255) NOT NULL,
    Is_Admin BOOLEAN NOT NULL

);

CREATE TABLE `Ciudades`(
    ID_Ciudad INT NOT NULL,
    Nombre_Ciudad VARCHAR(255) NOT NULL,
    Pais VARCHAR(255) NOT NULL,
    CONSTRAINT PK_Ciudades PRIMARY KEY(ID_Ciudad)

);

CREATE TABLE `Aviones`(
    ID_Avion INT NOT NULL ,
    Nombre_Avion VARCHAR(255) NOT NULL,
    Anyo_Fabricacion INT NOT NULl,
    CONSTRAINT PK_Aviones PRIMARY KEY(ID_Avion)
);


CREATE TABLE `Vuelos`(
    ID_Vuelo INT NOT NULL ,
    Ciudad_Salida INT NOT NULL,
    Ciudad_Destino INT NOT NULL,
    N_Asientos INT NOT NULL,
    ID_Avion INT NOT NULL,
    Fecha_Salida DATE NOT NULL,
    CONSTRAINT PK_Vuelos PRIMARY KEY(ID_Vuelo),
    CONSTRAINT FK_Ciudad_Salida FOREIGN KEY (Ciudad_Salida) REFERENCES Ciudades(ID_Ciudad) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_Ciudad_Destino FOREIGN KEY (Ciudad_Destino) REFERENCES Ciudades(ID_Ciudad) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `Reservas`(
    ID_Reserva INT NOT NULL ,
    ID_Usuario INT NOT NULL,
    ID_Vuelo INT NOT NULL,
    CONSTRAINT PK_Reservas PRIMARY KEY(ID_Reserva),
    CONSTRAINT FK_Usuarios FOREIGN KEY (ID_Usuario) REFERENCES Usuarios(ID_Usuario) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_Vuelos FOREIGN KEY (ID_Vuelo) REFERENCES Vuelos(ID_Vuelo) ON DELETE CASCADE ON UPDATE CASCADE
);


INSERT INTO `Usuarios` (ID_Usuario, Nombre_Usuario, Pass, is_Admin) VALUES (1, "root", "63a9f0ea7bb98050796b649e85481845", true)