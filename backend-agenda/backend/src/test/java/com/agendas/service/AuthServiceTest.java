package com.agendas.service;

import com.agendas.dto.auth.EmpresaRegistrationRequest;
import com.agendas.dto.auth.GoogleLoginRequest;
import com.agendas.entity.Empresa;
import com.agendas.entity.Role;
import com.agendas.entity.User;
import com.agendas.repository.EmpresaRepository;
import com.agendas.repository.UserRepository;
import com.agendas.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void googleLoginShouldCreateUserAndReturnTokenWhenGoogleTokenIsValid() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthService service = new AuthService(repo, empresaRepository, encoder, jwtService, restTemplateBuilder);
        ReflectionTestUtils.setField(service, "googleClientId", "client-id");
        EmpresaRegistrationRequest empresaRequest = new EmpresaRegistrationRequest();
        empresaRequest.setNombre("Empresa Demo");
        empresaRequest.setNit("900123456-7");
        empresaRequest.setSector("Tecnología");
        empresaRequest.setTamano("Pequeña");
        GoogleLoginRequest request = new GoogleLoginRequest("valid-token", empresaRequest);
        Empresa empresa = new Empresa();
        empresa.setNombre("Empresa Demo");
        empresa.setNit("900123456-7");
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        when(restTemplate.getForObject(anyString(), eq(Map.class), eq("valid-token"))).thenReturn(Map.of(
                "email", "google@example.com",
                "name", "Google User",
                "email_verified", true,
                "aud", "client-id"
        ));
        when(repo.findByEmail("google@example.com")).thenReturn(Optional.empty());
        when(empresaRepository.findByNit("900123456-7")).thenReturn(Optional.empty());
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);
        when(repo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        var response = service.googleLogin(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals(Role.EVALUADOR.name(), response.getRole());
        verify(repo).save(any(User.class));
    }
}
