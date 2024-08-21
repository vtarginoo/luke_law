package br.lukelaw.mvp_luke_law.messaging.dto;


import br.lukelaw.mvp_luke_law.webscraping.entity.Movimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class AnaliseDeMovimento {

    private Processo processo;
    private long horasDesdeUltimoMovimento;
    private boolean movimentoRecente;

}

