package br.lukelaw.mvp_luke_law.datajud.canais.email;

public record Email(
        String to,
        String subject,
        String body
) {
}
