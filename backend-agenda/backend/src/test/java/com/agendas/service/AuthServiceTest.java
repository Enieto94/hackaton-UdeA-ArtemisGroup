package com.agendas.service;

import com.agendas.entity.Role;
import com.agendas.entity.User;
import com.agendas.repository.UserRepository;
import com.agendas.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private JwtService jwtService;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthService service;

    @Test
    void googleLoginShouldCreateUserAndReturnTokenWhenGoogleTokenIsValid() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        ReflectionTestUtils.setField(service, "encoder", encoder);
        ReflectionTestUtils.setField(service, "googleClientId", "client-id");
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        when(restTemplate.getForObject(anyString(), any(Class.class), any())).thenReturn(Map.of(
                "email", "google@example.com",
                "name", "Google User",
                "email_verified", true,
                "aud", "client-id"
        ));
        when(repo.findByEmail("google@example.com")).thenReturn(Optional.empty());
        when(repo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        var response = service.googleLogin("valid-token");

        assertEquals("jwt-token", response.getToken());
        verify(repo).save(any(User.class));
    }
}
