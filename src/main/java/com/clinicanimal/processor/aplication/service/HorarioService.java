package com.clinicanimal.processor.aplication.service;

import com.clinicanimal.processor.web.dto.horario.HorarioRequestDto;
import com.clinicanimal.processor.web.dto.horario.HorarioResponseDto;

import java.util.List;

public interface HorarioService {

    List<HorarioResponseDto> listar();

    HorarioResponseDto buscarPorId(Long id);

    List<HorarioResponseDto> listarPorPersonal(Long personalId);

    HorarioResponseDto crear(HorarioRequestDto dto);

    HorarioResponseDto actualizar(Long id, HorarioRequestDto dto);

    void desactivar(Long id);
}
