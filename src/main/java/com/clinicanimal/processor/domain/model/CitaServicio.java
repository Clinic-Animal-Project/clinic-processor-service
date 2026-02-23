package com.clinicanimal.processor.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cita_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita_serv")
    private Long id;

    private Integer cantidad;

    @Column(name = "precio_base")
    private double precioBase;

    @Column(name = "sub_total")
    private  double subTotal;

    @ManyToOne
    @JoinColumn(name="id_cita")
    private Cita cita;

    @Column(name = "id_servicio", nullable = false)
    private Long idServicio;    // solo ID — BD externa
}
