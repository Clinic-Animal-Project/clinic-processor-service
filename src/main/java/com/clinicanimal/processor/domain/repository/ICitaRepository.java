package com.clinicanimal.processor.domain.repository;

import com.clinicanimal.processor.domain.model.Cita;
import com.clinicanimal.processor.domain.model.estado.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICitaRepository extends JpaRepository<Cita, Long> {

    // Listar por estado especifico (ej: todas las PROGRAMADAS)
    List<Cita> findByEstado(EstadoCita estado);

    // Listar por varios estados a la vez (ej: EN_COLA + EN_PROGRESO)
    List<Cita> findByEstadoIn(List<EstadoCita> estados);

    // Bandeja del veterinario: sus citas en estados activos
    List<Cita> findByIdVeterinarioAndEstadoIn(Long idVeterinario, List<EstadoCita> estados);

    // Historial por mascota
    List<Cita> findByIdMascota(Long idMascota);

    // Historial por cliente (dueño)
    List<Cita> findByIdCliente(Long idCliente);
}
