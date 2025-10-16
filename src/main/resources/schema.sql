DROP TABLE IF EXISTS senadores;

-- Crea una tabla optimizada para la información de los senadores
CREATE TABLE senadores (
                           id VARCHAR(255) PRIMARY KEY,
                           nombre VARCHAR(255),
                           apellido VARCHAR(255),
                           bloque VARCHAR(255),
                           provincia VARCHAR(255),
                           partido VARCHAR(255),
                           email VARCHAR(255),
                           telefono VARCHAR(255),
                           mandato_inicio DATE,
                           mandato_fin DATE
);