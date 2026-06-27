package com.agendas.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MicrosoftLoginRequest {

    @NotBlank(message = "El token de Microsoft es obligatorio")
    private String token;

    private EmpresaRegistrationRequest empresa;
}
