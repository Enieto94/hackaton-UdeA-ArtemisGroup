package com.agendas.controller;

import com.agendas.dto.appointment.AppointmentRequest;
import com.agendas.dto.appointment.AppointmentResponse;
import com.agendas.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public AppointmentResponse create(
            @Valid @RequestBody AppointmentRequest req,
            Authentication auth
    ) {
        return service.create(req, auth.getName());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public List<AppointmentResponse> myAppointments(Authentication auth) {
        return service.getByUser(auth.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppointmentResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/barber")
    @PreAuthorize("hasRole('BARBER')")
    public List<AppointmentResponse> getByBarber(Authentication auth) {
        return service.getByBarber(auth.getName());
    }

    @GetMapping("/availability")
    @PreAuthorize("hasAnyRole('USER','ADMIN','BARBER')")
    public List<String> getAvailability(
            @RequestParam Integer barberId,
            @RequestParam String fecha
    ) {
        return service.getAvailableSlots(Long.valueOf(barberId), LocalDate.parse(fecha))
                .stream()
                .map(Object::toString)
                .toList();
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('BARBER')")
    public void confirm(@PathVariable Long id, Authentication auth) {
        service.confirm(id, auth.getName());
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER','ADMIN','BARBER')")
    public void cancel(
            @PathVariable Long id,
            Authentication auth
    ) {

        String email = auth.getName();

        String role = auth.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority()
                .replace("ROLE_", "");

        service.cancel(id, email, role);
    }
}
