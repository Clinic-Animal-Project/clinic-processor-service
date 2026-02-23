package com.clinicanimal.processor.domain.repository;

import com.clinicanimal.processor.domain.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICitaRepository extends JpaRepository<Cita, Long> {
}
