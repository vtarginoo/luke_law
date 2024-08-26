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

    public boolean iniciarKafka() {
        try {
            log.info("Iniciando Kafka...");

            // Executa o script de inicialização do Kafka
            Process startProcess = Runtime.getRuntime().exec(START_KAFKA_SCRIPT);
            int exitCode = startProcess.waitFor();

            if (exitCode != 0) {
                log.error("Falha ao executar o script de inicialização do Kafka. Código de saída: " + exitCode);
                return false;
            }

            // Aguardar alguns segundos para o Kafka iniciar
            Thread.sleep(10000);  // Espera 10 segundos para o Kafka iniciar

            // Verifica se o Kafka está escutando na porta padrão (9092)
            if (isKafkaReady()) {
                log.info("Kafka está pronto e escutando na porta " + KAFKA_PORT + ".");
                return true;
            } else {
                log.error("Kafka não está pronto, abortando...");
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
            int exitCode = stopProcess.waitFor();

            if (exitCode == 0) {
                log.info("Kafka foi parado com sucesso.");
            } else {
                log.error("Falha ao parar o Kafka. Código de saída: " + exitCode);
            }

        } catch (Exception e) {
            log.error("Erro ao parar o Kafka", e);
        }
    }

    private boolean isKafkaReady() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", KAFKA_PORT), 2000);
            return true;  // Kafka está pronto
        } catch (IOException e) {
            log.warn("Kafka não está escutando na porta " + KAFKA_PORT);
            return false;
        }
    }
}