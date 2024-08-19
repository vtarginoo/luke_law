package br.lukelaw.mvp_luke_law.webscraping.controller;

import br.lukelaw.mvp_luke_law.webscraping.dto.AnaliseRequest;
import br.lukelaw.mvp_luke_law.webscraping.dto.AnaliseResponse;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.webscraping.service.MovimentoService;
import br.lukelaw.mvp_luke_law.webscraping.service.WebScrapingService;
import br.lukelaw.mvp_luke_law.webscraping.service.WhatsappService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pje")
@Tag(name = "PjeController", description = "Endpoints para consulta e análise de processos via PJE")
public class PjeController {

    @Autowired
    private WebScrapingService webScrapingService;

    @Autowired
    private MovimentoService movimentoService;

    @Autowired
    private WhatsappService wppService;


    // Recebe um processo, Faz a Analise dele e Devolve o Ultimo Movimento
    @PostMapping("/consultaPassiva")
    public ResponseEntity<Processo> rotaConsultaPassiva
    (@Valid @NotNull @RequestBody AnaliseRequest request) throws JsonProcessingException {

        System.out.println("Received numProcesso: " + request.getNumProcesso());
        var requestProcesso = webScrapingService.scrapePjeUltimoMov(request.getNumProcesso());
        System.out.println("Web scraping concluído.");

        return ResponseEntity.ok(requestProcesso);
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



