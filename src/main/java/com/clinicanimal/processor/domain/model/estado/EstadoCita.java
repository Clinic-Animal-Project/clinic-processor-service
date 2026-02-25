package com.clinicanimal.processor.domain.model.estado;

public enum EstadoCita {
    PROGRAMADA, EN_COLA, EN_PROGRESO, TERMINADA, PAGADA, CANCELADO;

    // Estados no permitidos al crear una cita
    public boolean validarEstadoCrear() {
        return this != PROGRAMADA && this != EN_COLA;
    }

    // Estados en los que la cita ya no se puede editar (datos/servicios)
    public boolean validarCambiarEstado() {
        return this == EN_PROGRESO || this == TERMINADA || this == PAGADA || this == CANCELADO;
    }

    // Estados en los que NO se puede reprogramar
    public boolean validarReProgramar() {
        return this == EN_PROGRESO || this == TERMINADA || this == PAGADA || this == CANCELADO;
    }

    // Veterinario: EN_COLA → EN_PROGRESO
    public boolean puedeIniciar() {
        return this == EN_COLA;
    }

    // Veterinario: EN_PROGRESO → TERMINADA
    public boolean puedeTerminar() {
        return this == EN_PROGRESO;
    }

    // Recepcionista: TERMINADA → PAGADA
    public boolean puedePagar() {
        return this == TERMINADA;
    }

    // Cancelable solo desde PROGRAMADA o EN_COLA
    public boolean puedeCancelar() {
        return this == PROGRAMADA || this == EN_COLA;
    }
}
