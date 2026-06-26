package com.agendas.dto.appointment;

import com.agendas.entity.Servicio;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {

    @NotNull(message = "El barbero es obligatorio")
    private Long barberId;

    @FutureOrPresent(message = "La fecha no puede ser pasada")
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @NotNull(message = "El servicio es obligatorio")
    private Servicio servicio;
}
