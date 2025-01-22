CREATE TABLE respuestas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    borrado BIT(1) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    solucion BIT(1) NOT NULL,
    ultima_actualizacion DATETIME NOT NULL,
    discusion_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (discusion_id) REFERENCES discusion(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);