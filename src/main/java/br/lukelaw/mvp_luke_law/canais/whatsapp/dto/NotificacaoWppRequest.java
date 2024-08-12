package br.lukelaw.mvp_luke_law.canais.whatsapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NotificacaoWppRequest(
        @NotBlank(message = "O número do processo é obrigatório")
        @Size(min = 20, max = 20, message = "O número do processo deve ter exatamente 20 dígitos")
        @Pattern(regexp = "^\\d{20}$", message = "O número do processo deve conter apenas números")
        String numProcesso) {}
