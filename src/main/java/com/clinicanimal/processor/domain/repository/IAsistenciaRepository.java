package com.clinicanimal.processor.domain.repository;

import com.clinicanimal.processor.domain.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IAsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByHorarioId(Long horarioId);

    List<Asistencia> findByFecha(LocalDate fecha);

    boolean existsByHorarioIdAndFecha(Long horarioId, LocalDate fecha);
}
