package com.clinicanimal.processor.web.dto.asistencia;

import com.clinicanimal.processor.domain.model.estado.DiaSemana;

import java.time.LocalTime;
import java.util.List;

public record HorarioSummaryDto(
        Long id,
        Long personalId,
        String nombrePersonal,
        String apellidoPersonal,
        List<DiaSemana> dias,
        LocalTime horaEntrada,
        LocalTime horaSalida
) {}
