package br.lukelaw.mvp_luke_law.deserializer;

import br.lukelaw.mvp_luke_law.entity.Movimento;
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
import java.util.ArrayList;
import java.util.List;

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


            // Extraindo os valores dos campos
            Long id = sourceNode.path("id").asLong();
            String tribunal = sourceNode.path("tribunal").asText();
            String numeroProcesso = sourceNode.path("numeroProcesso").asText();
            String grau = sourceNode.path("grau").asText();
            String nivelSigilo = sourceNode.path("nivelSigilo").asText();

            // Extraindo os movimentos (supondo que é uma lista de objetos JSON)
            List<Movimento> movimentos = new ArrayList<>();
            JsonNode movimentosNode = sourceNode.path("movimentos");

            //===== Serializar Movimentos
            MovimentoDeserializer movimentoDeserializer = new MovimentoDeserializer();
            if (movimentosNode.isArray()) {
                for (JsonNode movimentoNode : movimentosNode) {
                    JsonParser movimentoParser = movimentoNode.traverse(p.getCodec());
                    Movimento movimento = movimentoDeserializer.deserialize(movimentoParser, ctxt);
                    movimentos.add(movimento);
                }
            }

            OffsetDateTime dataAjuizamento = OffsetDateTime.parse(sourceNode.path("dataAjuizamento").asText(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime dataAjuizamentoLocal = dataAjuizamento.toLocalDateTime();
            OffsetDateTime dataHoraUltimaAtualizacao = OffsetDateTime.parse(sourceNode.path("dataHoraUltimaAtualizacao").asText(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime dataHoraUltimaAtualizacaoLocal = dataHoraUltimaAtualizacao.toLocalDateTime();

            // Extraindo o timestamp
            OffsetDateTime timestamp = OffsetDateTime.parse(sourceNode.path("@timestamp").asText(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime timestampLocal = timestamp.toLocalDateTime();

            // Criando o objeto Processo com o construtor atualizado
            return new Processo(id, tribunal, numeroProcesso, dataAjuizamentoLocal,
                    grau, nivelSigilo,movimentos, dataHoraUltimaAtualizacaoLocal,  timestampLocal);

        } else {
            throw new IOException("O nó 'hits' não contém elementos ou está vazio.");
        }
    }}