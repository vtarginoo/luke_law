package br.lukelaw.mvp_luke_law.webscraping.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);



    public void iniciarKafka() {
        try {
            log.info("Iniciando Kafka...");
            Runtime.getRuntime().exec(getKafkaCommand("start-kafka"));
            Thread.sleep(10000); // Aguardar Kafka iniciar
        } catch (Exception e) {
            log.error("Erro ao iniciar o Kafka", e);
        }
    }


    public void pararKafka() {
        try {
            log.info("Parando Kafka...");
            Runtime.getRuntime().exec(getKafkaCommand("stop-kafka"));
        } catch (Exception e) {
            log.error("Erro ao parar o Kafka", e);
        }
    }

    private String getKafkaCommand(String command) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "cmd /c " + command + ".bat";
        } else {
            return "/usr/bin/" + command + ".sh";
        }
    }
}
