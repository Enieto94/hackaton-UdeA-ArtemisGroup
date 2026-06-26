package com.agendas.controller;

import com.agendas.dto.auth.*;
import com.agendas.entity.User;
import com.agendas.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        return service.login(req);
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        service.register(user);
    }

    @PostMapping("/google")
    public AuthResponse google(@RequestBody GoogleLoginRequest req) {
        return service.googleLogin(req.getToken());
    }
}