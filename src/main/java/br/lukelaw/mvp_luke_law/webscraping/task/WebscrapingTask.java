package br.lukelaw.mvp_luke_law.webscraping.task;

import br.lukelaw.mvp_luke_law.messaging.service.ConsultaAtivaService;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.webscraping.fontes.pje.PjeWebScrapingService;
import br.lukelaw.mvp_luke_law.xSimulateBD.BDSimulate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class WebscrapingTask {

    private static final Logger log = LoggerFactory.getLogger(WebscrapingTask.class);

    @Autowired
    private PjeWebScrapingService webScrapingService;

    @Autowired
    private BDSimulate bdSimulate;

    @Autowired
    private ConsultaAtivaService consultaAtivaService;


    @Scheduled(fixedRate = 1200)
    //@Scheduled(cron = "0 0 8-19 * * ?", zone = "America/Sao_Paulo")
    public void scrapingPJE() {
        try {

            log.info("Iniciando scraping e envio ao Kafka...");
            for (String processo : bdSimulate.processosAssociados.keySet()) {
                try {
                    Processo processoRaspado = webScrapingService.scrapePjeUltimoMov(processo);

                    if (processoRaspado == null) {
                        log.warn("Falha no scraping para o processo {}", processo);
                        continue;
                    }

                    consultaAtivaService.consumerConsultaAtiva(processoRaspado);

                    log.info("Processo {} Enviado Analise De Consulta", processoRaspado.getNumeroProcesso());

                } catch (Exception e) {
                    log.error("Erro ao realizar o scraping do processo {}", processo, e);
                }
            }

            log.info("Finalizando scraping e Analise.");
        } catch (Exception e) {
            log.error("Erro ao realizar o scraping ou enviar ao Kafka", e);
        }
    }

}