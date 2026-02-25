package com.clinicanimal.processor.web.dto.cita;

import com.clinicanimal.processor.domain.model.estado.EstadoCita;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
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

        // Solo PROGRAMADA o EN_COLA permitidos al crear
        @NotNull(message = "El estado es obligatorio")
        EstadoCita estado,

        // Requerido solo si estado == EN_COLA
        Long idVeterinario,

        @NotEmpty(message = "La cita debe tener al menos un servicio")
        @Valid
        List<CitaServicioRequestDto> servicios

) {
    // Validacion cruzada: EN_COLA exige veterinario
    @AssertTrue(message = "El veterinario es obligatorio cuando el estado es EN_COLA")
    public boolean isVeterinarioValido() {
        if (estado == null) return true;
        if (estado == EstadoCita.EN_COLA) return idVeterinario != null;
        return true;
    }

    // Validacion cruzada: solo PROGRAMADA o EN_COLA son estados validos al crear
    @AssertTrue(message = "Solo se permite crear una cita en estado PROGRAMADA o EN_COLA")
    public boolean isEstadoValido() {
        if (estado == null) return true;
        return !estado.validarEstadoCrear();
    }
}
