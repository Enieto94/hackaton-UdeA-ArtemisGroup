package com.agendas.dto.appointment;

import com.agendas.entity.Servicio;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {
    private Long barberId;
    private LocalDate fecha;
    private LocalTime hora;
    private Servicio servicio;
}