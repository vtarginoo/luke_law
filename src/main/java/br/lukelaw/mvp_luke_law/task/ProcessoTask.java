package br.lukelaw.mvp_luke_law.task;

import br.lukelaw.mvp_luke_law.email.Email;
import br.lukelaw.mvp_luke_law.email.EmailService;
import br.lukelaw.mvp_luke_law.entity.Movimento;
import br.lukelaw.mvp_luke_law.service.ProcessoService;
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

    @Scheduled(initialDelay = 120000 ) // 5 minutos em milissegundos
public void monitoramentoMovimentoDeProcesso () {

    var user = "vtarginoo@gmail.com";
    String[] processos = {"09077874720238190001", "09476172020238190001", "01016022920238190000"};

    for (String processo: processos) {

        var processoMonitorado = processoService.realizarRequisicao(processo);
        var ultimoMovimento = processoMonitorado.getMovimentos().get(processoMonitorado.getMovimentos().size() - 1);

        if(processoService.analisarMovimentacao(processoMonitorado)) {

           String emailBody =  emailService.createEmailBody("movimentacao-alert",
                   "processo",processoMonitorado, "movimento", ultimoMovimento);

            var email = new Email(user, "Movimentação Processo - " + processo, emailBody);

            emailService.sendEmail(email);
        }
    }
   }
}
