package com.clinicanimal.processor.aplication.mapper;

import com.clinicanimal.processor.client.dto.master.area.AreaResponseDto;
import com.clinicanimal.processor.client.dto.master.cliente.ClienteResponseDto;
import com.clinicanimal.processor.client.dto.master.mascota.MascotaResponseDto;
import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.client.dto.master.servicios.ServiciosResponseDto;
import com.clinicanimal.processor.domain.model.Cita;
import com.clinicanimal.processor.domain.model.CitaServicio;
import com.clinicanimal.processor.web.dto.cita.CitaRequestDto;
import com.clinicanimal.processor.web.dto.cita.CitaResponseDto;
import com.clinicanimal.processor.web.dto.cita.CitaServicioRequestDto;
import com.clinicanimal.processor.web.dto.cita.CitaServicioResponseDto;
import com.clinicanimal.processor.web.dto.cita.CitaUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CitaMapper {

    // Request → Entidad
    // estado viene del DTO, los servicios se asignan en el service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "citaServicios", ignore = true)
    Cita toEntity(CitaRequestDto dto);

    // Request servicio → Entidad servicio
    // subTotal se calcula en el service (cantidad * precioBase), cita se asigna manualmente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cita", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    CitaServicio toServicioEntity(CitaServicioRequestDto dto);

    // Entidad → Response
    // Los datos de FK externos no están en la entidad, llegan como parámetros desde el service
    @Mapping(target = "id", source = "cita.id")
    @Mapping(target = "fechaHora", source = "cita.fechaHora")
    @Mapping(target = "estado", source = "cita.estado")
    @Mapping(target = "area", source = "area")
    @Mapping(target = "mascota", source = "mascota")
    @Mapping(target = "cliente", source = "cliente")
    @Mapping(target = "veterinario", source = "veterinario")
    @Mapping(target = "servicios", ignore = true)
    CitaResponseDto toResponse(
            Cita cita,
            AreaResponseDto area,
            MascotaResponseDto mascota,
            ClienteResponseDto cliente,
            PersonalResponseDto veterinario
    );

    // Entidad servicio → Response servicio
    // El objeto ServiciosResponseDto llega del Feign al service
    @Mapping(target = "servicio", source = "servicio")
    CitaServicioResponseDto toServicioResponse(CitaServicio citaServicio, ServiciosResponseDto servicio);

    // Update: aplica los campos del UpdateDto sobre la entidad existente
    // estado, citaServicios, idArea, idMascota, idCliente se preservan intactos
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "idArea", ignore = true)
    @Mapping(target = "idMascota", ignore = true)
    @Mapping(target = "idCliente", ignore = true)
    @Mapping(target = "citaServicios", ignore = true)
    void updateEntity(CitaUpdateDto dto, @MappingTarget Cita cita);
}
