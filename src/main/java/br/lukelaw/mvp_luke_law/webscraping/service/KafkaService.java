package br.lukelaw.mvp_luke_law.webscraping.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Service
public class KafkaService {

    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);


    public boolean iniciarKafka() {
        try {
            log.info("Iniciando Kafka...");
            String startCommand = getKafkaCommand("start-kafka");
            Runtime.getRuntime().exec(startCommand);
            Thread.sleep(10000); // Aguardar Kafka iniciar

            // Verificação básica se o Kafka está escutando na porta padrão (9092)
            boolean isKafkaReady = false;
            int maxRetries = 5;
            int retryCount = 0;

            while (!isKafkaReady && retryCount < maxRetries) {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress("localhost", 9092), 2000);
                    isKafkaReady = true;
                    log.info("Kafka está pronto e escutando na porta 9092.");
                } catch (IOException e) {
                    log.warn("Kafka ainda não está pronto, aguardando...");
                    retryCount++;
                    try {
                        Thread.sleep(5000); // Aguarda 5 segundos antes de tentar novamente
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("Thread interrompida", ie);
                    }
                }
            }

            if (!isKafkaReady) {
                log.error("Kafka não está pronto após várias tentativas, abortando...");
                return false; // Retorna falso para indicar que o Kafka não está pronto
            }

        } catch (Exception e) {
            log.error("Erro ao iniciar o Kafka", e);
            return false;
        }

        return true; // Retorna verdadeiro se tudo ocorreu bem
    }

    public void pararKafka() {
        try {
            log.info("Parando Kafka...");
            String stopCommand = getKafkaCommand("stop-kafka");
            Runtime.getRuntime().exec(stopCommand);
        } catch (Exception e) {
            log.error("Erro ao parar o Kafka", e);
        }
    }

    private String getKafkaCommand(String command) {
        String kafkaHome = System.getenv("KAFKA_HOME");
        if (kafkaHome == null || kafkaHome.isEmpty()) {
            log.error("A variável de ambiente KAFKA_HOME não está definida.");
            return command;
        }

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "cmd /c " + kafkaHome + "\\bin\\" + command + ".bat";
        } else {
            return kafkaHome + "/bin/" + command + ".sh";
        }
    }
}