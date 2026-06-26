package com.agendas.mapper;

import com.agendas.dto.appointment.AppointmentResponse;
import com.agendas.entity.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .usuario(a.getUser().getNombre())
                .barbero(a.getBarber().getUser().getNombre())
                .fecha(a.getFecha())
                .hora(a.getHora())
                .servicio(a.getServicio())
                .estado(a.getEstado())
                .build();
    }
}