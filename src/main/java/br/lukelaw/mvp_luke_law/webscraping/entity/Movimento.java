package br.lukelaw.mvp_luke_law.webscraping.entity;

import java.time.LocalDateTime;

// Quando se Tratar do último movimento usaremos um código 999
public record Movimento(
        Long ordem,
        String nome,
        LocalDateTime dataHora
) {
}

