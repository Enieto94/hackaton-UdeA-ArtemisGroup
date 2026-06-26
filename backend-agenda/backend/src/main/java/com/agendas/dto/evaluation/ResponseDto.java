package com.agendas.dto.evaluation;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ResponseDto {
    private UUID id;
    private Integer preguntaNumero;
    private Boolean respuesta;
    private BigDecimal pesoAplicado;
    private String evidencia;
    private List<RecommendationDto> recomendaciones;
}
