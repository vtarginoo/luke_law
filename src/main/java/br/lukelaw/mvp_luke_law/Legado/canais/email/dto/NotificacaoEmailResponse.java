package br.lukelaw.mvp_luke_law.Legado.canais.email.dto;


import br.lukelaw.mvp_luke_law.webscraping.dto.AnaliseDeMovimento;

public record NotificacaoEmailResponse(
        AnaliseDeMovimento analise,
        boolean emailEnviado
) {}
