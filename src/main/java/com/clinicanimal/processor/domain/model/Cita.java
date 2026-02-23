package com.clinicanimal.processor.domain.model;

import com.clinicanimal.processor.domain.model.estado.EstadoCita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cita")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")

    private Long id;
    @Column(name = "fecha_hora")

    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCita estado;

    // Ya no @ManyToOne — solo IDs que apuntan al master-service
    @Column(name = "id_area", nullable = false)
    private Long idArea;

    @Column(name = "id_mascota", nullable = false)
    private Long idMascota;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(name = "id_recepcionista", nullable = false)
    private Long idRecepcionista;

    @Column(name = "id_veterinario")
    private Long idVeterinario;

    // Estas sí se quedan como relaciones JPA — son entidades locales del processor
//    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, optional = true)
//    private Receta receta;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CitaServicio> citaServicios = new ArrayList<>();

}
