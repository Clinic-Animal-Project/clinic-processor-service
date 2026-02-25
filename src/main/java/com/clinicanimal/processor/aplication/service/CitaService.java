package com.clinicanimal.processor.aplication.service;

import com.clinicanimal.processor.domain.model.estado.EstadoCita;
import com.clinicanimal.processor.web.dto.cita.CitaEncolarDto;
import com.clinicanimal.processor.web.dto.cita.CitaReprogramarDto;
import com.clinicanimal.processor.web.dto.cita.CitaRequestDto;
import com.clinicanimal.processor.web.dto.cita.CitaResponseDto;
import com.clinicanimal.processor.web.dto.cita.CitaUpdateDto;

import java.util.List;

public interface CitaService {

    // --- Consultas generales ---
    List<CitaResponseDto> listar();
    CitaResponseDto buscarPorId(Long id);

    // Listar todas las citas de un estado especifico (ej: todas las PROGRAMADAS)
    List<CitaResponseDto> listarPorEstado(EstadoCita estado);

    // Bandeja del veterinario: EN_COLA + EN_PROGRESO + TERMINADA filtradas por su id
    List<CitaResponseDto> listarBandejaVeterinario(Long idVeterinario);

    // Historial de citas de una mascota
    List<CitaResponseDto> listarPorMascota(Long idMascota);

    // Historial de citas de un cliente (dueño)
    List<CitaResponseDto> listarPorCliente(Long idCliente);

    // --- Operaciones CRUD ---
    CitaResponseDto crear(CitaRequestDto dto);
    CitaResponseDto actualizar(Long id, CitaUpdateDto dto);

    // --- Cambios de estado ---
    // PROGRAMADA → EN_COLA: asigna veterinario
    CitaResponseDto encolarCita(Long id, CitaEncolarDto dto);

    // EN_COLA → EN_PROGRESO: veterinario inicia la consulta (vet pasa a OCUPADO)
    CitaResponseDto iniciarCita(Long id);

    // EN_PROGRESO → TERMINADA: veterinario finaliza la consulta (vet pasa a DISPONIBLE)
    CitaResponseDto terminarCita(Long id);

    // TERMINADA → PAGADA: recepcionista confirma el cobro
    CitaResponseDto registrarPago(Long id);

    // PROGRAMADA o EN_COLA → CANCELADO
    CitaResponseDto cancelarCita(Long id);

    // EN_COLA → PROGRAMADA: libera veterinario y cambia fecha
    CitaResponseDto reprogramarCita(Long id, CitaReprogramarDto dto);
}
