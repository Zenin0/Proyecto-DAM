DROP DATABASE IF EXISTS `Manolo_Airlines`;
CREATE DATABASE `Manolo_Airlines`;

USE `Manolo_Airlines`;

CREATE TABLE `Usuarios`(
    ID int NOT NULL PRIMARY KEY,
    Nombre_Usuario VARCHAR(255) NOT NULL,
    Pass VARCHAR(255) NOT NULL,
    Is_Admin BOOLEAN NOT NULL

);

CREATE TABLE `Ciudades`(
    ID int NOT NULL PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Pais VARCHAR(255) NOT NULL

);

CREATE TABLE `Aviones`(
    ID int NOT NULL PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Pasajeros INT NOT NULL,
);

CREATE TABLE `Vuelos`(
    ID int NOT NULL PRIMARY KEY,
    Ciudad_Salida INT NOT NULL,
    Ciudad_Destino INT NOT NULL,
    Avion INT NOT NULL,
    Fecha_Salida DATE NOT NULL
);


INSERT INTO `Usuarios` (id, Nombre_Usuario, Pass, is_Admin) VALUES (1, "root", "63a9f0ea7bb98050796b649e85481845", true)