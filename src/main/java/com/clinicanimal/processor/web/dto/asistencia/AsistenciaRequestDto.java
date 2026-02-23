package com.clinicanimal.processor.web.dto.asistencia;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.time.LocalTime;

public record AsistenciaRequestDto(

        @NotNull(message = "El horario es obligatorio")
        Long idHorario,

        @NotNull(message = "La fecha es obligatoria")
        @PastOrPresent(message = "La fecha de asistencia no puede ser futura")
        LocalDate fecha,

        LocalTime horaEntrada,

        LocalTime horaSalida

) {}
