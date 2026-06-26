package com.agendas.service;

import com.agendas.dto.auth.*;
import com.agendas.entity.Role;
import com.agendas.entity.User;
import com.agendas.repository.UserRepository;
import com.agendas.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${google.client-id:}")
    private String googleClientId;

    public AuthResponse login(LoginRequest req) {
        User user = repo.findByEmail(req.getEmail()).orElseThrow();

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user);
        String role = user.getRole().name();

        return new AuthResponse(token, role);
    }

    public void register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }

    public AuthResponse googleLogin(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("El token de Google es obligatorio");
        }

        if (googleClientId == null || googleClientId.isBlank()) {
            throw new IllegalStateException("Falta configurar google.client-id");
        }

        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token={token}";
        Map<String, Object> payload = restTemplate.getForObject(url, Map.class, token);

        if (payload == null) {
            throw new IllegalArgumentException("No se pudo validar el token de Google");
        }

        Boolean emailVerified = (Boolean) payload.get("email_verified");
        String email = (String) payload.get("email");
        String aud = (String) payload.get("aud");
        String name = (String) payload.get("name");

        if (emailVerified == null || !emailVerified || email == null || aud == null || !aud.equals(googleClientId)) {
            throw new IllegalArgumentException("El token de Google no es válido para esta aplicación");
        }

        User user = repo.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setNombre(name);
            newUser.setRole(Role.USER);
            newUser.setPassword(encoder.encode(UUID.randomUUID().toString()));
            return repo.save(newUser);
        });

        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt, user.getRole().name());
    }
}