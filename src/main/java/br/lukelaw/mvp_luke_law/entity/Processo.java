package br.lukelaw.mvp_luke_law.entity;

import br.lukelaw.mvp_luke_law.deserializer.ProcessoDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class Processo {
    private Long id;
    private String tribunal;
    private String numeroProcesso;
    private LocalDateTime dataAjuizamento;
    private String grau;
    private String nivelSigilo;
    private List<Movimento> movimentos;
    private LocalDateTime dataHoraUltimaAtualizacao;
    @JsonProperty("@timestamp")
    private LocalDateTime timestamp;

}

