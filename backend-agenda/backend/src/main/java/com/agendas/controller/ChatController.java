package com.agendas.controller;

import com.agendas.dto.chat.ChatRequest;
import com.agendas.dto.chat.ChatResponse;
import com.agendas.service.DiagnosticChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final DiagnosticChatService service;

    @PostMapping("/diagnostico")
    public ChatResponse answer(@Valid @RequestBody ChatRequest request) {
        return service.answer(request);
    }
}
