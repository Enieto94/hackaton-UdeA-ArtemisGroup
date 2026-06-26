package com.agendas.controller;

import com.agendas.dto.evaluation.EvaluationRequest;
import com.agendas.dto.evaluation.EvaluationResponse;
import com.agendas.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/evaluaciones")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService service;

    @GetMapping
    public List<EvaluationResponse> list(Authentication authentication) {
        return service.list(authentication.getName());
    }

    @GetMapping("/{id}")
    public EvaluationResponse get(@PathVariable UUID id, Authentication authentication) {
        return service.get(id, authentication.getName());
    }

    @PostMapping
    public EvaluationResponse create(@Valid @RequestBody EvaluationRequest request, Authentication authentication) {
        return service.create(request, authentication.getName());
    }

    @PutMapping("/{id}")
    public EvaluationResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody EvaluationRequest request,
            Authentication authentication
    ) {
        return service.update(id, request, authentication.getName());
    }
}
