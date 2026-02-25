package com.clinicanimal.processor.web.dto.cita;

import com.clinicanimal.processor.client.dto.master.servicios.ServiciosResponseDto;

public record CitaServicioResponseDto(
        ServiciosResponseDto servicio,
        Integer cantidad,
        double precioBase,
        double subTotal
) {}
