package br.lukelaw.mvp_luke_law.task;

import br.lukelaw.mvp_luke_law.canais.email.Email;
import br.lukelaw.mvp_luke_law.canais.email.EmailService;
import br.lukelaw.mvp_luke_law.canais.whatsapp.WhatsappService;
import br.lukelaw.mvp_luke_law.service.ProcessoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class ProcessoTask {

    @Autowired
    ProcessoService processoService;

    @Autowired
    EmailService emailService;

    @Autowired
    WhatsappService wppService;

    //@Scheduled(initialDelay = 120000 ) // 5 minutos em milissegundos
public void monitoramentoMovimentoDeProcessoEmail () throws JsonProcessingException {

    var user = "vtarginoo@gmail.com";
    //var user = "sefyunes@gmail.com";

    String[] processos = {"09077874720238190001", "09476172020238190001", "01016022920238190000"};

    for (String processo: processos) {

        var processoMonitorado = processoService.reqDataJud(processo);
        var analiseDeMovimento = processoService.analisarMovimentacao(processoMonitorado);

        var hora = analiseDeMovimento.getUltimoMovimento().dataHora();

        if(analiseDeMovimento.isMovimentoRecente()) {

           String emailBody =  emailService.createEmailBody("movimentacao-alert",
                   "analiseDeMovimento", analiseDeMovimento);

            var email = new Email(user, "Movimentação Processo - " + processo, emailBody);

            emailService.sendEmail(email);
        }
    }
   }

    //@Scheduled(initialDelay = 120000 ) // 5 minutos em milissegundos
    public void monitoramentoMovimentoDeProcessoWpp () throws JsonProcessingException {

        String[] processos = {"09077874720238190001", "09476172020238190001", "01016022920238190000",
                "08091295120248190001"};

        for (String processo: processos) {

            var processoMonitorado = processoService.reqDataJud(processo);
            var analiseDeMovimento = processoService.analisarMovimentacao(processoMonitorado);

            String messageBody = "Aqui é o Luke! teve movimentação no seu processo da uma olhada ai! "
                    + processoMonitorado.getNumeroProcesso() +
                    "\nData e hora da movimentação: " + analiseDeMovimento.getUltimoMovimento().dataHora() +
                    "Tipo de Movimentação " +  analiseDeMovimento.getUltimoMovimento().nome();

            if(analiseDeMovimento.isMovimentoRecente()) {
                wppService.notificacaoWhatsapp(messageBody);
            }
        }
    }






}
