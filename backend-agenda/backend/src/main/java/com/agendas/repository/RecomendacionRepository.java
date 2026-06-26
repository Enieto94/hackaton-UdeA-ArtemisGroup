package com.agendas.repository;

import com.agendas.entity.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, UUID> {
}
