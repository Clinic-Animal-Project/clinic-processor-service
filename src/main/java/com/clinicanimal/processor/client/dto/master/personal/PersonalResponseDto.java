package com.clinicanimal.processor.client.dto.master.personal;

import com.clinicanimal.processor.client.dto.master.enums.EstadoPersonal;

import java.time.LocalDate;

public record PersonalResponseDto(
        Long id,
        String nombrePersonal,
        String apellidoPersonal,
        int edad,
        String dni,
        String email,
        String telefono,
        Long idRol,
        String nombreRol,
        Long idArea,
        String nomArea,
        LocalDate fechaRegistro,
        EstadoPersonal estadoPersonal
) {}
