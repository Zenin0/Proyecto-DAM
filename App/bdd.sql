DROP DATABASE IF EXISTS `Manolo_Airlines`;
CREATE DATABASE `Manolo_Airlines`;

USE `Manolo_Airlines`;

CREATE TABLE `Usuarios`(
    id VARCHAR(255) NOT NULL,
    Nombre_Usuario VARCHAR(255) NOT NULL,
    Pass VARCHAR(255) NOT NULL,
    Tipo VARCHAR(255) NOT NULL

);