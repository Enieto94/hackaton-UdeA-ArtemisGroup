package com.agendas.controller;

import com.agendas.dto.auth.*;
import com.agendas.exception.BusinessException;
import com.agendas.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String GOOGLE_STATE_COOKIE = "cavaltec_google_oauth_state";

    private final AuthService service;

    @Value("${google.state-cookie-secure:false}")
    private boolean googleStateCookieSecure;

    @Value("${google.state-expiration-seconds:600}")
    private long googleStateExpirationSeconds;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return service.login(req);
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        return service.register(req);
    }

    @PostMapping("/google")
    public AuthResponse google(@Valid @RequestBody GoogleLoginRequest req) {
        return service.googleLogin(req);
    }

    @GetMapping("/google")
    public ResponseEntity<Void> googleRedirect(@RequestParam(required = false) String empresa) {
        String state = service.createGoogleOAuthState(service.decodeEmpresaParam(empresa));
        URI googleUri = service.buildGoogleAuthorizationUri(state);
        ResponseCookie stateCookie = ResponseCookie.from(GOOGLE_STATE_COOKIE, state)
                .httpOnly(true)
                .secure(googleStateCookieSecure)
                .sameSite("Lax")
                .path("/auth/google")
                .maxAge(Duration.ofSeconds(googleStateExpirationSeconds))
                .build();

        return ResponseEntity.status(302)
                .header(HttpHeaders.SET_COOKIE, stateCookie.toString())
                .location(googleUri)
                .build();
    }

    @GetMapping("/google/callback")
    public ResponseEntity<Void> googleCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            @CookieValue(value = GOOGLE_STATE_COOKIE, required = false) String expectedState,
            HttpServletResponse response
    ) {
        URI frontendUri;

        try {
            if (error != null && !error.isBlank()) {
                throw new BusinessException("Google canceló o rechazó el inicio de sesión");
            }

            AuthResponse authResponse = service.completeGoogleAuthorizationCodeLogin(code, state, expectedState);
            frontendUri = service.buildFrontendAuthCallbackUri(authResponse, null);
        } catch (BusinessException ex) {
            frontendUri = service.buildFrontendAuthCallbackUri(null, ex.getMessage());
        } catch (Exception ex) {
            frontendUri = service.buildFrontendAuthCallbackUri(null, "No se pudo completar el inicio de sesión con Google");
        }

        ResponseCookie clearStateCookie = ResponseCookie.from(GOOGLE_STATE_COOKIE, "")
                .httpOnly(true)
                .secure(googleStateCookieSecure)
                .sameSite("Lax")
                .path("/auth/google")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearStateCookie.toString());

        return ResponseEntity.status(302)
                .location(frontendUri)
                .build();
    }

    @PostMapping("/microsoft")
    public AuthResponse microsoft(@Valid @RequestBody MicrosoftLoginRequest req) {
        return service.microsoftLogin(req);
    }
}
