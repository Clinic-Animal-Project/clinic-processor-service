package com.clinicanimal.processor.web.dto.horario;

import com.clinicanimal.processor.domain.model.estado.DiaSemana;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

public record HorarioRequestDto(

        @NotNull(message = "El personal es obligatorio")
        Long personalId,

        @NotEmpty(message = "Debe especificarse al menos un día de trabajo")
        List<DiaSemana> dias,

        @NotNull(message = "La hora de entrada es obligatoria")
        LocalTime horaEntrada,

        @NotNull(message = "La hora de salida es obligatoria")
        LocalTime horaSalida

) {}
