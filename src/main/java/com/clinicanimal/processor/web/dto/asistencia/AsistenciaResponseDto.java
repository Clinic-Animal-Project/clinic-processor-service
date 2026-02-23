package com.clinicanimal.processor.web.dto.asistencia;

import com.clinicanimal.processor.domain.model.estado.EstadoAsistencia;

import java.time.LocalDate;
import java.time.LocalTime;

public record AsistenciaResponseDto(
        Long id,
        HorarioSummaryDto horario,
        LocalDate fecha,
        LocalTime horaEntrada,
        LocalTime horaSalida,
        EstadoAsistencia estadoAsistencia
) {}
