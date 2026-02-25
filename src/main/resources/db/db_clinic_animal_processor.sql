-- ============================================================
--  BASE DE DATOS: clinic_animal_processor
-- ============================================================
DROP DATABASE  IF EXISTS clinic_animal_processor;
CREATE DATABASE IF NOT EXISTS clinic_animal_processor;
USE clinic_animal_processor;
-- ------------------------------------------------------------
-- HORARIO
-- ------------------------------------------------------------
create table horario
(
    id_horario   bigint auto_increment
        primary key,
    created_at   datetime(6) not null,
    is_active    boolean         null,
    updated_at   datetime(6) not null,
    hora_entrada time        not null,
    hora_salida  time        not null,
    personal_id  bigint      not null
);
-- ------------------------------------------------------------
-- ASISTENCIA
-- ------------------------------------------------------------
create table asistencia
(
    id_asistencia bigint auto_increment
        primary key,
    estado        enum ('FALTA', 'FUERA_DE_SERVICIO', 'PRESENTE', 'TARDANZA') not null,
    fecha         date                                                        not null,
    hora_entrada  time                                                        null,
    hora_salida   time                                                        null,
    id_horario    bigint                                                      not null,
    constraint FK_id_horario
        foreign key (id_horario) references horario (id_horario)
);
-- ------------------------------------------------------------
-- CITA
-- ------------------------------------------------------------
create table cita
(
    id_cita        bigint auto_increment
        primary key,
    estado         enum ('CANCELADO', 'EN_COLA', 'EN_PROGRESO', 'PAGADA', 'PROGRAMADA', 'TERMINADA') not null,
    fecha_hora     datetime(6)                                                                       null,
    id_area        bigint                                                                            not null,
    id_cliente     bigint                                                                            not null,
    id_mascota     bigint                                                                            not null,
    id_veterinario bigint                                                                            null
);
-- ------------------------------------------------------------
-- CITA_SERVICIO
-- ------------------------------------------------------------
create table cita_servicio
(
    id_cita_serv bigint auto_increment
        primary key,
    cantidad     int    null,
    id_servicio  bigint not null,
    precio_base  double null,
    sub_total    double null,
    id_cita      bigint null,
    constraint FK_id_cita_servicio
        foreign key (id_cita) references cita (id_cita)
);

create table horario_dias
(
    id_horario bigint                                                                          not null,
    dia        enum ('DOMINGO', 'JUEVES', 'LUNES', 'MARTES', 'MIERCOLES', 'SABADO', 'VIERNES') null,
    constraint FK_horario_dias
        foreign key (id_horario) references horario (id_horario)
);
-- ============================================================
--  DATOS DE PRUEBA
-- ============================================================
-- HORARIOS (1 por veterinario)


INSERT INTO cita (id_cliente, id_mascota, id_area, id_veterinario, fecha_hora, estado) VALUES
                                                                                           (1, 1, 1, 1, NOW() + INTERVAL 1 DAY, 'PROGRAMADA'),
                                                                                           (2, 2, 1, 2, NOW() + INTERVAL 2 DAY, 'PROGRAMADA'),
                                                                                           (3, 3, 1, 3, NOW() + INTERVAL 3 DAY, 'PROGRAMADA'),
                                                                                           (4, 4, 2, 1, NOW() + INTERVAL 4 DAY, 'PROGRAMADA'),
                                                                                           (5, 5, 3, 2, NOW() + INTERVAL 5 DAY, 'PROGRAMADA');


INSERT INTO cita_servicio (id_cita, id_servicio, cantidad, precio_base, sub_total) VALUES
                                                                                       (1, 1, 1, 50.00, 50.00),
                                                                                       (2, 1, 1, 50.00, 50.00),
                                                                                       (3, 2, 1, 35.00, 35.00),
                                                                                       (4, 6, 1, 260.00, 260.00),
                                                                                       (5, 10, 1, 45.00, 45.00);


insert into clinic_animal_processor.horario ( personal_id, is_active, hora_entrada, hora_salida,
                                              created_at,updated_at)values
                                                                        (1, true, '09:00:00', '17:00:00',  NOW(), NOW()),
                                                                        (2, true, '10:00:00', '18:00:00',  NOW(), NOW()),
                                                                        (3, true, '11:00:00', '19:00:00',  NOW(), NOW());

insert into horario_dias (id_horario, dia)
values (1,'LUNES'),
       (1,'MIERCOLES'),
       (1,'VIERNES');

insert into horario_dias (id_horario, dia)
values (2,'DOMINGO'),
       (2,'SABADO'),
       (2,'VIERNES');

insert into horario_dias (id_horario, dia)
values (3,'LUNES'),
       (3,'MARTES'),
       (4,'SABADO');


insert into clinic_animal_processor.asistencia ( id_horario,estado, hora_entrada, hora_salida,fecha )values
                                                                                                         (1, 'PRESENTE', '09:00:00', '17:00:00',  NOW()),
                                                                                                         (2, 'PRESENTE', '10:15:00', '18:00:00', NOW()),
                                                                                                         (3, 'PRESENTE', '11:00:00', '19:00:00',  NOW());