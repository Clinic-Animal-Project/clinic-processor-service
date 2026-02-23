package com.clinicanimal.processor.web.dto.cita;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CitaRequestDto(

        @NotNull(message = "La fecha y hora de la cita es obligatoria")
        @Future(message = "La fecha y hora de la cita debe ser futura")
        LocalDateTime fechaHora,

        @NotNull(message = "El área es obligatoria")
        Long idArea,

        @NotNull(message = "La mascota es obligatoria")
        Long idMascota,

        @NotNull(message = "El cliente es obligatorio")
        Long idCliente,

        @NotNull(message = "El recepcionista es obligatorio")
        Long idRecepcionista,

        Long idVeterinario,

        @NotEmpty(message = "La cita debe tener al menos un servicio")
        @Valid
        List<CitaServicioRequestDto> servicios

) {}
