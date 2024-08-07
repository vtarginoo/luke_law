package br.lukelaw.mvp_luke_law.service;




import br.lukelaw.mvp_luke_law.entity.Processo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ProcessoService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessoService.class);
    private String apiUrl = "https://api-publica.datajud.cnj.jus.br/api_publica_tjrj/_search";
    private String apiKey = "APIKey cDZHYzlZa0JadVREZDJCendQbXY6SkJlTzNjLV9TRENyQk1RdnFKZGRQdw==";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String getBody(String numeroProcesso) {
        // Construir o corpo da requisição com o número do processo
        return "{\"query\": {\"match\": {\"numeroProcesso\": \"" + numeroProcesso + "\"}}}";
    }

    public ResponseEntity<String> realizarRequisicao(String numeroProcesso) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", apiKey);

        HttpEntity<String> request = new HttpEntity<>(getBody(numeroProcesso), headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        processarJson(response.getBody());

        return response;
    }

    public void processarJson(String json) {
        try {
            Processo processo = objectMapper.readValue(json, Processo.class);
            System.out.println(processo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}