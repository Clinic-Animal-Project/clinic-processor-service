package com.clinicanimal.processor.web.dto.cita;

import com.clinicanimal.processor.client.dto.master.area.AreaResponseDto;
import com.clinicanimal.processor.client.dto.master.cliente.ClienteResponseDto;
import com.clinicanimal.processor.client.dto.master.mascota.MascotaResponseDto;
import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.domain.model.estado.EstadoCita;

import java.time.LocalDateTime;
import java.util.List;

public record CitaResponseDto(
        Long id,
        LocalDateTime fechaHora,
        EstadoCita estado,
        AreaResponseDto area,
        MascotaResponseDto mascota,
        ClienteResponseDto cliente,
        PersonalResponseDto veterinario,
        List<CitaServicioResponseDto> servicios
) {}
