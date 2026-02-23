package com.clinicanimal.processor.aplication.service.impl;

import com.clinicanimal.processor.aplication.mapper.CitaMapper;
import com.clinicanimal.processor.aplication.service.CitaService;
import com.clinicanimal.processor.client.*;
import com.clinicanimal.processor.client.dto.master.area.AreaResponseDto;
import com.clinicanimal.processor.client.dto.master.cliente.ClienteResponseDto;
import com.clinicanimal.processor.client.dto.master.mascota.MascotaResponseDto;
import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.client.dto.master.servicios.ServiciosResponseDto;
import com.clinicanimal.processor.domain.model.Cita;
import com.clinicanimal.processor.domain.model.CitaServicio;
import com.clinicanimal.processor.domain.repository.ICitaRepository;
import com.clinicanimal.processor.web.dto.cita.CitaRequestDto;
import com.clinicanimal.processor.web.dto.cita.CitaResponseDto;
import com.clinicanimal.processor.web.dto.cita.CitaServicioResponseDto;
import com.clinicanimal.processor.web.dto.cita.CitaUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final ICitaRepository citaRepository;
    private final CitaMapper citaMapper;

    private final IAreaFeignClient areaClient;
    private final IClienteFeignClient clienteClient;
    private final IMascotaFeignClient mascotaClient;
    private final IPersonalFeignClient personalClient;
    private final IServiciosFeignClient serviciosClient;

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDto> listar() {
        return citaRepository.findAll()
                .stream()
                .map(this::enrichCita)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponseDto buscarPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));
        return enrichCita(cita);
    }

    @Override
    @Transactional
    public CitaResponseDto crear(CitaRequestDto dto) {
        Cita cita = citaMapper.toEntity(dto);

        List<CitaServicio> servicios = dto.servicios().stream()
                .map(s -> {
                    CitaServicio cs = citaMapper.toServicioEntity(s);
                    cs.setSubTotal(s.cantidad() * s.precioBase());
                    cs.setCita(cita);
                    return cs;
                }).toList();

        cita.getCitaServicios().addAll(servicios);
        Cita guardada = citaRepository.save(cita);
        return enrichCita(guardada);
    }

    @Override
    @Transactional
    public CitaResponseDto actualizar(Long id, CitaUpdateDto dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));

        if (cita.getEstado().validarCambiarEstado()) {
            throw new RuntimeException("No se puede modificar una cita en estado: " + cita.getEstado());
        }

        citaMapper.updateEntity(dto, cita);

        // reemplaza los servicios por completo
        cita.getCitaServicios().clear();
        List<CitaServicio> nuevosServicios = dto.servicios().stream()
                .map(s -> {
                    CitaServicio cs = citaMapper.toServicioEntity(s);
                    cs.setSubTotal(s.cantidad() * s.precioBase());
                    cs.setCita(cita);
                    return cs;
                }).toList();
        cita.getCitaServicios().addAll(nuevosServicios);

        Cita actualizada = citaRepository.save(cita);
        return enrichCita(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));

        if (cita.getEstado().validarCambiarEstado()) {
            throw new RuntimeException("No se puede eliminar una cita en estado: " + cita.getEstado());
        }

        citaRepository.delete(cita);
    }

    // --- helper privado: enriquece una Cita con los datos del master-service ---
    private CitaResponseDto enrichCita(Cita cita) {
        AreaResponseDto area = areaClient
                .buscarAreaPorId(cita.getIdArea()).getBody().data();

        MascotaResponseDto mascota = mascotaClient
                .buscarMascotaPorId(cita.getIdMascota()).getBody().data();

        ClienteResponseDto cliente = clienteClient
                .buscarClientePorId(cita.getIdCliente()).getBody().data();

        PersonalResponseDto recepcionista = personalClient
                .buscarPersonalPorId(cita.getIdRecepcionista()).getBody().data();

        PersonalResponseDto veterinario = cita.getIdVeterinario() != null
                ? personalClient.buscarPersonalPorId(cita.getIdVeterinario()).getBody().data()
                : null;

        List<CitaServicioResponseDto> servicios = cita.getCitaServicios().stream()
                .map(cs -> {
                    ServiciosResponseDto servicio = serviciosClient
                            .buscarServicioPorId(cs.getIdServicio()).getBody().data();
                    return citaMapper.toServicioResponse(cs, servicio);
                }).toList();

        CitaResponseDto response = citaMapper.toResponse(cita, area, mascota, cliente, recepcionista, veterinario);

        // servicios se completan aparte porque el mapper los ignora (necesitan llamadas Feign)
        return new CitaResponseDto(
                response.id(),
                response.fechaHora(),
                response.estado(),
                response.area(),
                response.mascota(),
                response.cliente(),
                response.recepcionista(),
                response.veterinario(),
                servicios
        );
    }
}
