package com.agendas.service;

import com.agendas.dto.evaluation.EvaluationAnswerRequest;
import com.agendas.dto.evaluation.EvaluationRequest;
import com.agendas.dto.evaluation.EvaluationResponse;
import com.agendas.dto.evaluation.RecommendationDto;
import com.agendas.dto.evaluation.ResponseDto;
import com.agendas.entity.EstadoEvaluacion;
import com.agendas.entity.Evaluacion;
import com.agendas.entity.Recomendacion;
import com.agendas.entity.Respuesta;
import com.agendas.entity.Role;
import com.agendas.entity.User;
import com.agendas.exception.BusinessException;
import com.agendas.repository.EvaluacionRepository;
import com.agendas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationService {

    private final EvaluacionRepository evaluacionRepository;
    private final UserRepository userRepository;
    private final DiagnosticCatalog catalog;

    public EvaluationResponse create(EvaluationRequest request, String email) {
        User user = currentUser(email);
        if (user.getEmpresa() == null) {
            throw new BusinessException("El usuario no está asociado a una empresa");
        }

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setEmpresa(user.getEmpresa());
        evaluacion.setUsuario(user);
        applyAnswers(evaluacion, request);
        return toResponse(evaluacionRepository.save(evaluacion));
    }

    public EvaluationResponse update(UUID id, EvaluationRequest request, String email) {
        User user = currentUser(email);
        Evaluacion evaluacion = loadAllowed(id, user);
        applyAnswers(evaluacion, request);
        return toResponse(evaluacionRepository.save(evaluacion));
    }

    @Transactional(readOnly = true)
    public List<EvaluationResponse> list(String email) {
        User user = currentUser(email);
        if (user.getRole() == Role.EVALUADOR && user.getEmpresa() == null) {
            throw new BusinessException("El usuario no está asociado a una empresa");
        }
        List<Evaluacion> evaluaciones = user.getRole() == Role.EVALUADOR
                ? evaluacionRepository.findByEmpresaIdOrderByCreatedAtDesc(user.getEmpresa().getId())
                : evaluacionRepository.findAll();

        return evaluaciones.stream()
                .sorted(Comparator.comparing(Evaluacion::getCreatedAt).reversed())
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EvaluationResponse get(UUID id, String email) {
        return toResponse(loadAllowed(id, currentUser(email)));
    }

    private void applyAnswers(Evaluacion evaluacion, EvaluationRequest request) {
        Map<Integer, EvaluationAnswerRequest> received = request.getRespuestas().stream()
                .collect(Collectors.toMap(EvaluationAnswerRequest::getPreguntaNumero, Function.identity(), (a, b) -> b));

        Map<Integer, Boolean> answers = new HashMap<>();
        received.forEach((number, answer) -> answers.put(number, answer.getRespuesta()));

        evaluacion.getRespuestas().clear();
        BigDecimal total = BigDecimal.ZERO;

        for (QuestionDefinition question : catalog.questions()) {
            EvaluationAnswerRequest incoming = received.get(question.number());
            Boolean answer = incoming == null ? null : incoming.getRespuesta();
            String evidence = incoming == null ? null : incoming.getEvidencia();

            if (isNotApplicable(question, answers)) {
                answer = null;
            }

            BigDecimal appliedWeight = Boolean.TRUE.equals(answer)
                    ? BigDecimal.valueOf(question.weight())
                    : BigDecimal.ZERO;
            total = total.add(appliedWeight);

            Respuesta response = new Respuesta();
            response.setEvaluacion(evaluacion);
            response.setPreguntaNumero(question.number());
            response.setRespuesta(answer);
            response.setPesoAplicado(appliedWeight);
            response.setEvidencia(trimToNull(evidence));

            if (Boolean.FALSE.equals(answer)) {
                response.getRecomendaciones().add(buildRecommendation(response, question));
            }

            evaluacion.getRespuestas().add(response);
        }

        evaluacion.setScoreTotal(total);
        evaluacion.setEstado(request.isCompletar() ? EstadoEvaluacion.COMPLETADA : EstadoEvaluacion.BORRADOR);
        evaluacion.setCompletedAt(request.isCompletar() ? Instant.now() : null);
    }

    private boolean isNotApplicable(QuestionDefinition question, Map<Integer, Boolean> answers) {
        if (question.parent() != null && !Boolean.TRUE.equals(answers.get(question.parent()))) {
            return true;
        }
        return question.dependsOn() != null && !Boolean.TRUE.equals(answers.get(question.dependsOn()));
    }

    private Recomendacion buildRecommendation(Respuesta response, QuestionDefinition question) {
        Recomendacion recommendation = new Recomendacion();
        recommendation.setRespuesta(response);
        recommendation.setPrioridad(priority(question));
        recommendation.setAccion(question.recommendation());
        recommendation.setFundamentoNormativo(question.article());
        return recommendation;
    }

    private String priority(QuestionDefinition question) {
        if (question.number() == 1 || question.weight() >= 12) {
            return "ALTA";
        }
        if (question.weight() >= 8) {
            return "MEDIA";
        }
        return "COMPLEMENTARIA";
    }

    private Evaluacion loadAllowed(UUID id, User user) {
        Evaluacion evaluacion = evaluacionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Evaluación no encontrada"));

        if (user.getRole() == Role.EVALUADOR
                && !evaluacion.getEmpresa().getId().equals(user.getEmpresa().getId())) {
            throw new BusinessException("No tiene permisos para acceder a esta evaluación");
        }

        return evaluacion;
    }

    private User currentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
    }

    private EvaluationResponse toResponse(Evaluacion evaluacion) {
        return EvaluationResponse.builder()
                .id(evaluacion.getId())
                .empresaId(evaluacion.getEmpresa().getId())
                .usuarioId(evaluacion.getUsuario().getId())
                .scoreTotal(evaluacion.getScoreTotal())
                .estado(evaluacion.getEstado().name())
                .createdAt(evaluacion.getCreatedAt())
                .completedAt(evaluacion.getCompletedAt())
                .respuestas(evaluacion.getRespuestas().stream()
                        .sorted(Comparator.comparing(Respuesta::getPreguntaNumero))
                        .map(this::toDto)
                        .toList())
                .build();
    }

    private ResponseDto toDto(Respuesta respuesta) {
        return ResponseDto.builder()
                .id(respuesta.getId())
                .preguntaNumero(respuesta.getPreguntaNumero())
                .respuesta(respuesta.getRespuesta())
                .pesoAplicado(respuesta.getPesoAplicado())
                .evidencia(respuesta.getEvidencia())
                .recomendaciones(respuesta.getRecomendaciones().stream()
                        .map(this::toDto)
                        .toList())
                .build();
    }

    private RecommendationDto toDto(Recomendacion recomendacion) {
        return RecommendationDto.builder()
                .id(recomendacion.getId())
                .prioridad(recomendacion.getPrioridad())
                .accion(recomendacion.getAccion())
                .fundamentoNormativo(recomendacion.getFundamentoNormativo())
                .build();
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
