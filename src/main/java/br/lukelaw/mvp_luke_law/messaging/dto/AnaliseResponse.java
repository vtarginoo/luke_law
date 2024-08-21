package br.lukelaw.mvp_luke_law.messaging.dto;

public record AnaliseResponse(
        AnaliseDeMovimento analise,
        boolean emailEnviado
) {}
