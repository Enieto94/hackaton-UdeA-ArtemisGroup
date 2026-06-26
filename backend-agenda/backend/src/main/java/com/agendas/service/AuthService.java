package com.agendas.service;

import com.agendas.dto.auth.AuthResponse;
import com.agendas.dto.auth.LoginRequest;
import com.agendas.dto.auth.RegisterRequest;
import com.agendas.entity.Role;
import com.agendas.entity.User;
import com.agendas.exception.BusinessException;
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
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user);
        String role = user.getRole().name();

        return new AuthResponse(token, role);
    }

    public void register(RegisterRequest req) {
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }

        User user = new User();
        user.setNombre(req.getNombre());
        user.setTelefono(req.getTelefono());
        user.setEmail(req.getEmail());
        user.setRole(req.getRole());
        user.setPassword(encoder.encode(req.getPassword()));

        repo.save(user);
    }

    @SuppressWarnings("unchecked")
    public AuthResponse googleLogin(String token) {
        if (token == null || token.isBlank()) {
            throw new BusinessException("El token de Google es obligatorio");
        }

        if (googleClientId == null || googleClientId.isBlank()) {
            throw new BusinessException("Falta configurar google.client-id");
        }

        RestTemplate restTemplate = restTemplateBuilder.build();
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token={token}";
        Map<String, Object> payload = restTemplate.getForObject(url, Map.class, token);

        if (payload == null) {
            throw new BusinessException("No se pudo validar el token de Google");
        }

        Boolean emailVerified = (Boolean) payload.get("email_verified");
        String email = (String) payload.get("email");
        String aud = (String) payload.get("aud");
        String name = (String) payload.get("name");

        if (emailVerified == null || !emailVerified || email == null || aud == null || !aud.equals(googleClientId)) {
            throw new BusinessException("El token de Google no es válido para esta aplicación");
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
