package com.clinicanimal.processor.aplication.service.impl;

import com.clinicanimal.processor.aplication.mapper.CitaMapper;
import com.clinicanimal.processor.aplication.service.CitaService;
import com.clinicanimal.processor.client.*;
import com.clinicanimal.processor.client.dto.master.area.AreaResponseDto;
import com.clinicanimal.processor.client.dto.master.cliente.ClienteResponseDto;
import com.clinicanimal.processor.client.dto.master.enums.EstadoPersonal;
import com.clinicanimal.processor.client.dto.master.mascota.MascotaResponseDto;
import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.client.dto.master.servicios.ServiciosResponseDto;
import com.clinicanimal.processor.domain.model.Cita;
import com.clinicanimal.processor.domain.model.CitaServicio;
import com.clinicanimal.processor.domain.model.estado.EstadoCita;
import com.clinicanimal.processor.domain.repository.ICitaRepository;
import com.clinicanimal.processor.web.dto.cita.CitaEncolarDto;
import com.clinicanimal.processor.web.dto.cita.CitaReprogramarDto;
import com.clinicanimal.processor.web.dto.cita.CitaRequestDto;
import com.clinicanimal.processor.web.dto.cita.CitaResponseDto;
import com.clinicanimal.processor.web.dto.cita.CitaServicioResponseDto;
import com.clinicanimal.processor.web.dto.cita.CitaUpdateDto;
import com.clinicanimal.processor.domain.exception.ResourceNotFoundException;
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
                .map(this::citaResuesta)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponseDto buscarPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));
        return citaResuesta(cita);
    }

    @Override
    @Transactional
    public CitaResponseDto crear(CitaRequestDto dto) {
        // Si la cita llega en EN_COLA, validar disponibilidad del veterinario
        if (dto.idVeterinario() != null) {
            PersonalResponseDto vet = personalClient
                    .buscarPersonalPorId(dto.idVeterinario()).getBody().data();
            validarDisponibilidadVeterinario(vet);
        }

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
        return citaResuesta(guardada);
    }

    @Override
    @Transactional
    public CitaResponseDto actualizar(Long id, CitaUpdateDto dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));

        if (cita.getEstado().validarCambiarEstado()) {
            throw new IllegalArgumentException("No se puede modificar una cita en estado: " + cita.getEstado());
        }

        citaMapper.updateEntity(dto, cita);

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
        return citaResuesta(actualizada);
    }

    // PROGRAMADA → EN_COLA: asigna veterinario y valida su disponibilidad
    @Override
    @Transactional
    public CitaResponseDto encolarCita(Long id, CitaEncolarDto dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));

        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new IllegalArgumentException("Solo se puede encolar una cita en estado PROGRAMADA. Estado actual: " + cita.getEstado());
        }

        PersonalResponseDto vet = personalClient
                .buscarPersonalPorId(dto.idVeterinario()).getBody().data();
        validarDisponibilidadVeterinario(vet);

        cita.setIdVeterinario(dto.idVeterinario());
        cita.setEstado(EstadoCita.EN_COLA);
        Cita guardada = citaRepository.save(cita);
        return citaResuesta(guardada);
    }

    // EN_COLA → EN_PROGRESO: el veterinario inicia la consulta → pasa a OCUPADO
    @Override
    @Transactional
    public CitaResponseDto iniciarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));

        if (!cita.getEstado().puedeIniciar()) {
            throw new IllegalArgumentException("Solo se puede iniciar una cita en estado EN_COLA. Estado actual: " + cita.getEstado());
        }

        cita.setEstado(EstadoCita.EN_PROGRESO);
        citaRepository.save(cita);

        personalClient.actualizarEstado(cita.getIdVeterinario(), EstadoPersonal.OCUPADO);

        return citaResuesta(cita);
    }

    // EN_PROGRESO → TERMINADA: el veterinario finaliza la consulta → pasa a DISPONIBLE
    @Override
    @Transactional
    public CitaResponseDto terminarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));

        if (!cita.getEstado().puedeTerminar()) {
            throw new IllegalArgumentException("Solo se puede terminar una cita en estado EN_PROGRESO. Estado actual: " + cita.getEstado());
        }

        cita.setEstado(EstadoCita.TERMINADA);
        citaRepository.save(cita);

        personalClient.actualizarEstado(cita.getIdVeterinario(), EstadoPersonal.DISPONIBLE);

        return citaResuesta(cita);
    }

    // TERMINADA → PAGADA: la recepcionista confirma el cobro
    @Override
    @Transactional
    public CitaResponseDto registrarPago(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));

        if (!cita.getEstado().puedePagar()) {
            throw new IllegalArgumentException("Solo se puede registrar el pago de una cita en estado TERMINADA. Estado actual: " + cita.getEstado());
        }

        cita.setEstado(EstadoCita.PAGADA);
        Cita actualizada = citaRepository.save(cita);
        return citaResuesta(actualizada);
    }

    // PROGRAMADA o EN_COLA → CANCELADO: libera veterinario si habia uno asignado
    @Override
    @Transactional
    public CitaResponseDto cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));

        if (!cita.getEstado().puedeCancelar()) {
            throw new IllegalArgumentException("Solo se puede cancelar una cita en estado PROGRAMADA o EN_COLA. Estado actual: " + cita.getEstado());
        }

        if (cita.getIdVeterinario() != null) {
            personalClient.actualizarEstado(cita.getIdVeterinario(), EstadoPersonal.DISPONIBLE);
        }

        cita.setEstado(EstadoCita.CANCELADO);
        Cita actualizada = citaRepository.save(cita);
        return citaResuesta(actualizada);
    }

    // EN_COLA → PROGRAMADA: nueva fecha, se limpia el veterinario asignado
    @Override
    @Transactional
    public CitaResponseDto reprogramarCita(Long id, CitaReprogramarDto dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", id));

        if (cita.getEstado().validarReProgramar()) {
            throw new IllegalArgumentException("Solo se puede reprogramar una cita en estado EN_COLA. Estado actual: " + cita.getEstado());
        }

        // Liberar al veterinario que estaba asignado
        if (cita.getIdVeterinario() != null) {
            personalClient.actualizarEstado(cita.getIdVeterinario(), EstadoPersonal.DISPONIBLE);
        }

        cita.setFechaHora(dto.fechaHora());
        cita.setIdVeterinario(null);
        cita.setEstado(EstadoCita.PROGRAMADA);
        Cita actualizada = citaRepository.save(cita);
        return citaResuesta(actualizada);
    }

    // Todas las citas de un estado especifico
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDto> listarPorEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado)
                .stream()
                .map(this::citaResuesta)
                .toList();
    }

    // Bandeja del veterinario: EN_COLA + EN_PROGRESO + TERMINADA filtradas por su id
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDto> listarBandejaVeterinario(Long idVeterinario) {
        List<EstadoCita> estadosBandeja = List.of(
                EstadoCita.EN_COLA,
                EstadoCita.EN_PROGRESO,
                EstadoCita.TERMINADA
        );
        return citaRepository.findByIdVeterinarioAndEstadoIn(idVeterinario, estadosBandeja)
                .stream()
                .map(this::citaResuesta)
                .toList();
    }

    // Historial de citas de una mascota
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDto> listarPorMascota(Long idMascota) {
        return citaRepository.findByIdMascota(idMascota)
                .stream()
                .map(this::citaResuesta)
                .toList();
    }

    // Historial de citas de un cliente (dueño)
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponseDto> listarPorCliente(Long idCliente) {
        return citaRepository.findByIdCliente(idCliente)
                .stream()
                .map(this::citaResuesta)
                .toList();
    }

    // Construye el CitaResponseDto enriquecido con datos de los Feign clients
    private CitaResponseDto citaResuesta(Cita cita) {
        AreaResponseDto area = areaClient
                .buscarAreaPorId(cita.getIdArea()).getBody().data();

        MascotaResponseDto mascota = mascotaClient
                .buscarMascotaPorId(cita.getIdMascota()).getBody().data();

        ClienteResponseDto cliente = clienteClient
                .buscarClientePorId(cita.getIdCliente()).getBody().data();

        PersonalResponseDto veterinario = cita.getIdVeterinario() != null
                ? personalClient.buscarPersonalPorId(cita.getIdVeterinario()).getBody().data()
                : null;

        List<CitaServicioResponseDto> servicios = cita.getCitaServicios().stream()
                .map(cs -> {
                    ServiciosResponseDto servicio = serviciosClient
                            .buscarServicioPorId(cs.getIdServicio()).getBody().data();
                    return citaMapper.toServicioResponse(cs, servicio);
                }).toList();

        CitaResponseDto response = citaMapper.toResponse(cita, area, mascota, cliente, veterinario);

        return new CitaResponseDto(
                response.id(),
                response.fechaHora(),
                response.estado(),
                response.area(),
                response.mascota(),
                response.cliente(),
                response.veterinario(),
                servicios
        );
    }

    // Bloquea la asignacion si el veterinario no esta presente en la clinica
    private void validarDisponibilidadVeterinario(PersonalResponseDto vet) {
        EstadoPersonal estado = vet.estadoPersonal();
        if (estado == EstadoPersonal.DESCANSO || estado == EstadoPersonal.NO_DISPONIBLE) {
            throw new IllegalArgumentException(
                    "El veterinario " + vet.nombrePersonal() + " " + vet.apellidoPersonal()
                    + " no puede ser asignado. Estado actual: " + estado
            );
        }
    }
}
