package br.lukelaw.mvp_luke_law.canais.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NotificacaoEmailRequest(
        @NotBlank(message = "O destinatário é obrigatório")
        @Email(message = "O destinatário deve ser um e-mail válido")
        String destinatario,
        @NotBlank(message = "O número do processo é obrigatório")
        @Size(min = 20, max = 20, message = "O número do processo deve ter exatamente 20 dígitos")
        @Pattern(regexp = "^\\d{20}$", message = "O número do processo deve conter apenas números")
        String numProcesso
) {}
