package com.agendas.dto.evaluation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EvaluationAnswerRequest {

    @NotNull(message = "El número de pregunta es obligatorio")
    @Min(value = 1, message = "La pregunta mínima es 1")
    @Max(value = 11, message = "La pregunta máxima es 11")
    private Integer preguntaNumero;

    private Boolean respuesta;

    private String evidencia;
}
