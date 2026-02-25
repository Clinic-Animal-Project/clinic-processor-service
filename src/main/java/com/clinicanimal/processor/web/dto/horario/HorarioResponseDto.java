package com.clinicanimal.processor.web.dto.horario;

import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.domain.model.estado.DiaSemana;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record HorarioResponseDto(
        Long id,
        PersonalResponseDto personal,
        List<DiaSemana> dias,
        LocalTime horaEntrada,
        LocalTime horaSalida,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
