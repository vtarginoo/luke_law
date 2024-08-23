package br.lukelaw.mvp_luke_law.messaging.dto;

import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;

public record ConsultaPassivaResponse(
        Processo processo,
        String processoStatus,
        String statusMensagem,
        String numeroEnviado
) {
}