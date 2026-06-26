package com.agendas.service;

import com.agendas.dto.appointment.AppointmentRequest;
import com.agendas.dto.appointment.AppointmentResponse;
import com.agendas.entity.Appointment;
import com.agendas.entity.Barber;
import com.agendas.entity.Estado;
import com.agendas.entity.User;
import com.agendas.exception.BusinessException;
import com.agendas.mapper.AppointmentMapper;
import com.agendas.repository.AppointmentRepository;
import com.agendas.repository.BarberRepository;
import com.agendas.repository.BlockedDaysRepository;
import com.agendas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    // 🟢 DEPENDENCIAS
    private final AppointmentRepository repo;
    private final UserRepository userRepo;
    private final BarberRepository barberRepo;
    private final AppointmentMapper mapper;
    private final BlockedDaysRepository blockedDayRepo;

    // =========================================================
    // 🔥 CREAR CITA (CONCURRENCIA PRO)
    // =========================================================
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public AppointmentResponse create(AppointmentRequest req, String email) {
        // 🔥 VALIDAR BLOQUEO GLOBAL
        blockedDayRepo.findByFechaAndBarberIsNull(req.getFecha())
                .ifPresent(b -> {
                    throw new BusinessException("Día no disponible (feriado)");
                });

        // 🔥 VALIDAR BLOQUEO DEL BARBERO
        blockedDayRepo.findByFechaAndBarberId(req.getFecha(), req.getBarberId())
                .ifPresent(b -> {
                    throw new BusinessException("El barbero no está disponible ese día");
                });
        // 🔒 1. BLOQUEO PESIMISTA
        repo.lockAppointments(req.getBarberId(), req.getFecha());

        // 2. Buscar usuario y barbero
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        Barber barber = barberRepo.findById(req.getBarberId())
                .orElseThrow(() -> new BusinessException("Barbero no encontrado"));

        // 3. Validar disponibilidad
        boolean ocupado = repo.existsByBarberIdAndFechaAndHora(
                barber.getId(), req.getFecha(), req.getHora()
        );

        if (ocupado) {
            throw new BusinessException("Horario no disponible");
        }

        try {
            // 4. Crear cita
            Appointment a = new Appointment();
            a.setUser(user);
            a.setBarber(barber);
            a.setFecha(req.getFecha());
            a.setHora(req.getHora());
            a.setServicio(req.getServicio());
            a.setEstado(Estado.PENDIENTE);

            // 🔥 VALIDACIÓN INMEDIATA EN DB
            repo.saveAndFlush(a);

            return mapper.toResponse(a);

        } catch (DataIntegrityViolationException e) {

            log.warn("Conflicto de reserva: barber={}, fecha={}, hora={}",
                    barber.getId(), req.getFecha(), req.getHora());

            throw new BusinessException("Ese horario acaba de ser reservado por otro usuario");
        }
    }


    // =========================================================
    // 👨‍💼 ADMIN → VER TODAS LAS CITAS
    // =========================================================
    public List<AppointmentResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // =========================================================
    // 💈 BARBERO → VER SUS CITAS
    // =========================================================
    public List<AppointmentResponse> getByBarber(String email) {
        return repo.findByBarberUserEmail(email)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // =========================================================
    // 👤 USUARIO → VER SUS CITAS
    // =========================================================
    public List<AppointmentResponse> getByUser(String email) {
        return repo.findByUserEmail(email)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // =========================================================
    // ⏰ DISPONIBILIDAD DINÁMICA
    // =========================================================
    public List<LocalTime> getAvailableSlots(Long barberId, LocalDate fecha) {
        // 🔥 BLOQUEO GLOBAL
        if (blockedDayRepo.findByFechaAndBarberIsNull(fecha).isPresent()) {
            return List.of();
        }

        // 🔥 BLOQUEO BARBERO
        if (blockedDayRepo.findByFechaAndBarberId(fecha, barberId).isPresent()) {
            return List.of();
        }
        // ❌ Validar fecha pasada
        if (fecha.isBefore(LocalDate.now())) {
            throw new BusinessException("No puedes consultar fechas pasadas");
        }

        List<LocalTime> ocupadas = repo.findHorasOcupadas(barberId, fecha);

        List<LocalTime> disponibles = new ArrayList<>();

        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fin = LocalTime.of(20, 0);

        LocalTime now = LocalTime.now();

        while (inicio.isBefore(fin)) {

            boolean esPasado = fecha.equals(LocalDate.now()) && inicio.isBefore(now);

            if (!ocupadas.contains(inicio) && !esPasado) {
                disponibles.add(inicio);
            }

            inicio = inicio.plusHours(1);
        }

        return disponibles;
    }

    @Transactional
    public void cancel(Long appointmentId, String email, String role) {

        Appointment appointment = repo.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("Cita no encontrada"));

        // 🔐 VALIDACIÓN POR ROL

        // 👤 Usuario solo puede cancelar sus citas
        if (role.equals("USER") && !appointment.getUser().getEmail().equals(email)) {
            throw new BusinessException("No puedes cancelar esta cita");
        }

        // 💈 Barbero solo puede cancelar sus citas
        if (role.equals("BARBER") && !appointment.getBarber().getUser().getEmail().equals(email)) {
            throw new BusinessException("No puedes cancelar esta cita");
        }

        // ❌ No cancelar si ya está cancelada
        if (appointment.getEstado() == Estado.CANCELADA) {
            throw new BusinessException("La cita ya está cancelada");
        }

        // 🔥 CAMBIO DE ESTADO
        appointment.setEstado(Estado.CANCELADA);

        repo.save(appointment);
    }


    @Transactional
    public void confirm(Long appointmentId, String email) {

        Appointment appointment = repo.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("Cita no encontrada"));

        // 🔐 Validar que el barbero es dueño de la cita
        if (!appointment.getBarber().getUser().getEmail().equals(email)) {
            throw new BusinessException("No puedes confirmar esta cita");
        }

        if (appointment.getEstado() != Estado.PENDIENTE) {
            throw new BusinessException("Solo citas pendientes pueden confirmarse");
        }

        appointment.setEstado(Estado.CONFIRMADA);

        repo.save(appointment);

    }

    @Transactional
    public void reject(Long appointmentId, String email) {

        Appointment appointment = repo.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("Cita no encontrada"));

        if (!appointment.getBarber().getUser().getEmail().equals(email)) {
            throw new BusinessException("No puedes rechazar esta cita");
        }

        if (appointment.getEstado() != Estado.PENDIENTE) {
            throw new BusinessException("Solo citas pendientes pueden rechazarse");
        }

        appointment.setEstado(Estado.RECHAZADA);

        repo.save(appointment);
    }
}