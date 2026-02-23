package com.clinicanimal.processor.client.dto.master.roles;

public record RolesResponseDto(
        Long id,
        String rolNombre,
        String descripcion,
        boolean accesoWeb
) {}
