package com.clinicanimal.processor.aplication.service;

import com.clinicanimal.processor.web.dto.cita.CitaRequestDto;
import com.clinicanimal.processor.web.dto.cita.CitaResponseDto;
import com.clinicanimal.processor.web.dto.cita.CitaUpdateDto;

import java.util.List;

public interface CitaService {
    List<CitaResponseDto> listar();
    CitaResponseDto buscarPorId(Long id);
    CitaResponseDto crear(CitaRequestDto dto);
    CitaResponseDto actualizar(Long id, CitaUpdateDto dto);
    void eliminar(Long id);
}
