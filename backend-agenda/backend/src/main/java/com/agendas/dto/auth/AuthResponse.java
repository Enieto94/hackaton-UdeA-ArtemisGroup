package com.agendas.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private String email;
    private String nombre;
    private UUID empresaId;

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
