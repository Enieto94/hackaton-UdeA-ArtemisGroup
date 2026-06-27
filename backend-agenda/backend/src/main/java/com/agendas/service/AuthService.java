package com.agendas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
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
    private final ObjectMapper objectMapper;

    @Value("${google.client-id:}")
    private String googleClientId;

    @Value("${google.client-secret:}")
    private String googleClientSecret;

    @Value("${google.redirect-uri:}")
    private String googleRedirectUri;

    @Value("${google.state-expiration-seconds:600}")
    private long googleStateExpirationSeconds;

    @Value("${app.frontend-auth-callback-url:http://localhost:9000/auth/callback}")
    private String frontendAuthCallbackUrl;

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
        String subject = trimToNull((String) payload.get("sub"));

        if (!emailVerified || email == null || aud == null || !aud.equals(googleClientId)) {
            throw new BusinessException("El token de Google no es válido para esta aplicación");
        }

        User user = findOrLinkOauthUser("google", subject, email)
                .orElseGet(() -> createOauthUser(email, name, "google", subject, req.getEmpresa()));

        return buildResponse(user);
    }

    public String createGoogleOAuthState(EmpresaRegistrationRequest empresaRequest) {
        GoogleOAuthState state = new GoogleOAuthState();
        state.nonce = UUID.randomUUID().toString();
        state.iat = Instant.now().getEpochSecond();
        state.empresa = empresaRequest;

        try {
            String json = objectMapper.writeValueAsString(state);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException ex) {
            throw new BusinessException("No se pudo iniciar OAuth con Google");
        }
    }

    public URI buildGoogleAuthorizationUri(String state) {
        validateGoogleAuthorizationConfig();

        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .queryParam("state", state)
                .build()
                .encode()
                .toUri();
    }

    @SuppressWarnings("unchecked")
    public AuthResponse completeGoogleAuthorizationCodeLogin(String code, String state, String expectedState) {
        validateGoogleAuthorizationConfig();

        if (code == null || code.isBlank()) {
            throw new BusinessException("Google no retornó código de autorización");
        }

        if (state == null || state.isBlank() || expectedState == null || expectedState.isBlank() || !state.equals(expectedState)) {
            throw new BusinessException("El state de OAuth no es válido");
        }

        GoogleOAuthState oauthState = readGoogleOAuthState(state);
        RestTemplate restTemplate = restTemplateBuilder.build();

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", googleClientId);
        body.add("client_secret", googleClientSecret);
        body.add("redirect_uri", googleRedirectUri);
        body.add("grant_type", "authorization_code");

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                new HttpEntity<>(body, tokenHeaders),
                Map.class
        );

        Map<String, Object> tokenPayload = tokenResponse.getBody();
        if (tokenPayload == null || tokenPayload.get("access_token") == null) {
            throw new BusinessException("Google no retornó un access token");
        }

        String accessToken = String.valueOf(tokenPayload.get("access_token"));
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);
        Map<String, Object> userInfo = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                new HttpEntity<>(userInfoHeaders),
                Map.class
        ).getBody();

        if (userInfo == null) {
            throw new BusinessException("Google no retornó datos del usuario");
        }

        boolean emailVerified = Boolean.parseBoolean(String.valueOf(userInfo.get("email_verified")));
        String email = normalizeEmail((String) userInfo.get("email"));
        String name = (String) userInfo.get("name");
        String subject = trimToNull((String) userInfo.get("sub"));

        if (!emailVerified || email == null || subject == null) {
            throw new BusinessException("Google no retornó un correo electrónico verificable");
        }

        User user = findOrLinkOauthUser("google", subject, email)
                .orElseGet(() -> createOauthUser(email, name, "google", subject, oauthState.empresa));

        return buildResponse(user);
    }

    public URI buildFrontendAuthCallbackUri(AuthResponse authResponse, String error) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(frontendAuthCallbackUrl);

        if (error != null && !error.isBlank()) {
            return builder.queryParam("error", error).build().encode().toUri();
        }

        return builder
                .queryParam("token", authResponse.getToken())
                .queryParam("role", authResponse.getRole())
                .queryParam("email", authResponse.getEmail())
                .queryParam("nombre", authResponse.getNombre())
                .queryParam("empresaId", authResponse.getEmpresaId())
                .build()
                .encode()
                .toUri();
    }

    public EmpresaRegistrationRequest decodeEmpresaParam(String encodedEmpresa) {
        if (encodedEmpresa == null || encodedEmpresa.isBlank()) {
            return null;
        }

        try {
            byte[] decoded = Base64.getUrlDecoder().decode(encodedEmpresa);
            return objectMapper.readValue(new String(decoded, StandardCharsets.UTF_8), EmpresaRegistrationRequest.class);
        } catch (IllegalArgumentException | JsonProcessingException ex) {
            throw new BusinessException("Los datos de empresa para OAuth no son válidos");
        }
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

        String subject = trimToNull((String) payload.get("id"));
        User user = findOrLinkOauthUser("microsoft", subject, email)
                .orElseGet(() -> createOauthUser(email, name, "microsoft", subject, req.getEmpresa()));

        return buildResponse(user);
    }

    private User createOauthUser(String email, String name, String provider, String providerId, EmpresaRegistrationRequest empresaRequest) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setNombre(name == null || name.isBlank() ? email : name);
        newUser.setRole(Role.EVALUADOR);
        newUser.setOauthProvider(provider);
        newUser.setOauthProviderId(providerId);
        if (empresaRequest != null) {
            newUser.setEmpresa(resolveEmpresa(empresaRequest));
        }
        newUser.setPassword(encoder.encode(UUID.randomUUID().toString()));
        return repo.save(newUser);
    }

    private Optional<User> findOrLinkOauthUser(String provider, String providerId, String email) {
        if (providerId != null) {
            Optional<User> userByProvider = repo.findByOauthProviderAndOauthProviderId(provider, providerId);
            if (userByProvider.isPresent()) {
                return userByProvider;
            }
        }

        Optional<User> userByEmail = repo.findByEmail(email);
        userByEmail.ifPresent(user -> {
            if (providerId != null && user.getOauthProviderId() == null) {
                user.setOauthProvider(provider);
                user.setOauthProviderId(providerId);
            }
        });

        return userByEmail;
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

    private void validateGoogleAuthorizationConfig() {
        if (googleClientId == null || googleClientId.isBlank()) {
            throw new BusinessException("Falta configurar google.client-id");
        }

        if (googleClientSecret == null || googleClientSecret.isBlank()) {
            throw new BusinessException("Falta configurar google.client-secret");
        }

        if (googleRedirectUri == null || googleRedirectUri.isBlank()) {
            throw new BusinessException("Falta configurar google.redirect-uri");
        }
    }

    private GoogleOAuthState readGoogleOAuthState(String state) {
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(state);
            GoogleOAuthState oauthState = objectMapper.readValue(new String(decoded, StandardCharsets.UTF_8), GoogleOAuthState.class);
            long ageSeconds = Instant.now().getEpochSecond() - oauthState.iat;

            if (oauthState.nonce == null || oauthState.nonce.isBlank() || ageSeconds < 0 || ageSeconds > googleStateExpirationSeconds) {
                throw new BusinessException("El state de OAuth expiró");
            }

            return oauthState;
        } catch (IllegalArgumentException | JsonProcessingException ex) {
            throw new BusinessException("El state de OAuth no es válido");
        }
    }

    private static class GoogleOAuthState {
        public String nonce;
        public long iat;
        public EmpresaRegistrationRequest empresa;
    }
}
