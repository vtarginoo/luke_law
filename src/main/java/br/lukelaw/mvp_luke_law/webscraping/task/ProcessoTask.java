package br.lukelaw.mvp_luke_law.webscraping.task;

import br.lukelaw.mvp_luke_law.datajud.canais.whatsapp.WhatsappService;
import br.lukelaw.mvp_luke_law.webscraping.service.MovimentoService;
import br.lukelaw.mvp_luke_law.webscraping.service.WebScrapingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class ProcessoTask {

    @Autowired
    private WebScrapingService webScrapingService;

    @Autowired
    MovimentoService movimentoService;

    @Autowired
    WhatsappService wppService;

    @Scheduled(cron ="0 0 8-12 * * ?" ) // 5 minutos em milissegundos
    public void monitoramentoMovimentoDeProcessoWpp () throws JsonProcessingException {

        String[] processos = {"09077874720238190001", "09476172020238190001", "01016022920238190000",
                "08091295120248190001"};

        for (String processo: processos) {

            var requestProcesso = webScrapingService.scrapePjeUltimoMov(processo);
            var analiseDeMovimento = movimentoService.analisarMovimentacao(requestProcesso);

            String messageBody = "Prezado Cliente, segue as informações sobre a movimentação do processo "
                    + requestProcesso.getNumeroProcesso() +
                    "\nData e hora da movimentação: " + analiseDeMovimento.getUltimoMovimento().dataHora() +
                    "Tipo de Movimentação " + analiseDeMovimento.getUltimoMovimento().nome();

            if(analiseDeMovimento.isMovimentoRecente()) {
                wppService.notificacaoWhatsapp(messageBody);
            }
        }
    }
}
