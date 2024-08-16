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

    @GetMapping("/{numProcesso}")
    public Processo consultarUltimaMovimentacao(
            @PathVariable @Valid
            @NotBlank(message = "O número do processo é obrigatório")
            String numProcesso) {

        return webScrapingService.scrapePjeUltimoMov(numProcesso);
    }

    @PostMapping("/consultaAnalise")
    public ResponseEntity<AnaliseResponse> rotaAnalisePassiva
            (@Valid @NotNull @RequestBody AnaliseRequest request) throws JsonProcessingException {

        System.out.println("Received numProcesso: " + request.getNumProcesso());

        var requestProcesso = webScrapingService.scrapePjeUltimoMov(request.getNumProcesso());
        var analiseProcesso = movimentoService.analisarMovimentacao(requestProcesso);

        if (analiseProcesso.isMovimentoRecente()) {

            return ResponseEntity.ok(new AnaliseResponse(analiseProcesso, true));
        }
        return ResponseEntity.ok(new AnaliseResponse(analiseProcesso, false));
    }

    @PostMapping("/AnaliseWpp")
    public ResponseEntity<AnaliseResponse> rotaAnaliseWppPassiva
            (@Valid @NotNull @RequestBody AnaliseRequest wppRequest) throws JsonProcessingException {

        var requestProcesso = webScrapingService.scrapePjeUltimoMov(wppRequest.getNumProcesso());
        var analiseProcesso = movimentoService.analisarMovimentacao(requestProcesso);

        String messageBody = "Prezado Cliente, segue as informações sobre a movimentação do processo "
                + requestProcesso.getNumeroProcesso() +
                "\nData e hora da movimentação: " + analiseProcesso.getUltimoMovimento().dataHora() +
                "Tipo de Movimentação " + analiseProcesso.getUltimoMovimento().nome();

        if (analiseProcesso.isMovimentoRecente()) {
            wppService.notificacaoWhatsapp(messageBody);

            return ResponseEntity.ok(new AnaliseResponse(analiseProcesso, true));
        }

        return ResponseEntity.ok(new AnaliseResponse(analiseProcesso, false));

    }

}



