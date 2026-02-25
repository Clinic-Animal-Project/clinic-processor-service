package com.clinicanimal.processor.web.dto.asistencia;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record AsistenciaSalidaDto(

        @NotNull(message = "La hora de salida es obligatoria")
        LocalTime horaSalida

) {}
