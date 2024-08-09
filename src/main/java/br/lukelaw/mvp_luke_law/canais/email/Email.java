package br.lukelaw.mvp_luke_law.canais.email;

public record Email(
        String to,
        String subject,
        String body
) {
}
