package br.lukelaw.mvp_luke_law.webscraping.task;

import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.webscraping.service.WebScrapingService;
import br.lukelaw.mvp_luke_law.xSimulateBD.BDSimulate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class WebscrapingTask {

    private static final Logger log = LoggerFactory.getLogger(WebscrapingTask.class);

    @Autowired
    private WebScrapingService webScrapingService;

    @Autowired
    private KafkaTemplate<String, Processo> kafkaTemplate;

    @Autowired
    private BDSimulate bdSimulate;

    ///////
    @Scheduled(initialDelay = 120000)
    //@Scheduled(cron = "0 0 8-19 * * ?", zone = "America/Sao_Paulo")
    public void scrapingPJE() {
        try {
            iniciarKafka();

            log.info("Iniciando scraping e envio ao Kafka...");
            for (String processo : bdSimulate.processosAssociados.keySet()) {
                try {
                    Processo processoRaspado = webScrapingService.scrapePjeUltimoMov(processo);

                    if (processoRaspado == null) {
                        log.warn("Falha no scraping para o processo {}", processo);
                        continue;
                    }

                    kafkaTemplate.send("processos", processoRaspado);
                    log.info("Processo {} publicado no tópico Kafka", processoRaspado.getNumeroProcesso());

                } catch (Exception e) {
                    log.error("Erro ao realizar o scraping do processo {}", processo, e);
                }
            }

            log.info("Finalizando scraping e envio ao Kafka.");
        } catch (Exception e) {
            log.error("Erro ao realizar o scraping ou enviar ao Kafka", e);
        } finally {
            pararKafka();
        }
    }

    private void iniciarKafka() {
        try {
            log.info("Iniciando Kafka...");
            Runtime.getRuntime().exec("cmd /c start-kafka.bat");
            Thread.sleep(10000); // Aguardar Kafka iniciar
        } catch (Exception e) {
            log.error("Erro ao iniciar o Kafka", e);
        }
    }

    private void pararKafka() {
        try {
            log.info("Parando Kafka...");
            Runtime.getRuntime().exec("cmd /c stop-kafka.bat");
        } catch (Exception e) {
            log.error("Erro ao parar o Kafka", e);
        }
    }
}

