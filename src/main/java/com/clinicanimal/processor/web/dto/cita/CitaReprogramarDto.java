package com.clinicanimal.processor.web.dto.cita;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CitaReprogramarDto(

        @NotNull(message = "La nueva fecha y hora es obligatoria")
        @Future(message = "La fecha y hora debe ser futura")
        LocalDateTime fechaHora

) {}
