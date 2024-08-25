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

    private static final String START_KAFKA_SCRIPT = "/app/start-kafka.sh";
    private static final String STOP_KAFKA_SCRIPT = "/app/stop-kafka.sh";
    private static final int KAFKA_PORT = 9092;
    private static final int MAX_RETRIES = 3;  // Aumentado para 3 tentativas
    private static final int RETRY_DELAY_MS = 15000;  // Aumentado para 15 segundos

    public boolean iniciarKafka() {
        try {
            log.info("Iniciando Kafka...");

            // Executa o script de inicialização do Kafka
            Process startProcess = Runtime.getRuntime().exec(START_KAFKA_SCRIPT);
            startProcess.waitFor();  // Aguarda o script finalizar
            log.info("Script de inicialização do Kafka executado.");

            // Aguardar alguns segundos para o Kafka iniciar
            Thread.sleep(30000);

            // Verificação se o Kafka está escutando na porta padrão (9092)
            if (isKafkaReady()) {
                log.info("Kafka está pronto e escutando na porta " + KAFKA_PORT + ".");
                return true;
            } else {
                log.error("Kafka não está pronto após várias tentativas, abortando...");
                return false;
            }

        } catch (Exception e) {
            log.error("Erro ao iniciar o Kafka", e);
            return false;
        }
    }

    public void pararKafka() {
        try {
            log.info("Parando Kafka...");

            // Executa o script de parada do Kafka
            Process stopProcess = Runtime.getRuntime().exec(STOP_KAFKA_SCRIPT);
            stopProcess.waitFor();  // Aguarda o script finalizar
            log.info("Kafka foi parado com sucesso.");

        } catch (Exception e) {
            log.error("Erro ao parar o Kafka", e);
        }
    }

    private boolean isKafkaReady() {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress("localhost", KAFKA_PORT), 2000);
                return true;  // Kafka está pronto
            } catch (IOException e) {
                log.warn("Kafka ainda não está pronto, aguardando...");
                retryCount++;
                try {
                    Thread.sleep(RETRY_DELAY_MS);  // Aguarda alguns segundos antes de tentar novamente
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.error("Thread interrompida", ie);
                }
            }
        }
        return false;  // Kafka não está pronto após várias tentativas
    }
}