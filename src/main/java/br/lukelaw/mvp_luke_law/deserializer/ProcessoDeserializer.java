package br.lukelaw.mvp_luke_law.deserializer;

import br.lukelaw.mvp_luke_law.entity.Processo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class ProcessoDeserializer extends JsonDeserializer<Processo> {

    @Override
    public Processo deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode rootNode = p.getCodec().readTree(p);
        JsonNode hitsNode = rootNode.path("hits");
        JsonNode hitsArrayNode = hitsNode.path("hits");

        if (hitsArrayNode.isArray() && hitsArrayNode.size() > 0) {
            JsonNode sourceNode = hitsArrayNode.get(0).path("_source");
            if (sourceNode.isMissingNode()) {
                throw new IOException("O nó '_source' está ausente no JSON.");
            }

            // Adiciona log para verificação
            System.out.println("Source Node: " + sourceNode.toPrettyString());
            System.out.println("Até aqui estava tudo certo!");

            // Extraindo os valores dos campos
            Long id = sourceNode.path("id").asLong();
            String tribunal = sourceNode.path("tribunal").asText();
            String numeroProcesso = sourceNode.path("numeroProcesso").asText();

            // Utilizando OffsetDateTime para tratar o formato com fuso horário
            OffsetDateTime dataAjuizamento = OffsetDateTime.parse(sourceNode.path("dataAjuizamento").asText(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime dataAjuizamentoLocal = dataAjuizamento.toLocalDateTime();

            String grau = sourceNode.path("grau").asText();
            String nivelSigilo = sourceNode.path("nivelSigilo").asText();

            OffsetDateTime dataHoraUltimaAtualizacao = OffsetDateTime.parse(sourceNode.path("dataHoraUltimaAtualizacao").asText(), DateTimeFormatter.ISO_DATE.ISO_DATE_TIME);
            LocalDateTime dataHoraUltimaAtualizacaoLocal = dataHoraUltimaAtualizacao.toLocalDateTime();

            // Criando o objeto Processo com o construtor
            return new Processo(id, tribunal, numeroProcesso, dataAjuizamentoLocal, grau, nivelSigilo, dataHoraUltimaAtualizacaoLocal);


        } else {
            throw new IOException("O nó 'hits' não contém elementos ou está vazio.");
        }
    }
}