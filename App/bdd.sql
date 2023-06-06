DROP DATABASE IF EXISTS `Manolo_Airlines`;
CREATE DATABASE `Manolo_Airlines`;

USE `Manolo_Airlines`;

CREATE TABLE `Usuarios`(
    ID_Usuario INT NOT NULL PRIMARY KEY,
    Nombre_Usuario VARCHAR(255) NOT NULL,
    Nombre VARCHAR(255),
    Apellidos VARCHAR(255),
    Pass VARCHAR(255) NOT NULL,
    Is_Admin BOOLEAN NOT NULL,
    Image LONGBLOB

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
    Capacidad INT NOT NULL,
    CONSTRAINT PK_Aviones PRIMARY KEY(ID_Avion)
);


CREATE TABLE `Vuelos`(
    ID_Vuelo INT NOT NULL ,
    Ciudad_Salida INT NOT NULL,
    Ciudad_Destino INT NOT NULL,
    ID_Avion INT NOT NULL,
    Fecha_Salida DATE NOT NULL,
    Creada varchar(255) NOT NULL,
    CONSTRAINT PK_Vuelos PRIMARY KEY(ID_Vuelo),
    CONSTRAINT FK_Ciudad_Salida FOREIGN KEY (Ciudad_Salida) REFERENCES Ciudades(ID_Ciudad) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_Ciudad_Destino FOREIGN KEY (Ciudad_Destino) REFERENCES Ciudades(ID_Ciudad) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_Avion FOREIGN KEY (ID_Avion) REFERENCES Aviones(ID_Avion) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `Reservas`(
    ID_Reserva INT NOT NULL ,
    ID_Usuario INT NOT NULL,
    ID_Vuelo INT NOT NULL,
    Asiento INT NOT NULL,
    CONSTRAINT PK_Reservas PRIMARY KEY(ID_Reserva),
    CONSTRAINT FK_Usuarios FOREIGN KEY (ID_Usuario) REFERENCES Usuarios(ID_Usuario) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_Vuelos FOREIGN KEY (ID_Vuelo) REFERENCES Vuelos(ID_Vuelo) ON DELETE CASCADE ON UPDATE CASCADE
);

/* Descomentar para hacer inserts
INSERT INTO `Ciudades` (ID_Ciudad, Nombre_Ciudad, Pais) VALUES
                                                            (1, 'Madrid', 'España'),
                                                            (2, 'Barcelona', 'España'),
                                                            (3, 'Londres', 'Reino Unido'),
                                                            (4, 'París', 'Francia'),
                                                            (5, 'Roma', 'Italia'),
                                                            (6, 'Nueva York', 'Estados Unidos'),
                                                            (7, 'Berlín', 'Alemania'),
                                                            (8, 'Ámsterdam', 'Países Bajos'),
                                                            (9, 'Tokio', 'Japón'),
                                                            (10, 'Sídney', 'Australia');

-- Inserts de algunos aviones
INSERT INTO `Aviones` (ID_Avion, Nombre_Avion, Capacidad) VALUES
                                                              (1, 'Boeing 747', 200),
                                                              (2, 'Airbus A320', 180),
                                                              (3, 'Embraer E195', 120),
                                                              (4, 'Boeing 787', 90),
                                                              (5, 'Airbus A380', 150),
                                                              (6, 'Bombardier CRJ900', 90),
                                                              (7, 'Boeing 737', 200),
                                                              (8, 'Airbus A330', 120),
                                                              (9, 'Embraer E190', 110),
                                                              (10, 'Cessna 172', 4);

INSERT INTO `Vuelos` (ID_Vuelo, Ciudad_Salida, Ciudad_Destino, ID_Avion, Fecha_Salida, Creada) VALUES
                                                                                                   (1, 1, 2, 1, '2023-06-05', '2023-06-01 10:00:00'),
                                                                                                   (2, 2, 3, 2, '2023-06-10', '2023-06-01 11:30:00'),
                                                                                                   (3, 3, 4, 3, '2023-06-15', '2023-06-01 12:45:00'),
                                                                                                   (4, 4, 1, 4, '2023-06-20', '2023-06-01 14:15:00'),
                                                                                                   (5, 5, 2, 5, '2023-06-25', '2023-06-01 15:30:00'),
                                                                                                   (6, 6, 3, 6, '2023-06-30', '2023-06-01 16:45:00'),
                                                                                                   (7, 7, 4, 7, '2023-07-05', '2023-06-01 18:00:00'),
                                                                                                   (8, 8, 1, 8, '2023-07-10', '2023-06-01 19:15:00'),
                                                                                                   (9, 9, 2, 9, '2023-07-15', '2023-06-01 20:30:00'),
                                                                                                   (10, 10, 3, 10, '2023-07-20', '2023-06-01 21:45:00'),
                                                                                                   (11, 1, 3, 1, '2023-07-25', '2023-06-01 22:00:00'),
                                                                                                   (12, 2, 4, 2, '2023-07-30', '2023-06-01 23:15:00'),
                                                                                                   (13, 3, 1, 3, '2023-08-05', '2023-06-02 10:30:00'),
                                                                                                   (14, 4, 2, 4, '2023-08-10', '2023-06-02 11:45:00'),
                                                                                                   (15, 5, 3, 5, '2023-08-15', '2023-06-02 13:00:00'),
                                                                                                   (16, 6, 4, 6, '2023-08-20', '2023-06-02 14:15:00'),
                                                                                                   (17, 7, 1, 7, '2023-08-25', '2023-06-02 15:30:00'),
                                                                                                   (18, 8, 2, 8, '2023-08-30', '2023-06-02 16:45:00'),
                                                                                                   (19, 9, 3, 9, '2023-09-05', '2023-06-02 18:00:00'),
                                                                                                   (20, 10, 4, 10, '2023-09-10', '2023-06-02 19:15:00'),
                                                                                                   (21, 1, 4, 1, '2023-09-15', '2023-06-02 20:30:00'),
                                                                                                   (22, 2, 1, 2, '2023-09-20', '2023-06-02 21:45:00'),
                                                                                                   (23, 3, 2, 3, '2023-09-25', '2023-06-02 22:00:00'),
                                                                                                   (24, 4, 3, 4, '2023-09-30', '2023-06-02 23:15:00'),
                                                                                                   (25, 5, 1, 5, '2023-10-05', '2023-06-03 10:30:00'),
                                                                                                   (26, 6, 2, 6, '2023-10-10', '2023-06-03 11:45:00'),
                                                                                                   (27, 7, 3, 7, '2023-10-15', '2023-06-03 13:00:00'),
                                                                                                   (28, 8, 4, 8, '2023-10-20', '2023-06-03 14:15:00'),
                                                                                                   (29, 9, 1, 9, '2023-10-25', '2023-06-03 15:30:00'),
                                                                                                   (30, 10, 2, 10, '2023-10-30', '2023-06-03 16:45:00');


*/
INSERT INTO `Usuarios` (ID_Usuario, Nombre, Apellidos, Nombre_Usuario, Pass, is_Admin) VALUES (1, NULL, NULL, "root", "63a9f0ea7bb98050796b649e85481845", true);

SET GLOBAL event_scheduler = ON;

CREATE EVENT borrar_vuelos_pasados
    ON SCHEDULE
        EVERY 10 MINUTE
            STARTS CURRENT_TIMESTAMP + INTERVAL 10 MINUTE
    DO
    DELETE FROM Vuelos
    WHERE Fecha_Salida < CURDATE();


