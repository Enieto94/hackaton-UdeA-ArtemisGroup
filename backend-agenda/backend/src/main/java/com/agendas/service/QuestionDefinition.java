package com.agendas.service;

public record QuestionDefinition(
        int number,
        String block,
        String article,
        int weight,
        Integer parent,
        Integer dependsOn,
        String text,
        String evidence,
        String recommendation
) {
    boolean isComplementary() {
        return weight == 0 && dependsOn != null;
    }
}
