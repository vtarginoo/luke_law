package br.lukelaw.mvp_luke_law.webscraping.task;

import br.lukelaw.mvp_luke_law.webscraping.service.MovimentoService;
import br.lukelaw.mvp_luke_law.webscraping.service.WebScrapingService;
import br.lukelaw.mvp_luke_law.webscraping.service.WhatsappService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@EnableScheduling
public class ProcessoTask {

    private static final Logger log = LoggerFactory.getLogger(ProcessoTask.class);

    @Autowired
    private WebScrapingService webScrapingService;

    @Autowired
    MovimentoService movimentoService;

    @Autowired
    WhatsappService wppService;

    @Scheduled(cron = "0 30 15-17 * * ?", zone = "America/Sao_Paulo")
    public void monitoramentoMovimentoDeProcessoWpp() throws JsonProcessingException {

        String[] processos = {"0838717-06.2024.8.19.0001", "0809129-51.2024.8.19.0001",
                "0947617-20.2023.8.19.0001", "0938160-61.2023.8.19.0001"};

        for (String processo : processos) {

            var requestProcesso = webScrapingService.scrapePjeUltimoMov(processo);

            if (requestProcesso == null) {
                log.warn("Processo {} foi pulado devido a falha no scraping.", processo);
                continue; // Pula para o próximo processo
            }

            var analiseDeMovimento = movimentoService.analisarMovimentacao(requestProcesso);

            String messageBody = "Prezado Cliente, segue as informações sobre a movimentação do processo "
                    + requestProcesso.getNumeroProcesso() +
                    "\nData e hora da movimentação: " + analiseDeMovimento.getUltimoMovimento().dataHora() +
                    "Tipo de Movimentação " + analiseDeMovimento.getUltimoMovimento().nome();

            if (analiseDeMovimento.isMovimentoRecente()) {
                wppService.notificacaoWhatsapp(messageBody);
            }
        }
    }
}
