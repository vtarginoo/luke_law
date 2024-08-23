package br.lukelaw.mvp_luke_law.webscraping.task;

import br.lukelaw.mvp_luke_law.webscraping.dto.AnaliseDeMovimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.webscraping.service.MovimentoService;
import br.lukelaw.mvp_luke_law.webscraping.service.WebScrapingService;
import br.lukelaw.mvp_luke_law.webscraping.service.WhatsappService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
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

    //@Scheduled(initialDelay = 10000)
    @Scheduled(cron = "0 0 8-19 * * ?", zone = "America/Sao_Paulo")
    public void monitoramentoMovimentoDeProcessoWpp() throws JsonProcessingException {

        String[] processos = {"0838717-06.2024.8.19.0001", "0809129-51.2024.8.19.0001",
                "0947617-20.2023.8.19.0001", "0938160-61.2023.8.19.0001","0907787-47.2023.8.19.0001"};

        for (String processo : processos) {

            var requestProcesso = webScrapingService.scrapePjeUltimoMov(processo);

            if (requestProcesso == null) {
                log.warn("Processo {} foi pulado devido a falha no scraping.", processo);
                continue; // Pula para o próximo processo
            }

            var analiseDeMovimento = movimentoService.analisarMovimentacao(requestProcesso);
            String messageBody = getMessageBody(analiseDeMovimento, requestProcesso); /// ==Corpo==

            if (analiseDeMovimento.isMovimentoRecente()) {
                wppService.notificacaoWhatsapp(messageBody);
            }
        }
    }

    private static @NotNull String getMessageBody(AnaliseDeMovimento analiseDeMovimento, Processo requestProcesso) {
        var ultimoMovimento = analiseDeMovimento.getProcesso().getMovimentos().get(0);


        String messageBody =
                "*⚠️ Alerta de Movimentação no Processo*\n\n" +
                        "👥 *Partes:* " + requestProcesso.getPartesEnvolvidas() + "\n" +  // Incluindo as partes envolvidas
                        "📄 *Processo:* " + requestProcesso.getNumeroProcesso() + "\n" +
                        "🏛️ *Tribunal:* " + requestProcesso.getTribunal() + "\n" +
                        "🖥️ *Sistema:* " + requestProcesso.getSistema() + "\n\n" +
                        "*Última Movimentação:*\n" +
                        "🔍 *Tipo:* " + ultimoMovimento.nome() + "\n" +
                        "🕒 *Data e Hora:* " + ultimoMovimento.dataHora() + "\n" +
                        "⏳ *Horas desde a Última Movimentação:* " + analiseDeMovimento.getHorasDesdeUltimoMovimento() + " horas\n\n" +
                        "⚖️ Por favor, verifique os detalhes no sistema.";

        return messageBody;
    }
}
