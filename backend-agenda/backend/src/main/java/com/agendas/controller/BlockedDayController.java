package com.agendas.controller;

import com.agendas.entity.BlockedDay;
import com.agendas.repository.BlockedDaysRepository;
import com.agendas.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/blocked-days")
@RequiredArgsConstructor
public class BlockedDayController {

    private final BlockedDaysRepository repo;
    private final BarberRepository barberRepo;

    // =========================================================
    // 👨‍💼 ADMIN → BLOQUEAR DÍA GLOBAL (FERIADO)
    // =========================================================
    @PostMapping("/global")
    @PreAuthorize("hasRole('ADMIN')")
    public void blockGlobal(@RequestParam String fecha,
                            @RequestParam String motivo) {

        BlockedDay b = new BlockedDay();
        b.setFecha(LocalDate.parse(fecha));
        b.setMotivo(motivo);

        repo.save(b);
    }

    // =========================================================
    // 👨‍💼 ADMIN → BLOQUEAR BARBERO
    // =========================================================
    @PostMapping("/barber")
    @PreAuthorize("hasRole('ADMIN')")
    public void blockBarber(@RequestParam Long barberId,
                            @RequestParam String fecha,
                            @RequestParam String motivo) {

        BlockedDay b = new BlockedDay();
        b.setFecha(LocalDate.parse(fecha));
        b.setMotivo(motivo);
        b.setBarber(barberRepo.findById(barberId).orElseThrow());

        repo.save(b);
    }

    // =========================================================
    // 👨‍💼 ADMIN → ELIMINAR BLOQUEO
    // =========================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}