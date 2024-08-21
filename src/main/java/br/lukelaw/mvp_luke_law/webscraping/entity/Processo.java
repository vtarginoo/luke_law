package br.lukelaw.mvp_luke_law.webscraping.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class Processo {
    private String partesEnvolvidas;
    private String numeroProcesso;
    private String tribunal;
    private String sistema;
    private String grau;
    private List<Movimento> movimentos;
    private LocalDateTime dataHoraUltimaAtualizacao;

}
