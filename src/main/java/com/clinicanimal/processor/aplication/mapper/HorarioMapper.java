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
    // isActive = true por default en Auditable, no necesita expression
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Horario toEntity(HorarioRequestDto dto);

    // Entidad → Response
    // El personal no está en la entidad (solo personalId), llega como parámetro desde el service
    @Mapping(target = "personal", source = "personal")
    HorarioResponseDto toResponse(Horario horario, PersonalResponseDto personal);
}
