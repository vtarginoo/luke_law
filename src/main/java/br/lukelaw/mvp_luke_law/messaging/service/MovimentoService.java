package br.lukelaw.mvp_luke_law.messaging.service;


import br.lukelaw.mvp_luke_law.messaging.dto.AnaliseDeMovimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Movimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Service
public class MovimentoService {

    private static final Logger log = LoggerFactory.getLogger(MovimentoService.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "dd/MM/yyyy HH:mm:ss");

    public Movimento criarMovimento(String movimentoScrape) {
        try {
            // Separa a string em partes usando parênteses como delimitadores
            String[] partes = movimentoScrape.split("\\(");
            if (partes.length < 2) {
                throw new IllegalArgumentException(
                        "Formato inválido para a string de movimentação: " + movimentoScrape);
            }

            // A primeira parte é o nome da movimentação
            String nome = partes[0].trim();

            // A segunda parte contém a data e hora, remover o parêntese final e o código "00"
            String dataHoraString = partes[1].split("\\)")[0].trim();

            // Converte a string de data e hora para LocalDateTime
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraString, DATE_TIME_FORMATTER);

            // Define o código 999 para o último movimento
            Long ordem = 999L;

            // Retorna o novo objeto Movimento
            return new Movimento(ordem, nome, dataHora);

        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Erro ao parsear a string de movimentação: "
                    + movimentoScrape, e);
        }
    }

    public AnaliseDeMovimento analisarMovimentacao(Processo processo) {
        // Obter o último movimento
        Movimento ultimoMovimento = processo.getMovimentos().get(processo.getMovimentos().size() - 1);
        log.info("Último movimento obtido: {}", ultimoMovimento);

        // Converter a data do último movimento para LocalDateTime
        LocalDateTime dataUltimoMovimento = ultimoMovimento.dataHora();
        log.info("Data e hora do último movimento: {}", dataUltimoMovimento);

        // Obter a data e hora atual
        LocalDateTime agora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        log.info("Data e hora atual (SP): {}", agora);

        // Calcular a diferença em horas
        long horasDesdeUltimoMovimento = ChronoUnit.HOURS.between(dataUltimoMovimento, agora);
        log.info("Horas desde o último movimento: {}", horasDesdeUltimoMovimento);

        // Verificar se a movimentação ocorreu fora do horário de expediente (entre 19h e 7h)
        boolean foraDoHorarioExpediente = dataUltimoMovimento.getHour() >= 19 || dataUltimoMovimento.getHour() < 7;
        log.info("Movimentação fora do horário de expediente: {}", foraDoHorarioExpediente);

        // Verificar se estamos na primeira execução do dia (às 8h)
        boolean primeiraVerificacaoDoDia = agora.getHour() == 8;
        log.info("Primeira verificação do dia: {}", primeiraVerificacaoDoDia);

        // Lógica para movimentação fora do horário de expediente
        if (foraDoHorarioExpediente && horasDesdeUltimoMovimento < 12 && primeiraVerificacaoDoDia) {
            log.info("Movimento recente detectado fora do horário de expediente.");
            return new AnaliseDeMovimento(processo, horasDesdeUltimoMovimento, true);
        }

        // Lógica padrão para movimentação dentro do horário de expediente
        boolean movimentoRecente = horasDesdeUltimoMovimento < 2;
        log.info("Movimento recente dentro do horário de expediente: {}", movimentoRecente);

        return new AnaliseDeMovimento(processo, horasDesdeUltimoMovimento, movimentoRecente);
    }

}