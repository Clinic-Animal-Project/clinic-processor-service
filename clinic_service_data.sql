-- ============================================================
--  BASE DE DATOS: clinic_animal_processor
-- ============================================================
CREATE DATABASE IF NOT EXISTS clinic_animal_processor
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE clinic_animal_processor;

-- ------------------------------------------------------------
-- HORARIO
-- ------------------------------------------------------------
CREATE TABLE horario (
    id_horario  BIGINT          NOT NULL AUTO_INCREMENT,
    id_personal BIGINT          NOT NULL,
    dias        VARCHAR(50)     NOT NULL, -- Ejemplo: "Lunes-Viernes"
    hora_entrada TIME           NOT NULL,
    hora_salida  TIME           NOT NULL,
    is_active   TINYINT(1)      NOT NULL DEFAULT 1,
    created_at  DATETIME        NOT NULL,
    updated_at  DATETIME        NOT NULL,
    PRIMARY KEY (id_horario),
    CONSTRAINT fk_horario_personal FOREIGN KEY (id_personal) REFERENCES personal (id_personal)
);

-- ------------------------------------------------------------
-- ASISTENCIA
-- ------------------------------------------------------------
CREATE TABLE asistencia (
    id_asistencia BIGINT          NOT NULL AUTO_INCREMENT,
    id_horario    BIGINT          NOT NULL,
    fecha         DATE            NOT NULL,
    hora_entrada  TIME            NOT NULL,
    hora_salida   TIME            NOT NULL,
    estado_asistencia VARCHAR(20) NOT NULL, -- Presente, Tarde, Ausente
    is_active     TINYINT(1)      NOT NULL DEFAULT 1,
    created_at    DATETIME        NOT NULL,
    updated_at    DATETIME        NOT NULL,
    PRIMARY KEY (id_asistencia),
    CONSTRAINT fk_asistencia_horario FOREIGN KEY (id_horario) REFERENCES horario (id_horario)
);

-- ------------------------------------------------------------
-- CITA
-- ------------------------------------------------------------
CREATE TABLE cita (
    id_cita       BIGINT          NOT NULL AUTO_INCREMENT,
    id_cliente    BIGINT          NOT NULL,
    id_mascota    BIGINT          NOT NULL,
    id_area       BIGINT          NOT NULL,
    id_veterinario BIGINT         NOT NULL,
    fecha_hora    DATETIME        NOT NULL,
    estado        VARCHAR(20)     NOT NULL DEFAULT 'PROGRAMADA', -- Ejemplo: PROGRAMADA, COMPLETADA, CANCELADA
    is_active     TINYINT(1)      NOT NULL DEFAULT 1,
    created_at    DATETIME        NOT NULL,
    updated_at    DATETIME        NOT NULL,
    PRIMARY KEY (id_cita),
    CONSTRAINT fk_cita_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente),
    CONSTRAINT fk_cita_mascota FOREIGN KEY (id_mascota) REFERENCES mascota (id_mascota),
    CONSTRAINT fk_cita_area FOREIGN KEY (id_area) REFERENCES areas (id_areas),
    CONSTRAINT fk_cita_veterinario FOREIGN KEY (id_veterinario) REFERENCES personal (id_personal)
);

-- ------------------------------------------------------------
-- CITA_SERVICIO
-- ------------------------------------------------------------
CREATE TABLE cita_servicio (
    id_cita_servicio BIGINT          NOT NULL AUTO_INCREMENT,
    id_cita          BIGINT          NOT NULL,
    id_servicio      BIGINT          NOT NULL,
    cantidad         INT             NOT NULL DEFAULT 1,
    precio_base      DOUBLE          NOT NULL,
    sub_total        DOUBLE          NOT NULL,
    created_at       DATETIME        NOT NULL,
    updated_at       DATETIME        NOT NULL,
    PRIMARY KEY (id_cita_servicio),
    CONSTRAINT fk_cita_servicio_cita FOREIGN KEY (id_cita) REFERENCES cita (id_cita),
    CONSTRAINT fk_cita_servicio_servicio FOREIGN KEY (id_servicio) REFERENCES servicios (id_servicios)
);

-- ============================================================
--  DATOS DE PRUEBA
-- ============================================================
-- HORARIOS (1 por veterinario)
INSERT INTO horario (id_personal, dias, hora_entrada, hora_salida, is_active, created_at, updated_at) VALUES
(1, 'Lunes-Viernes', '09:00:00', '17:00:00', 1, NOW(), NOW()),
(2, 'Lunes-Viernes', '10:00:00', '18:00:00', 1, NOW(), NOW()),
(3, 'Lunes-Viernes', '11:00:00', '19:00:00', 1, NOW(), NOW());

-- ASISTENCIA (1 por horario)
INSERT INTO asistencia (id_horario, fecha, hora_entrada, hora_salida, estado_asistencia, is_active, created_at, updated_at) VALUES
(1, CURDATE(), '09:00:00', '17:00:00', 'Presente', 1, NOW(), NOW()),
(2, CURDATE(), '10:15:00', '18:00:00', 'Tarde', 1, NOW(), NOW()),
(3, CURDATE(), '11:00:00', '19:00:00', 'Presente', 1, NOW(), NOW());

-- CITAS (5 registros)
INSERT INTO cita (id_cliente, id_mascota, id_area, id_veterinario, fecha_hora, estado, is_active, created_at, updated_at) VALUES
(1, 1, 1, 1, NOW() + INTERVAL 1 DAY, 'PROGRAMADA', 1, NOW(), NOW()),
(2, 2, 1, 2, NOW() + INTERVAL 2 DAY, 'PROGRAMADA', 1, NOW(), NOW()),
(3, 3, 1, 3, NOW() + INTERVAL 3 DAY, 'PROGRAMADA', 1, NOW(), NOW()),
(4, 4, 2, 1, NOW() + INTERVAL 4 DAY, 'PROGRAMADA', 1, NOW(), NOW()),
(5, 5, 3, 2, NOW() + INTERVAL 5 DAY, 'PROGRAMADA', 1, NOW(), NOW());

-- CITA_SERVICIO (1 servicio por cita)
INSERT INTO cita_servicio (id_cita, id_servicio, cantidad, precio_base, sub_total, created_at, updated_at) VALUES
(1, 1, 1, 50.00, 50.00, NOW(), NOW()),
(2, 1, 1, 50.00, 50.00, NOW(), NOW()),
(3, 2, 1, 35.00, 35.00, NOW(), NOW()),
(4, 6, 1, 260.00, 260.00, NOW(), NOW()),
(5, 10, 1, 45.00, 45.00, NOW(), NOW());