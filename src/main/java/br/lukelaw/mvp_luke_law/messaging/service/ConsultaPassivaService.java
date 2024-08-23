package br.lukelaw.mvp_luke_law.messaging.service;

import br.lukelaw.mvp_luke_law.messaging.templates.MessageTemplate;
import br.lukelaw.mvp_luke_law.webscraping.entity.Movimento;
import br.lukelaw.mvp_luke_law.webscraping.entity.Processo;
import br.lukelaw.mvp_luke_law.xSimulateBD.BDSimulate;
import br.lukelaw.mvp_luke_law.xSimulateBD.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ConsultaPassivaService {

    @Autowired
    WhatsappService whatsappService;

    @Autowired
    MessageTemplate messageTemplate;

    @Autowired
    private BDSimulate bdSimulate;

    String messageBody;

    public void envioDeConsultaPassiva(Processo requestProcesso, String advWpp) throws JsonProcessingException {

        Movimento movimentoProcesso =requestProcesso.getMovimentos().get(0);

        LocalDateTime dataUltimoMovimento = movimentoProcesso.dataHora();

        // Obter a data e hora atual
        LocalDateTime agora = LocalDateTime.now();

        // Calcular a diferença em horas
        long horasDesdeUltimoMovimento = ChronoUnit.HOURS.between(dataUltimoMovimento, agora);

      //  === Identificação para Envio ==
        // Encontra o usuário pelo número de celular
        User user = bdSimulate.findUserByPhoneNumber(advWpp);

        if (user != null) {
            // Usa o template identificado se o usuário for encontrado
            messageBody = messageTemplate.mensagemBodyPassivaIdentificado(requestProcesso, user);
        } else {
            // Usa o template genérico se o usuário não for encontrado
            messageBody = messageTemplate.mensagemBodyPassivaGenerico(requestProcesso);
        }

        //=== Envia a mensagem
        whatsappService.envioWhatsapp(advWpp, messageBody);
    }
}
