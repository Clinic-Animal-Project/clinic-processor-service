package com.clinicanimal.processor.web.dto.cita;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CitaServicioRequestDto(

        @NotNull(message = "El servicio es obligatorio")
        Long idServicio,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidad,

        @NotNull(message = "El precio base es obligatorio")
        @Positive(message = "El precio base debe ser mayor a 0")
        Double precioBase

) {}
