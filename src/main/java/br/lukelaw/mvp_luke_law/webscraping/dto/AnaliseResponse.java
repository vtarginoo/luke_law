package br.lukelaw.mvp_luke_law.webscraping.dto;

public record AnaliseResponse(
        AnaliseDeMovimento analise,
        boolean emailEnviado
) {}
