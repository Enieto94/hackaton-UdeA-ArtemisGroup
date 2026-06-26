package com.agendas.repository;

import com.agendas.entity.BlockedDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BlockedDaysRepository extends JpaRepository<BlockedDay, Long> {

    Optional<BlockedDay> findByFechaAndBarberId(LocalDate fecha, Long barberId);

    Optional<BlockedDay> findByFechaAndBarberIsNull(LocalDate fecha);
}
