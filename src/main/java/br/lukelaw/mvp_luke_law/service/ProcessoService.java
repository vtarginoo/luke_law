package br.lukelaw.mvp_luke_law.service;




import br.lukelaw.mvp_luke_law.email.Email;
import br.lukelaw.mvp_luke_law.email.EmailService;
import br.lukelaw.mvp_luke_law.entity.Movimento;
import br.lukelaw.mvp_luke_law.entity.Processo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Service
public class ProcessoService {

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

    public Processo realizarRequisicao(String numeroProcesso) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", apiKey);

        HttpEntity<String> request = new HttpEntity<>(getBody(numeroProcesso), headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        // Usando o ObjectMapper configurado para desserializar a resposta JSON para um objeto Processo
        Processo processo = null;
        try {
            processo = objectMapper.readValue(response.getBody(), Processo.class);

        } catch (IOException e) {
            e.printStackTrace();
            // Trate a exceção conforme necessário
        }
        return processo;
    }

    public boolean analisarMovimentacao(Processo processo) {

        // Obter o último movimento
        Movimento ultimoMovimento = processo.getMovimentos().get(processo.getMovimentos().size() - 1);

        // Converter a data do último movimento para LocalDateTime
        LocalDateTime dataUltimoMovimento = ultimoMovimento.dataHora();

        // Obter a data e hora atual
        LocalDateTime agora = LocalDateTime.now();

        // Calcular a diferença em horas
        long horasDesdeUltimoMovimento = ChronoUnit.HOURS.between(dataUltimoMovimento, agora);

        System.out.println(horasDesdeUltimoMovimento);

        // Verificar se a diferença é menor que 24 horas  |||| Para conseguir testar vou aumentar em 1600 horas
        return horasDesdeUltimoMovimento < 1600;
    }





}







