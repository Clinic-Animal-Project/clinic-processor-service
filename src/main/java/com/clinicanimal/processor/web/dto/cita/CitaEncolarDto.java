package com.clinicanimal.processor.web.dto.cita;

import jakarta.validation.constraints.NotNull;

public record CitaEncolarDto(

        @NotNull(message = "El veterinario es obligatorio para encolar una cita")
        Long idVeterinario

) {}
