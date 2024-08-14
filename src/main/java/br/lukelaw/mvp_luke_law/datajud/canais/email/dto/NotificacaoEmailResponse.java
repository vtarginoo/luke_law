package br.lukelaw.mvp_luke_law.datajud.canais.email.dto;

import br.lukelaw.mvp_luke_law.datajud.dto.AnaliseDeMovimento;

public record NotificacaoEmailResponse(
        AnaliseDeMovimento analise,
        boolean emailEnviado
) {}
