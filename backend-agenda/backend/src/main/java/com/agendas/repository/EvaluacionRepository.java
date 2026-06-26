package com.agendas.repository;

import com.agendas.entity.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, UUID> {
    List<Evaluacion> findByEmpresaIdOrderByCreatedAtDesc(UUID empresaId);
}
