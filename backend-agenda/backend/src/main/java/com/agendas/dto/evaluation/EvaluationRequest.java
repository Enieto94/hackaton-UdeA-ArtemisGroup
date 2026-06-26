package com.agendas.dto.evaluation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class EvaluationRequest {

    @Valid
    @NotEmpty(message = "Debe enviar al menos una respuesta")
    private List<EvaluationAnswerRequest> respuestas;

    private boolean completar;
}
