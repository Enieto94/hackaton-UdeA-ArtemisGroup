package com.agendas.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"barber_id", "fecha", "hora"})
})
public class Appointment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Barber barber;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Servicio servicio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;
}
