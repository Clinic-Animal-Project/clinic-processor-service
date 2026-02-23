package com.clinicanimal.processor.domain.model;

import com.clinicanimal.processor.domain.model.audit.Auditable;
import com.clinicanimal.processor.domain.model.estado.DiaSemana;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;


@Entity
@Table(name = "horario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Horario extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long id;

    @Column(name = "personal_id", nullable = false)
    private Long personalId;

    @ElementCollection(targetClass = DiaSemana.class)
    @CollectionTable(name = "horario_dias", joinColumns = @JoinColumn(name = "id_horario"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dia")
    private List<DiaSemana> dias;

    @Column(name = "hora_entrada", nullable = false)
    private LocalTime horaEntrada;

    @Column(name = "hora_salida", nullable = false)
    private LocalTime horaSalida;
}
