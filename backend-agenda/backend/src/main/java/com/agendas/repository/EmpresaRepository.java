package com.agendas.repository;

import com.agendas.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    Optional<Empresa> findByNit(String nit);
}
