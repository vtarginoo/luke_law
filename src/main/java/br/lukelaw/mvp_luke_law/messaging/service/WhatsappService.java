package br.lukelaw.mvp_luke_law.messaging.service;

import br.lukelaw.mvp_luke_law.webscraping.entity.Movimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.webscraping.service.MovimentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class WhatsappService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String twilioPhoneNumber;

    @Autowired
    MovimentoService movimentoService;

    //"whatsapp:+5521996066505"

    public void notificacaoWhatsapp (String advWpp, String bodyMessage){

        Twilio.init(accountSid, authToken);

        Message message1 = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + advWpp),
                new com.twilio.type.PhoneNumber(twilioPhoneNumber),bodyMessage).create();

        System.out.println(message1.getSid());

        Message fetchedMessage = Message.fetcher(message1.getSid()).fetch();
        System.out.println("Fetched Message Status: " + fetchedMessage.getStatus());

    }


    public void envioDeConsultaPassiva(Processo requestProcesso) throws JsonProcessingException {

        Movimento movimentoProcesso =requestProcesso.getMovimentos().get(0);

        LocalDateTime dataUltimoMovimento = movimentoProcesso.dataHora();

        // Obter a data e hora atual
        LocalDateTime agora = LocalDateTime.now();

        // Calcular a diferença em horas
        long horasDesdeUltimoMovimento = ChronoUnit.HOURS.between(dataUltimoMovimento, agora);

        // Partes Envolvidas
        String partesEnvolvidas = requestProcesso.getPartesEnvolvidas();

        String messageBody =
                "*ℹ️ Segue a última movimentação de seu processo:*\n\n" +
                        "👥 *Partes:* " + partesEnvolvidas + "\n" +
                        "📄 *Processo:* " + requestProcesso.getNumeroProcesso() + "\n" +
                        "🏛️ *Tribunal:* " + requestProcesso.getTribunal() + "\n" +
                        "🖥️ *Sistema:* " + requestProcesso.getSistema() + "\n\n" +
                        "*Última Movimentação:*\n" +
                        "🔍 *Tipo:* " + movimentoProcesso.nome() + "\n" +
                        "🕒 *Data e Hora:* " + movimentoProcesso.dataHora() + "\n" +
                        "⏳ *Horas desde a Última Movimentação:* " + horasDesdeUltimoMovimento + " horas\n\n" +
                        "⚖️ Por favor, verifique os detalhes no sistema.";

        String advWpp = "+552199680027";
        notificacaoWhatsapp(advWpp,messageBody);
    }
        }






