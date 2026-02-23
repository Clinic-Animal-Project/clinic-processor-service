package com.clinicanimal.processor.web.dto.cita;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CitaUpdateDto(

        @NotNull(message = "La fecha y hora de la cita es obligatoria")
        @Future(message = "La fecha y hora de la cita debe ser futura")
        LocalDateTime fechaHora,

        Long idVeterinario,

        @NotNull(message = "El recepcionista es obligatorio")
        Long idRecepcionista,

        @NotEmpty(message = "La cita debe tener al menos un servicio")
        @Valid
        List<CitaServicioRequestDto> servicios

) {}
