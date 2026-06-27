package com.agendas.service;

import com.agendas.dto.chat.ChatRequest.EmpresaContext;
import com.agendas.dto.chat.ChatRequest;
import com.agendas.dto.chat.ChatResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiagnosticChatService {

    private final DiagnosticCatalog catalog;
    private final RestTemplateBuilder restTemplateBuilder;
    private final ObjectMapper objectMapper;
    private String legalContext = "";
    private String projectContext = "";

    @Value("${gemini.api-url:https://generativelanguage.googleapis.com/v1beta/interactions}")
    private String geminiApiUrl;

    @Value("${gemini.api-key:}")
    private String geminiApiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String geminiModel;

    @PostConstruct
    void loadKnowledgeBase() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        StringBuilder builder = new StringBuilder();

        Resource projectResource = resolver.getResource("classpath:knowledge-base/cavaltec-autodiagnostico-requisitos.md");
        if (projectResource.exists()) {
            projectContext = projectResource.getContentAsString(StandardCharsets.UTF_8);
        }

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

        Optional<ChatResponse> geminiResponse = requestGemini(request, question, answer, evidence, relevantLegalContext);
        if (geminiResponse.isPresent()) {
            return geminiResponse.get();
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
    private Optional<ChatResponse> requestGemini(
            ChatRequest request,
            QuestionDefinition question,
            String answer,
            String evidence,
            String relevantLegalContext
    ) {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            return Optional.empty();
        }

        String userPrompt = """
                Eres un asistente experto en auditoría normativa, Ley 1581 de 2012 de Colombia y privacidad desde el diseño.
                Objetivo del producto: %s
                Reglas:
                - Responde en español, con lenguaje claro para empresas no expertas en derecho.
                - Usa la información básica de la empresa cuando sea relevante.
                - Si el usuario pide explicar una pregunta, usa la herramienta explicar_pregunta.
                - Si el usuario pide evidencia o cumplimiento, usa la herramienta verificar_evidencia.
                - Si el usuario pide recomendaciones, plan o cierre de brechas, usa la herramienta recomendar_acciones_brecha.
                - No inventes obligaciones no soportadas por el contexto normativo entregado.
                - Entrega respuestas breves y accionables.

                Empresa: %s
                Estado del diagnóstico: puntaje %s, brechas actuales %s
                Pregunta activa: P%d - %s
                Bloque: %s
                Respuesta actual: %s
                Evidencia registrada: %s
                Fundamento: %s
                Contexto normativo: %s
                Mensaje del usuario: %s
                """.formatted(
                summarizeProjectGoal(),
                summarizeCompany(request.getEmpresa()),
                request.getPuntaje() == null ? "sin calcular" : request.getPuntaje() + "%",
                summarizeGaps(request),
                question.number(),
                question.text(),
                question.block(),
                answer,
                evidence,
                question.article(),
                relevantLegalContext,
                request.getMensaje()
        );

        Map<String, Object> firstPayload = Map.of(
                "model", geminiModel,
                "store", false,
                "input", List.of(userInputStep(userPrompt)),
                "tools", geminiTools(),
                "generation_config", Map.of("temperature", 0.2)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", geminiApiKey);

        try {
            Map<String, Object> firstResponse = restTemplateBuilder.build()
                    .postForObject(geminiApiUrl, new HttpEntity<>(firstPayload, headers), Map.class);

            List<Map<String, Object>> functionCalls = extractFunctionCalls(firstResponse);
            if (!functionCalls.isEmpty()) {
                List<Map<String, Object>> inputHistory = new ArrayList<>();
                inputHistory.add(userInputStep(userPrompt));
                inputHistory.addAll(extractSteps(firstResponse));
                functionCalls.stream()
                        .map(functionCall -> executeGeminiTool(
                                functionCall,
                                request,
                                question,
                                answer,
                                evidence,
                                relevantLegalContext
                        ))
                        .forEach(inputHistory::add);

                Map<String, Object> secondPayload = Map.of(
                        "model", geminiModel,
                        "store", false,
                        "input", inputHistory,
                        "tools", geminiTools(),
                        "generation_config", Map.of("temperature", 0.2)
                );

                Map<String, Object> secondResponse = restTemplateBuilder.build()
                        .postForObject(geminiApiUrl, new HttpEntity<>(secondPayload, headers), Map.class);
                String finalText = extractGeminiText(secondResponse);
                return finalText == null || finalText.isBlank()
                        ? Optional.empty()
                        : Optional.of(new ChatResponse(finalText.trim()));
            }

            String directText = extractGeminiText(firstResponse);
            return directText == null || directText.isBlank()
                    ? Optional.empty()
                    : Optional.of(new ChatResponse(directText.trim()));
        } catch (RestClientException ex) {
            log.warn("No se pudo obtener una respuesta util de Gemini; se usara fallback local: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private Map<String, Object> userInputStep(String prompt) {
        return Map.of(
                "type", "user_input",
                "content", List.of(Map.of("type", "text", "text", prompt))
        );
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractSteps(Map<String, Object> response) {
        if (response == null) {
            return List.of();
        }
        Object steps = response.get("steps");
        if (steps instanceof List<?> list) {
            return list.stream()
                    .filter(Map.class::isInstance)
                    .map(item -> (Map<String, Object>) item)
                    .toList();
        }
        return List.of();
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractFunctionCalls(Map<String, Object> response) {
        return extractSteps(response).stream()
                .filter(step -> "function_call".equals(step.get("type")))
                .toList();
    }

    @SuppressWarnings("unchecked")
    private String extractGeminiText(Map<String, Object> response) {
        if (response == null) {
            return null;
        }

        Object outputText = response.get("output_text");
        if (outputText == null) {
            outputText = response.get("outputText");
        }
        if (outputText instanceof String text) {
            return text;
        }

        StringBuilder builder = new StringBuilder();
        for (Map<String, Object> step : extractSteps(response)) {
            Object text = step.get("text");
            if (text instanceof String value) {
                builder.append(value).append("\n");
            }

            Object content = step.get("content");
            if (content instanceof String value) {
                builder.append(value).append("\n");
            }
            if (content instanceof List<?> parts) {
                for (Object part : parts) {
                    if (part instanceof Map<?, ?> partMap && partMap.get("text") instanceof String value) {
                        builder.append(value).append("\n");
                    }
                }
            }
        }
        return builder.toString().trim();
    }

    private List<Map<String, Object>> geminiTools() {
        return List.of(
                geminiFunction(
                        "explicar_pregunta",
                        "Explica el contexto legal y práctico de la pregunta seleccionada.",
                        Map.of(
                                "pregunta_id", Map.of("type", "string", "description", "Identificador de pregunta. Ejemplo: P2")
                        ),
                        List.of("pregunta_id")
                ),
                geminiFunction(
                        "verificar_evidencia",
                        "Indica qué documentos, URLs, registros o controles sirven como evidencia verificable para la pregunta.",
                        Map.of(
                                "pregunta_id", Map.of("type", "string", "description", "Identificador de pregunta. Ejemplo: P2"),
                                "tipo_evidencia", Map.of("type", "string", "description", "Tipo de evidencia a revisar, si el usuario lo menciona.")
                        ),
                        List.of("pregunta_id")
                ),
                geminiFunction(
                        "recomendar_acciones_brecha",
                        "Genera acciones priorizadas para cerrar brechas de cumplimiento detectadas.",
                        Map.of(
                                "pregunta_id", Map.of("type", "string", "description", "Identificador de pregunta principal, si aplica. Ejemplo: P9")
                        ),
                        List.of()
                )
        );
    }

    private Map<String, Object> geminiFunction(
            String name,
            String description,
            Map<String, Object> properties,
            List<String> required
    ) {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("type", "object");
        parameters.put("properties", properties);
        if (!required.isEmpty()) {
            parameters.put("required", required);
        }

        return Map.of(
                "type", "function",
                "name", name,
                "description", description,
                "parameters", parameters
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> executeGeminiTool(
            Map<String, Object> functionCall,
            ChatRequest request,
            QuestionDefinition selectedQuestion,
            String answer,
            String evidence,
            String relevantLegalContext
    ) {
        String name = String.valueOf(functionCall.get("name"));
        Object arguments = functionCall.get("arguments");
        Map<String, Object> args = arguments instanceof Map<?, ?> map ? (Map<String, Object>) map : Map.of();
        QuestionDefinition question = resolveQuestion(args.get("pregunta_id"), selectedQuestion);

        Map<String, Object> toolResult = switch (name) {
            case "explicar_pregunta" -> explicarPregunta(question, request, answer, evidence, relevantLegalContext);
            case "verificar_evidencia" -> verificarEvidencia(question, request, answer, evidence, args);
            case "recomendar_acciones_brecha" -> recomendarAccionesBrecha(question, request, answer, evidence);
            default -> Map.of("error", "Herramienta no soportada: " + name);
        };

        Map<String, Object> result = new HashMap<>();
        result.put("type", "function_result");
        result.put("name", name);
        Object id = functionCall.get("id");
        if (id != null) {
            result.put("call_id", id);
        }
        result.put("result", List.of(Map.of("type", "text", "text", toJson(toolResult))));
        return result;
    }

    private String toJson(Map<String, Object> value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            return "{\"error\":\"No se pudo serializar el resultado de la herramienta.\"}";
        }
    }

    private Map<String, Object> explicarPregunta(
            QuestionDefinition question,
            ChatRequest request,
            String answer,
            String evidence,
            String relevantLegalContext
    ) {
        return Map.of(
                "pregunta", questionSummary(question),
                "empresa", summarizeCompany(request.getEmpresa()),
                "respuesta_actual", answer,
                "evidencia_actual", evidence,
                "criterio_practico", "La empresa debe poder demostrar con evidencia verificable que el control existe, está vigente y es accesible para quien corresponda.",
                "fundamento", question.article(),
                "contexto_normativo", relevantLegalContext
        );
    }

    private Map<String, Object> verificarEvidencia(
            QuestionDefinition question,
            ChatRequest request,
            String answer,
            String evidence,
            Map<String, Object> args
    ) {
        return Map.of(
                "pregunta", questionSummary(question),
                "empresa", summarizeCompany(request.getEmpresa()),
                "respuesta_actual", answer,
                "evidencia_registrada", evidence,
                "evidencia_esperada", question.evidence(),
                "tipo_solicitado", String.valueOf(args.getOrDefault("tipo_evidencia", "no especificado")),
                "criterios_verificacion", List.of(
                        "Debe identificar responsable, fecha o versión cuando aplique.",
                        "Debe ser accesible para auditoría o para titulares si la obligación es pública.",
                        "Debe estar alineada con la finalidad, derechos, canales y medidas exigidas por la Ley 1581."
                ),
                "fundamento", question.article()
        );
    }

    private Map<String, Object> recomendarAccionesBrecha(
            QuestionDefinition question,
            ChatRequest request,
            String answer,
            String evidence
    ) {
        List<Map<String, Object>> gaps = request.getBrechas() == null || request.getBrechas().isEmpty()
                ? List.of(gapSummary(question))
                : questionsFromIds(request.getBrechas()).stream()
                        .map(this::gapSummary)
                        .toList();

        return Map.of(
                "empresa", summarizeCompany(request.getEmpresa()),
                "puntaje", request.getPuntaje() == null ? "sin calcular" : request.getPuntaje() + "%",
                "pregunta_activa", questionSummary(question),
                "respuesta_actual", answer,
                "evidencia_actual", evidence,
                "brechas", gaps,
                "instruccion", "Priorice por peso, impacto legal y facilidad de implementación. Proponga responsables, próximos pasos y evidencias de cierre."
        );
    }

    private QuestionDefinition resolveQuestion(Object preguntaId, QuestionDefinition fallback) {
        if (preguntaId instanceof Number number) {
            return catalog.get(number.intValue());
        }
        if (preguntaId instanceof String text) {
            String digits = text.replaceAll("\\D", "");
            if (!digits.isBlank()) {
                try {
                    return catalog.get(Integer.parseInt(digits));
                } catch (IllegalArgumentException ignored) {
                    return fallback;
                }
            }
        }
        return fallback;
    }

    private String summarizeProjectGoal() {
        if (projectContext.isBlank()) {
            return "Autodiagnóstico multiempresa de cumplimiento de Ley 1581 en fase de diseño.";
        }
        String normalized = projectContext.replaceAll("\\s+", " ").trim();
        return normalized.substring(0, Math.min(normalized.length(), 900));
    }

    private String summarizeCompany(EmpresaContext empresa) {
        if (empresa == null) {
            return "Sin datos de empresa registrados.";
        }
        return "Nombre: %s; NIT: %s; sector: %s; tamaño: %s.".formatted(
                blankToDefault(empresa.getNombre(), "sin nombre"),
                blankToDefault(empresa.getNit(), "sin NIT"),
                blankToDefault(empresa.getSector(), "sin sector"),
                blankToDefault(empresa.getTamano(), "sin tamaño")
        );
    }

    private String summarizeGaps(ChatRequest request) {
        if (request.getBrechas() == null || request.getBrechas().isEmpty()) {
            return "sin brechas registradas";
        }
        return questionsFromIds(request.getBrechas()).stream()
                .map(question -> "P" + question.number() + " " + question.block())
                .toList()
                .toString();
    }

    private List<QuestionDefinition> questionsFromIds(List<Integer> ids) {
        return ids.stream()
                .map(this::findQuestion)
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<QuestionDefinition> findQuestion(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(catalog.get(id));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    private Map<String, Object> questionSummary(QuestionDefinition question) {
        return Map.of(
                "numero", "P" + question.number(),
                "bloque", question.block(),
                "texto", question.text(),
                "peso", question.weight(),
                "fundamento", question.article(),
                "evidencia_esperada", question.evidence(),
                "recomendacion_base", question.recommendation()
        );
    }

    private Map<String, Object> gapSummary(QuestionDefinition question) {
        return Map.of(
                "numero", "P" + question.number(),
                "bloque", question.block(),
                "peso", question.weight(),
                "prioridad", priority(question),
                "brecha", question.text(),
                "accion_base", question.recommendation(),
                "evidencia_de_cierre", question.evidence(),
                "fundamento", question.article()
        );
    }

    private String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
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
