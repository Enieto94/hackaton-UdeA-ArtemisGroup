package com.agendas.dto.evaluation;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RecommendationDto {
    private UUID id;
    private String prioridad;
    private String accion;
    private String fundamentoNormativo;
}
