package com.agendas.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmpresaRegistrationRequest {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombre;

    @NotBlank(message = "El NIT es obligatorio")
    private String nit;

    private String sector;

    private String tamano;
}
