package com.agendas.service;

import com.agendas.dto.chat.ChatRequest;
import com.agendas.dto.chat.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiagnosticChatService {

    private final DiagnosticCatalog catalog;
    private final RestTemplateBuilder restTemplateBuilder;
    private String legalContext = "";

    @Value("${llm.api-url:}")
    private String llmApiUrl;

    @Value("${llm.api-key:}")
    private String llmApiKey;

    @PostConstruct
    void loadKnowledgeBase() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        StringBuilder builder = new StringBuilder();

        for (Resource resource : resolver.getResources("classpath:knowledge-base/ley-1581/*.md")) {
            builder.append(resource.getContentAsString(StandardCharsets.UTF_8)).append("\n\n");
        }

        legalContext = builder.toString();
    }

    public ChatResponse answer(ChatRequest request) {
        QuestionDefinition question = catalog.get(request.getPreguntaNumero());
        String prompt = request.getMensaje().toLowerCase(Locale.ROOT);
        String answer = request.getRespuesta() == null
                ? "sin respuesta registrada"
                : Boolean.TRUE.equals(request.getRespuesta()) ? "Sí" : "No";
        String evidence = request.getEvidencia() == null || request.getEvidencia().isBlank()
                ? "No se ha registrado evidencia."
                : request.getEvidencia().trim();

        String relevantLegalContext = extractContext(question.article());

        ChatResponse llmResponse = requestLlm(request, question, answer, evidence, relevantLegalContext);
        if (llmResponse != null) {
            return llmResponse;
        }

        if (prompt.contains("evidencia") || prompt.contains("cumplimiento")) {
            return new ChatResponse("Para P" + question.number() + " responda Sí solo si puede mostrar evidencia verificable. "
                    + "Evidencia esperada: " + question.evidence() + " Base normativa: " + question.article()
                    + ". Evidencia actual: " + evidence);
        }

        if (prompt.contains("brecha") || prompt.contains("recom")) {
            if (Boolean.TRUE.equals(request.getRespuesta())) {
                return new ChatResponse("P" + question.number() + " no aparece como brecha con la respuesta actual. "
                        + "Mantenga evidencia vigente y trazable: " + question.evidence());
            }
            return new ChatResponse("Brecha P" + question.number() + ": " + question.recommendation()
                    + " Prioridad sugerida por peso e impacto: " + priority(question)
                    + ". Fundamento: " + question.article() + ".");
        }

        if (prompt.contains("ley") || prompt.contains("art")) {
            return new ChatResponse("La P" + question.number() + " se apoya en " + question.article()
                    + ". Contexto normativo cargado: " + relevantLegalContext);
        }

        return new ChatResponse("P" + question.number() + " (" + question.block() + "): " + question.text()
                + " Respuesta actual: " + answer + ". Criterio práctico: valide que exista evidencia como "
                + question.evidence() + " Fundamento: " + question.article() + ".");
    }

    @SuppressWarnings("unchecked")
    private ChatResponse requestLlm(
            ChatRequest request,
            QuestionDefinition question,
            String answer,
            String evidence,
            String relevantLegalContext
    ) {
        if (llmApiUrl == null || llmApiUrl.isBlank()) {
            return null;
        }

        String systemPrompt = "Eres un asistente experto en Ley 1581 de 2012 para autodiagnóstico privacy by design. "
                + "Responde en español, con recomendaciones concretas y sin inventar obligaciones no soportadas.";
        String userPrompt = """
                Pregunta activa: P%d - %s
                Bloque: %s
                Respuesta actual: %s
                Evidencia registrada: %s
                Fundamento: %s
                Contexto normativo: %s
                Mensaje del usuario: %s
                """.formatted(
                question.number(),
                question.text(),
                question.block(),
                answer,
                evidence,
                question.article(),
                relevantLegalContext,
                request.getMensaje()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (llmApiKey != null && !llmApiKey.isBlank()) {
            headers.setBearerAuth(llmApiKey);
        }

        Map<String, Object> payload = Map.of(
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        try {
            Map<String, Object> response = restTemplateBuilder.build()
                    .postForObject(llmApiUrl, new HttpEntity<>(payload, headers), Map.class);
            String content = extractLlmContent(response);
            return content == null || content.isBlank() ? null : new ChatResponse(content.trim());
        } catch (RestClientException ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private String extractLlmContent(Map<String, Object> response) {
        if (response == null) {
            return null;
        }
        Object direct = response.get("respuesta");
        if (direct == null) {
            direct = response.get("content");
        }
        if (direct instanceof String text) {
            return text;
        }

        Object choices = response.get("choices");
        if (choices instanceof List<?> list && !list.isEmpty() && list.getFirst() instanceof Map<?, ?> first) {
            Object message = first.get("message");
            if (message instanceof Map<?, ?> messageMap && messageMap.get("content") instanceof String text) {
                return text;
            }
        }
        return null;
    }

    private String extractContext(String article) {
        if (legalContext.isBlank()) {
            return article;
        }

        String normalized = article.toLowerCase(Locale.ROOT);
        int index = legalContext.toLowerCase(Locale.ROOT).indexOf(normalized.split(" ")[0]);
        String excerpt = index >= 0
                ? legalContext.substring(index, Math.min(legalContext.length(), index + 360))
                : legalContext.substring(0, Math.min(legalContext.length(), 360));
        return excerpt.replaceAll("\\s+", " ").trim();
    }

    private String priority(QuestionDefinition question) {
        if (question.number() == 1 || question.weight() >= 12) {
            return "alta";
        }
        if (question.weight() >= 8) {
            return "media";
        }
        return "complementaria";
    }
}
