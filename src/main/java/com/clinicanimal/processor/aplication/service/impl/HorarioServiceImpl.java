package com.clinicanimal.processor.aplication.service.impl;

import com.clinicanimal.processor.aplication.mapper.HorarioMapper;
import com.clinicanimal.processor.aplication.service.HorarioService;
import com.clinicanimal.processor.client.IPersonalFeignClient;
import com.clinicanimal.processor.client.dto.master.personal.PersonalResponseDto;
import com.clinicanimal.processor.domain.model.Horario;
import com.clinicanimal.processor.domain.repository.IHorarioRepository;
import com.clinicanimal.processor.web.dto.horario.HorarioRequestDto;
import com.clinicanimal.processor.web.dto.horario.HorarioResponseDto;
import com.clinicanimal.processor.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioServiceImpl implements HorarioService {

    private final IHorarioRepository horarioRepository;
    private final HorarioMapper horarioMapper;
    private final IPersonalFeignClient personalClient;

    @Override
    public List<HorarioResponseDto> listar() {
        return horarioRepository.findAll()
                .stream()
                .map(this::horarioRespuesta)
                .toList();
    }

    @Override
    public HorarioResponseDto buscarPorId(Long id) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario", id));
        return horarioRespuesta(horario);
    }

    @Override
    public List<HorarioResponseDto> listarPorPersonal(Long personalId) {
        return horarioRepository.findByPersonalId(personalId)
                .stream()
                .map(this::horarioRespuesta)
                .toList();
    }

    @Override
    public HorarioResponseDto crear(HorarioRequestDto dto) {
        // Validar que el personal exista en el master-service
        PersonalResponseDto personal = obtenerPersonal(dto.personalId());

        Horario horario = horarioMapper.toEntity(dto);
        horario = horarioRepository.save(horario);
        return horarioMapper.toResponse(horario, personal);
    }

    @Override
    public HorarioResponseDto actualizar(Long id, HorarioRequestDto dto) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario", id));

        // Validar que el personal exista en el master-service
        PersonalResponseDto personal = obtenerPersonal(dto.personalId());

        horario.setPersonalId(dto.personalId());
        horario.setDias(dto.dias());
        horario.setHoraEntrada(dto.horaEntrada());
        horario.setHoraSalida(dto.horaSalida());

        horario = horarioRepository.save(horario);
        return horarioMapper.toResponse(horario, personal);
    }

    @Override
    public void desactivar(Long id) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario", id));
        horario.setActive(false);
        horarioRepository.save(horario);
    }

    // -------------------------------------------------------
    // HELPERS PRIVADOS
    // -------------------------------------------------------

    private HorarioResponseDto horarioRespuesta(Horario horario) {
        PersonalResponseDto personal = obtenerPersonal(horario.getPersonalId());
        return horarioMapper.toResponse(horario, personal);
    }

    private PersonalResponseDto obtenerPersonal(Long personalId) {
        var response = personalClient.buscarPersonalPorId(personalId);
        if (response == null || response.getBody() == null || response.getBody().data() == null) {
            throw new ResourceNotFoundException("Personal", personalId);
        }
        return response.getBody().data();
    }
}
