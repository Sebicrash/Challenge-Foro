CREATE TABLE discusiones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estado ENUM('OPEN', 'CLOSED', 'DELETED') NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    ultima_actualizacion DATETIME NOT NULL,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);