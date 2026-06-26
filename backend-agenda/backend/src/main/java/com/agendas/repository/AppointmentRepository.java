package com.agendas.repository;

import com.agendas.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByBarberIdAndFechaAndHora(Long barberId, LocalDate fecha, LocalTime hora);
    Optional<Appointment> findById(Long id);

    List<Appointment> findByBarberUserEmail(String email);

    List<Appointment> findByUserEmail(String email);

    @Query("""
    SELECT a.hora 
    FROM Appointment a 
    WHERE a.barber.id = :barberId 
    AND a.fecha = :fecha 
    AND a.estado <> 'CANCELADA'
""")
    List<LocalTime> findHorasOcupadas(Long barberId, LocalDate fecha);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Appointment a WHERE a.barber.id = :barberId AND a.fecha = :fecha")
    List<Appointment> lockAppointments(Long barberId, LocalDate fecha);

    List<Appointment> findByBarberId(Long barberId);


}