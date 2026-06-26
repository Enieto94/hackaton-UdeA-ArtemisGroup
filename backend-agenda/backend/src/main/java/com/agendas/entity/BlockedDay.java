package com.agendas.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class BlockedDay {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    // null representa un bloqueo global.
    @ManyToOne
    private Barber barber;

    @Column(nullable = false)
    private String motivo;
}
