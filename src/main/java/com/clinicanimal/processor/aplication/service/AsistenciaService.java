package com.clinicanimal.processor.aplication.service;

import com.clinicanimal.processor.domain.model.estado.EstadoAsistencia;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaSalidaDto;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaRequestDto;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaService {

    List<AsistenciaResponseDto> listar();

    AsistenciaResponseDto buscarPorId(Long id);

    List<AsistenciaResponseDto> listarPorHorario(Long horarioId);

    List<AsistenciaResponseDto> listarPorFecha(LocalDate fecha);

    AsistenciaResponseDto registrar(AsistenciaRequestDto dto);

    AsistenciaResponseDto registrarSalida(Long id, AsistenciaSalidaDto dto);

    AsistenciaResponseDto actualizarEstado(Long id, EstadoAsistencia nuevoEstado);
}
