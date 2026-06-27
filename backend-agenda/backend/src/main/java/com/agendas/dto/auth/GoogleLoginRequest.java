package com.agendas.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginRequest {

    @NotBlank(message = "El token de Google es obligatorio")
    private String token;

    private EmpresaRegistrationRequest empresa;
}
