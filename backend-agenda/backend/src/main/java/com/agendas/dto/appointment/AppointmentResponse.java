package com.agendas.dto.appointment;

import com.agendas.entity.Barber;
import com.agendas.entity.Estado;
import com.agendas.entity.Servicio;
import com.agendas.entity.User;
import org.springframework.transaction.annotation.Isolation;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentResponse {
    private Long id;
    private String usuario;
    private String barbero;
    private LocalDate fecha;
    private LocalTime hora;
    private Servicio servicio;
    private Estado estado;

}

