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

    private LocalDate fecha;

    // 🔥 NULL = bloqueo global (feriado)
    @ManyToOne
    private Barber barber;

    private String motivo;
}