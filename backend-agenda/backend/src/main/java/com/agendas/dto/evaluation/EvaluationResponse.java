package com.agendas.dto.evaluation;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class EvaluationResponse {
    private UUID id;
    private UUID empresaId;
    private UUID usuarioId;
    private BigDecimal scoreTotal;
    private String estado;
    private Instant createdAt;
    private Instant completedAt;
    private List<ResponseDto> respuestas;
}
