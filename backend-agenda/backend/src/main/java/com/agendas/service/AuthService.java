package com.agendas.service;

import com.agendas.dto.auth.AuthResponse;
import com.agendas.dto.auth.EmpresaRegistrationRequest;
import com.agendas.dto.auth.GoogleLoginRequest;
import com.agendas.dto.auth.LoginRequest;
import com.agendas.dto.auth.MicrosoftLoginRequest;
import com.agendas.dto.auth.RegisterRequest;
import com.agendas.entity.Empresa;
import com.agendas.entity.Role;
import com.agendas.entity.User;
import com.agendas.exception.BusinessException;
import com.agendas.repository.EmpresaRepository;
import com.agendas.repository.UserRepository;
import com.agendas.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository repo;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${google.client-id:}")
    private String googleClientId;

    public AuthResponse login(LoginRequest req) {
        User user = repo.findByEmail(normalizeEmail(req.getEmail()))
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException("Credenciales inválidas");
        }

        return buildResponse(user);
    }

    public AuthResponse register(RegisterRequest req) {
        String email = normalizeEmail(req.getEmail());
        if (repo.findByEmail(email).isPresent()) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }

        User user = new User();
        user.setNombre(req.getNombre());
        user.setEmail(email);
        user.setRole(Role.EVALUADOR);
        user.setOauthProvider("traditional");
        user.setEmpresa(resolveEmpresa(req.getEmpresa()));
        user.setPassword(encoder.encode(req.getPassword()));

        return buildResponse(repo.save(user));
    }

    @SuppressWarnings("unchecked")
    public AuthResponse googleLogin(GoogleLoginRequest req) {
        String token = req.getToken();
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

        boolean emailVerified = Boolean.parseBoolean(String.valueOf(payload.get("email_verified")));
        String email = normalizeEmail((String) payload.get("email"));
        String aud = (String) payload.get("aud");
        String name = (String) payload.get("name");

        if (!emailVerified || email == null || aud == null || !aud.equals(googleClientId)) {
            throw new BusinessException("El token de Google no es válido para esta aplicación");
        }

        User user = repo.findByEmail(email)
                .orElseGet(() -> createOauthUser(email, name, "google", req.getEmpresa()));

        return buildResponse(user);
    }

    @SuppressWarnings("unchecked")
    public AuthResponse microsoftLogin(MicrosoftLoginRequest req) {
        String token = req.getToken();
        if (token == null || token.isBlank()) {
            throw new BusinessException("El token de Microsoft es obligatorio");
        }

        RestTemplate restTemplate = restTemplateBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        Map<String, Object> payload = restTemplate.exchange(
                "https://graph.microsoft.com/v1.0/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        ).getBody();

        if (payload == null) {
            throw new BusinessException("No se pudo validar el token de Microsoft");
        }

        String microsoftEmail = normalizeEmail((String) payload.get("mail"));
        if (microsoftEmail == null) {
            microsoftEmail = normalizeEmail((String) payload.get("userPrincipalName"));
        }
        String email = microsoftEmail;
        String name = (String) payload.getOrDefault("displayName", email);

        if (email == null) {
            throw new BusinessException("Microsoft no retornó un correo electrónico verificable");
        }

        User user = repo.findByEmail(email)
                .orElseGet(() -> createOauthUser(email, name, "microsoft", req.getEmpresa()));

        return buildResponse(user);
    }

    private User createOauthUser(String email, String name, String provider, EmpresaRegistrationRequest empresaRequest) {
        if (empresaRequest == null) {
            throw new BusinessException("Debe completar los datos de empresa para registrar este usuario");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setNombre(name == null || name.isBlank() ? email : name);
        newUser.setRole(Role.EVALUADOR);
        newUser.setOauthProvider(provider);
        newUser.setEmpresa(resolveEmpresa(empresaRequest));
        newUser.setPassword(encoder.encode(UUID.randomUUID().toString()));
        return repo.save(newUser);
    }

    private Empresa resolveEmpresa(EmpresaRegistrationRequest req) {
        if (req == null) {
            throw new BusinessException("Los datos de empresa son obligatorios");
        }

        String nit = req.getNit().trim();
        return empresaRepository.findByNit(nit)
                .orElseGet(() -> {
                    Empresa empresa = new Empresa();
                    empresa.setNombre(req.getNombre().trim());
                    empresa.setNit(nit);
                    empresa.setSector(trimToNull(req.getSector()));
                    empresa.setTamano(trimToNull(req.getTamano()));
                    return empresaRepository.save(empresa);
                });
    }

    private AuthResponse buildResponse(User user) {
        String token = jwtService.generateToken(user);
        UUID empresaId = user.getEmpresa() == null ? null : user.getEmpresa().getId();
        return new AuthResponse(token, user.getRole().name(), user.getEmail(), user.getNombre(), empresaId);
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return email.trim().toLowerCase();
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
