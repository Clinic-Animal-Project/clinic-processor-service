package com.clinicanimal.processor.aplication.mapper;

import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.domain.model.Horario;
import com.clinicanimal.processor.web.dto.horario.HorarioRequestDto;
import com.clinicanimal.processor.web.dto.horario.HorarioResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HorarioMapper {

    // Request → Entidad
    // MapStruct usa setters directamente, por lo que accede sin problema
    // a los campos heredados de Auditable. Solo ignoramos id y los campos
    // de auditoría que Spring rellena automáticamente (@CreatedDate, @LastModifiedDate)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Horario toEntity(HorarioRequestDto dto);

    // Entidad → Response
    // Fuentes explícitas para evitar ambigüedad entre horario.id y personal.id
    @Mapping(target = "id", source = "horario.id")
    @Mapping(target = "personal", source = "personal")
    @Mapping(target = "dias", source = "horario.dias")
    @Mapping(target = "horaEntrada", source = "horario.horaEntrada")
    @Mapping(target = "horaSalida", source = "horario.horaSalida")
    @Mapping(target = "createdAt", source = "horario.createdAt")
    @Mapping(target = "updatedAt", source = "horario.updatedAt")
    HorarioResponseDto toResponse(Horario horario, PersonalResponseDto personal);
}
