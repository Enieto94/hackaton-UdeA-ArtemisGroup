package com.agendas.controller;

import com.agendas.dto.auth.*;
import com.agendas.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return service.login(req);
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest req) {
        service.register(req);
    }

    @PostMapping("/google")
    public AuthResponse google(@Valid @RequestBody GoogleLoginRequest req) {
        return service.googleLogin(req.getToken());
    }
}
