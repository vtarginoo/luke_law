package br.lukelaw.mvp_luke_law.messaging.controller;

import br.lukelaw.mvp_luke_law.messaging.service.WhatsappService;
import br.lukelaw.mvp_luke_law.messaging.dto.AnaliseRequest;
import br.lukelaw.mvp_luke_law.messaging.dto.AnaliseResponse;
import br.lukelaw.mvp_luke_law.webscraping.service.MovimentoService;
import br.lukelaw.mvp_luke_law.webscraping.service.WebScrapingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/messaging")
@Tag(name = "MessagingController", description = "Endpoints para consulta e análise de processos via web scraping do PJE")
public class MessagingController {

    @Autowired
    private WebScrapingService webScrapingService;

    @Autowired
    private MovimentoService movimentoService;

    @Autowired
    private WhatsappService wppService;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    // Recebe um NumProcesso e Envia um Whatsapp
    @PostMapping("/consultaPassiva")
    public ResponseEntity<String> rotaConsultaPassiva(@Valid @NotNull @RequestBody AnaliseRequest request) throws JsonProcessingException {

        System.out.println("Received numProcesso: " + request.getNumProcesso());
        System.out.println("Iniciando o WebScraping.");

        // Envia a consulta passiva em um thread separado
        executorService.submit(() -> {
            try {
                // Executa o web scraping de forma síncrona
                var requestProcesso = webScrapingService.scrapePjeUltimoMov(request.getNumProcesso());
                System.out.println("Web scraping concluído.");
                wppService.envioDeConsultaPassiva(requestProcesso);
                System.out.println("Envio de Processo Realizado.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Retorna 202 Accepted imediatamente
        return ResponseEntity.accepted().body("Requisição aceita. Informações serão enviadas via WhatsApp.");
    }


    // Recebe um processo, Faz a Analise dele e Devolve o Ultimo Movimento
    @PostMapping("/consultaAnalisePassiva")
    public ResponseEntity<AnaliseResponse> rotaAnalisePassiva
    (@Valid @NotNull @RequestBody AnaliseRequest request) throws JsonProcessingException {

        System.out.println("Received numProcesso: " + request.getNumProcesso());

        var requestProcesso = webScrapingService.scrapePjeUltimoMov(request.getNumProcesso());

        System.out.println("Web scraping concluído.");

        System.out.println("Iniciando a análise da movimentação...");
        var analiseProcesso = movimentoService.analisarMovimentacao(requestProcesso);

        System.out.println("Análise da movimentação concluída.");


        if (analiseProcesso.isMovimentoRecente()) {

            System.out.println("Movimento recente detectado.");

            return ResponseEntity.ok(new AnaliseResponse(analiseProcesso, true));
        }

        System.out.println("Nenhum movimento recente detectado.");

        return ResponseEntity.ok(new AnaliseResponse(analiseProcesso, false));
    }

}

