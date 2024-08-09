package br.lukelaw.mvp_luke_law.canais.email.dto;

import br.lukelaw.mvp_luke_law.dto.AnaliseDeMovimento;

public record NotificacaoEmailResponse(
        AnaliseDeMovimento analise,
        boolean emailEnviado
) {}
