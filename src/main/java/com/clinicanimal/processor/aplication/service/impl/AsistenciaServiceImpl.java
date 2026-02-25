package com.clinicanimal.processor.aplication.service.impl;

import com.clinicanimal.processor.aplication.mapper.AsistenciaMapper;
import com.clinicanimal.processor.aplication.service.AsistenciaService;
import com.clinicanimal.processor.client.IPersonalFeignClient;
import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.domain.model.Asistencia;
import com.clinicanimal.processor.domain.model.Horario;
import com.clinicanimal.processor.domain.model.estado.EstadoAsistencia;
import com.clinicanimal.processor.domain.repository.IAsistenciaRepository;
import com.clinicanimal.processor.domain.repository.IHorarioRepository;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaSalidaDto;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaRequestDto;
import com.clinicanimal.processor.web.dto.asistencia.AsistenciaResponseDto;
import com.clinicanimal.processor.web.dto.asistencia.HorarioSummaryDto;
import com.clinicanimal.processor.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaServiceImpl implements AsistenciaService {

    private final IAsistenciaRepository asistenciaRepository;
    private final IHorarioRepository horarioRepository;
    private final AsistenciaMapper asistenciaMapper;
    private final IPersonalFeignClient personalClient;

    @Override
    public List<AsistenciaResponseDto> listar() {
        return asistenciaRepository.findAll()
                .stream()
                .map(this::asistenciaRespuesta)
                .toList();
    }

    @Override
    public AsistenciaResponseDto buscarPorId(Long id) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", id));
        return asistenciaRespuesta(asistencia);
    }

    @Override
    public List<AsistenciaResponseDto> listarPorHorario(Long horarioId) {
        return asistenciaRepository.findByHorarioId(horarioId)
                .stream()
                .map(this::asistenciaRespuesta)
                .toList();
    }

    @Override
    public List<AsistenciaResponseDto> listarPorFecha(LocalDate fecha) {
        return asistenciaRepository.findByFecha(fecha)
                .stream()
                .map(this::asistenciaRespuesta)
                .toList();
    }

    @Override
    public AsistenciaResponseDto registrar(AsistenciaRequestDto dto) {
        // Validar que el horario exista
        Horario horario = horarioRepository.findById(dto.idHorario())
                .orElseThrow(() -> new ResourceNotFoundException("Horario", dto.idHorario()));

        // Evitar duplicado: un personal no puede tener dos registros en el mismo día para el mismo horario
        if (asistenciaRepository.existsByHorarioIdAndFecha(dto.idHorario(), dto.fecha())) {
            throw new IllegalArgumentException("Ya existe un registro de asistencia para este horario en la fecha: " + dto.fecha());
        }

        Asistencia asistencia = asistenciaMapper.toEntity(dto);
        asistencia.setHorario(horario);

        // Calcular estado automáticamente
        EstadoAsistencia estado = calcularEstado(dto, horario);
        asistencia.setEstadoAsistencia(estado);

        asistencia = asistenciaRepository.save(asistencia);
        return asistenciaRespuesta(asistencia);
    }

    // EN_PROGRESO → registra la hora de salida del empleado
    @Override
    public AsistenciaResponseDto registrarSalida(Long id, AsistenciaSalidaDto dto) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", id));

        if (asistencia.getEstadoAsistencia() == EstadoAsistencia.FALTA ||
            asistencia.getEstadoAsistencia() == EstadoAsistencia.FUERA_DE_SERVICIO) {
            throw new IllegalArgumentException(
                "No se puede registrar salida en una asistencia con estado: "
                + asistencia.getEstadoAsistencia()
            );
        }
        if (asistencia.getHoraSalida() != null) {
            throw new IllegalArgumentException("La salida ya fue registrada para esta asistencia");
        }

        asistencia.setHoraSalida(dto.horaSalida());
        asistencia = asistenciaRepository.save(asistencia);
        return asistenciaRespuesta(asistencia);
    }

    @Override
    public AsistenciaResponseDto actualizarEstado(Long id, EstadoAsistencia nuevoEstado) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia", id));
        asistencia.setEstadoAsistencia(nuevoEstado);
        asistencia = asistenciaRepository.save(asistencia);
        return asistenciaRespuesta(asistencia);
    }

    // -------------------------------------------------------
    // HELPERS PRIVADOS
    // -------------------------------------------------------

    private AsistenciaResponseDto asistenciaRespuesta(Asistencia asistencia) {
        Horario horario = asistencia.getHorario();
        PersonalResponseDto personal = obtenerPersonal(horario.getPersonalId());
        HorarioSummaryDto horarioSummary = asistenciaMapper.toHorarioSummary(horario, personal);
        return asistenciaMapper.toResponse(asistencia, horarioSummary);
    }

    private PersonalResponseDto obtenerPersonal(Long personalId) {
        var response = personalClient.buscarPersonalPorId(personalId);
        if (response == null || response.getBody() == null || response.getBody().data() == null) {
            throw new ResourceNotFoundException("Personal", personalId);
        }
        return response.getBody().data();
    }

    /**
     * Determina el EstadoAsistencia en base a la horaEntrada registrada
     * comparada con la horaEntrada del horario asignado.
     *
     * - Sin horaEntrada → FALTA
     * - horaEntrada <= horario.horaEntrada → PRESENTE
     * - horaEntrada > horario.horaEntrada → TARDANZA
     */
    private EstadoAsistencia calcularEstado(AsistenciaRequestDto dto, Horario horario) {
        if (dto.horaEntrada() == null) {
            return EstadoAsistencia.FALTA;
        }
        if (!dto.horaEntrada().isAfter(horario.getHoraEntrada())) {
            return EstadoAsistencia.PRESENTE;
        }
        return EstadoAsistencia.TARDANZA;
    }
}
