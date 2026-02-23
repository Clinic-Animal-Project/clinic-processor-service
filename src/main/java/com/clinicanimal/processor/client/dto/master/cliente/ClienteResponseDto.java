package com.clinicanimal.processor.client.dto.master.cliente;

public record ClienteResponseDto(
        Long id,
        String nombre,
        String apellido,
        String dni,
        String telefono,
        String direccion,
        String email
) {}
