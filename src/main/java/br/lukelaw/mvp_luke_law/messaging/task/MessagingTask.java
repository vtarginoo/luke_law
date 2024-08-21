package br.lukelaw.mvp_luke_law.messaging.task;

import br.lukelaw.mvp_luke_law.messaging.dto.AnaliseDeMovimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.webscraping.service.MovimentoService;
import br.lukelaw.mvp_luke_law.messaging.service.WhatsappService;
import br.lukelaw.mvp_luke_law.webscraping.xSimulateBD.BDSimulate;
import br.lukelaw.mvp_luke_law.webscraping.xSimulateBD.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class MessagingTask {

    private static final Logger log = LoggerFactory.getLogger(MessagingTask.class);

    @Autowired
    MovimentoService movimentoService;

    @Autowired
    WhatsappService wppService;

    @Autowired
    BDSimulate bdSimulate;

    @KafkaListener(topics = "processos", groupId = "processo_group", containerFactory = "kafkaListenerContainerFactory")
    public void monitoramentoMovimentoDeProcessoWpp(Processo requestProcesso) throws JsonProcessingException {

        log.info("Processo recebido: {}", requestProcesso.getNumeroProcesso());

        var analiseDeMovimento = movimentoService.analisarMovimentacao(requestProcesso);

        // Coloque a negação para testar a notificação mesmo quando o movimento não é recente
        if (!analiseDeMovimento.isMovimentoRecente()) {
            List<Integer> advogadosAssociados = bdSimulate.processosAssociados.get(requestProcesso.getNumeroProcesso());
            if (advogadosAssociados != null) {
                for (int advId : advogadosAssociados) {
                    User advogado = bdSimulate.usuarios.stream()
                            .filter(user -> user.id() == advId)
                            .findFirst()
                            .orElse(null);

                    if (advogado != null) {
                        String messageBody = getMessageBody(analiseDeMovimento, requestProcesso, advogado.nome());
                        wppService.notificacaoWhatsapp(advogado.celular(), messageBody);
                    }
                }
            } else {
                log.warn("Nenhum advogado associado ao processo {}", requestProcesso.getNumeroProcesso());
            }
        }
    }

    private String getMessageBody(AnaliseDeMovimento analiseDeMovimento, Processo requestProcesso, String advogadoNome) {
        var ultimoMovimento = analiseDeMovimento.getProcesso().getMovimentos().get(0);

        return  "*⚠️ Alerta de Movimentação no Processo*\n\n" +
                "Olá, *" + advogadoNome.toUpperCase() + "*!\n" +   // Coloca o nome do advogado em maiúsculas e negrito
                "👥 *Partes:* " + requestProcesso.getPartesEnvolvidas() + "\n" +
                "📄 *Processo:* " + requestProcesso.getNumeroProcesso() + "\n" +
                "🏛️ *Tribunal:* " + requestProcesso.getTribunal() + "\n" +
                "🖥️ *Sistema:* " + requestProcesso.getSistema() + "\n\n" +
                "*Última Movimentação:*\n" +
                "🔍 *Tipo:* " + ultimoMovimento.nome() + "\n" +
                "🕒 *Data e Hora:* " + ultimoMovimento.dataHora() + "\n" +
                "⏳ *Horas desde a Última Movimentação:* " + analiseDeMovimento.getHorasDesdeUltimoMovimento() + " horas\n\n" +
                "⚖️ Por favor, verifique os detalhes no sistema.";
    }
}

