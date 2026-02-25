package com.clinicanimal.processor.domain.repository;

import com.clinicanimal.processor.domain.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByPersonalId(Long personalId);

    List<Horario> findByActive(boolean active);
}
