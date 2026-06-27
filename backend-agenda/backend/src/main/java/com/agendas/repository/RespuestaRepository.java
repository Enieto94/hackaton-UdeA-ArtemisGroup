package com.agendas.repository;

import com.agendas.entity.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RespuestaRepository extends JpaRepository<Respuesta, UUID> {
    List<Respuesta> findByEvaluacionId(UUID evaluacionId);

    Optional<Respuesta> findByEvaluacionIdAndPreguntaNumero(UUID evaluacionId, Integer preguntaNumero);
}
