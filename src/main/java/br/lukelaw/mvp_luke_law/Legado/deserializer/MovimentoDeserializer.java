package br.lukelaw.mvp_luke_law.Legado.deserializer;

import br.lukelaw.mvp_luke_law.Legado.entity.Movimento;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovimentoDeserializer extends JsonDeserializer<Movimento> {

    @Override
    public Movimento deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException{

        JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);

        Long codigo = rootNode.get("codigo").asLong();
        String nome = rootNode.get("nome").asText();

        // Parse the date and time from the JSON string
        String dataHoraStr = rootNode.get("dataHora").asText();

        LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, DateTimeFormatter.ISO_DATE_TIME);

        return new Movimento(codigo, nome, dataHora);
    }
}