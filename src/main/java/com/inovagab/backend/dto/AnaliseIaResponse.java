package com.inovagab.backend.dto;

public record AnaliseIaResponse(
        int pontuacao,
        String prioridade,
        String justificativa
) {
}
