package com.clinicanimal.processor.aplication.mapper;

import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.domain.model.Asistencia;
import com.clinicanimal.processor.domain.model.Horario;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaRequestDto;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaResponseDto;
import com.clinicanimal.processor.web.dto.asistencia.HorarioSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AsistenciaMapper {

    // Request → Entidad
    // horario se asigna manualmente desde el service (necesita buscarlo en BD)
    // estadoAsistencia lo calcula el service comparando horaEntrada con el horario
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "horario", ignore = true)
    @Mapping(target = "estadoAsistencia", ignore = true)
    Asistencia toEntity(AsistenciaRequestDto dto);

    // Entidad → Response
    @Mapping(target = "horario", source = "horarioSummary")
    AsistenciaResponseDto toResponse(Asistencia asistencia, HorarioSummaryDto horarioSummary);

    // Horario + Personal → HorarioSummaryDto (se arma en el service con los datos del Feign)
    @Mapping(target = "personalId", source = "horario.personalId")
    @Mapping(target = "nombrePersonal", source = "personal.nombrePersonal")
    @Mapping(target = "apellidoPersonal", source = "personal.apellidoPersonal")
    @Mapping(target = "id", source = "horario.id")
    @Mapping(target = "dias", source = "horario.dias")
    @Mapping(target = "horaEntrada", source = "horario.horaEntrada")
    @Mapping(target = "horaSalida", source = "horario.horaSalida")
    HorarioSummaryDto toHorarioSummary(Horario horario, PersonalResponseDto personal);
}
