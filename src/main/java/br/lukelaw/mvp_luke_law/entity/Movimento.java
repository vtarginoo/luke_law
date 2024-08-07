package br.lukelaw.mvp_luke_law.entity;

import java.time.LocalDateTime;

public record Movimento(
        Long codigo,
        String nome,
        LocalDateTime dataHora
) {
}
