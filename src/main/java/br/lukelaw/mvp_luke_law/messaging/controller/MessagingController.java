package br.lukelaw.mvp_luke_law.messaging.controller;

import br.lukelaw.mvp_luke_law.messaging.dto.ConsultaPassivaResponse;
import br.lukelaw.mvp_luke_law.messaging.exception.BadRequestException;
import br.lukelaw.mvp_luke_law.messaging.exception.MessageSendFailureException;
import br.lukelaw.mvp_luke_law.messaging.service.ConsultaPassivaService;
import br.lukelaw.mvp_luke_law.messaging.service.WhatsappService;
import br.lukelaw.mvp_luke_law.messaging.dto.ConsultaPassivaRequest;
import br.lukelaw.mvp_luke_law.messaging.service.MovimentoService;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.webscraping.exception.ProcessNotFoundException;
import br.lukelaw.mvp_luke_law.webscraping.exception.ResponseError;
import br.lukelaw.mvp_luke_law.webscraping.exception.WebScrapingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/messaging")
@Tag(name = "MessagingController", description = "Endpoints para consulta e análise de processos via web scraping do PJE")
public class MessagingController {

    @Autowired
    private MovimentoService movimentoService;

    @Autowired
    ConsultaPassivaService consultaPassiva;

    @Autowired
    private WhatsappService wppService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.webscraping.url}")
    private String webScrapingApiUrl;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();




    @PostMapping("/consultaPassiva")
    public ResponseEntity<ConsultaPassivaResponse> rotaConsultaPassiva(@Valid @NotNull @RequestBody ConsultaPassivaRequest request) throws JsonProcessingException {

        String numProcesso = request.getNumProcesso();
        String advWpp = request.getCelular().trim();

        System.out.println("Received numProcesso: " + numProcesso);

        // Log correto indicando o início da requisição ao WebScrapingController
        System.out.println("Iniciando o WebScraping.");

        // Chama o WebScrapingController fazer a Raspagem de Dados
        ResponseEntity<Processo> response = restTemplate.postForEntity(
                webScrapingApiUrl, request, Processo.class);

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ProcessNotFoundException("Processo não encontrado para o número: "
                    + request.getNumProcesso(),advWpp);
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            ResponseError errorResponse = objectMapper.convertValue(response.getBody(), ResponseError.class);
            throw new BadRequestException(errorResponse,advWpp);
        } else if (response.getStatusCode() != HttpStatus.OK) {
            throw new WebScrapingException("Erro ao realizar web scraping para o número: "
                    + request.getNumProcesso(),advWpp);
        }

        Processo requestProcesso = response.getBody();
        System.out.println("Web scraping concluído com sucesso.");

        // Envia a consulta passiva em um thread separado
        executorService.submit(() -> {
            try {
                consultaPassiva.envioDeConsultaPassiva(requestProcesso,advWpp);
                System.out.println("Envio de Processo Realizado.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Envio da Mensagem Falhou");
                throw new MessageSendFailureException("Falha ao enviar a mensagem via WhatsApp para o processo: " + request.getNumProcesso());
            }});

        // Cria o ConsultaPassivaResponse
        ConsultaPassivaResponse consultaResponse = new ConsultaPassivaResponse(requestProcesso,
                "Ok", "Sending...", advWpp);

        // Retorna 202 Accepted imediatamente
        return ResponseEntity.accepted().body(consultaResponse);
    }
}
