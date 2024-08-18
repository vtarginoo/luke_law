package br.lukelaw.mvp_luke_law.webscraping.service;


import br.lukelaw.mvp_luke_law.webscraping.dto.AnaliseDeMovimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Movimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Service
public class MovimentoService {

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

        // Converter a data do último movimento para LocalDateTime
        LocalDateTime dataUltimoMovimento = ultimoMovimento.dataHora();

        // Obter a data e hora atual
        LocalDateTime agora = LocalDateTime.now();

        // Calcular a diferença em horas
        long horasDesdeUltimoMovimento = ChronoUnit.HOURS.between(dataUltimoMovimento, agora);

        // Verificar se a diferença é menor que 24 horas  |||| Para conseguir testar vou aumentar em 1600 horas
        boolean movimentoRecente = horasDesdeUltimoMovimento < 100;


        return new AnaliseDeMovimento(processo.getNumeroProcesso(),processo.getTribunal(), processo.getSistema(),
                ultimoMovimento, horasDesdeUltimoMovimento,movimentoRecente);
    }









}