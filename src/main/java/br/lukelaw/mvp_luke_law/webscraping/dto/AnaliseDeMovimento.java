package br.lukelaw.mvp_luke_law.webscraping.dto;


import br.lukelaw.mvp_luke_law.webscraping.entity.Movimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class AnaliseDeMovimento {

    private String numeroProcesso;
    private String tribunal;
    private String sistema;
    private Movimento ultimoMovimento;
    private long horasDesdeUltimoMovimento;
    private boolean movimentoRecente;

}

