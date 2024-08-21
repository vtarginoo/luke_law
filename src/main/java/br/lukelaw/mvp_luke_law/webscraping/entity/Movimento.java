package br.lukelaw.mvp_luke_law.webscraping.entity;

import lombok.Getter;

import java.time.LocalDateTime;


public record Movimento(
        Long ordem,
        String nome,
        LocalDateTime dataHora
) {
}

