package br.lukelaw.mvp_luke_law.email;

public record Email(
        String to,
        String subject,
        String body
) {
}
