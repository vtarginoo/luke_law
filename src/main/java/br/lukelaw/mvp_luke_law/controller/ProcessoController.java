package br.lukelaw.mvp_luke_law.controller;


import br.lukelaw.mvp_luke_law.canais.email.Email;
import br.lukelaw.mvp_luke_law.canais.email.EmailService;
import br.lukelaw.mvp_luke_law.canais.email.dto.NotificacaoEmailRequest;
import br.lukelaw.mvp_luke_law.canais.email.dto.NotificacaoEmailResponse;
import br.lukelaw.mvp_luke_law.canais.whatsapp.WhatsappService;
import br.lukelaw.mvp_luke_law.canais.whatsapp.dto.NotificacaoWppRequest;
import br.lukelaw.mvp_luke_law.entity.Processo;
import br.lukelaw.mvp_luke_law.service.ProcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    ProcessoService processoService;

    @Autowired
    EmailService emailService;

    @Autowired
    WhatsappService wppService;


    @GetMapping("/{numProcesso}")
    public ResponseEntity<Processo> capturarInfoProcesso (@PathVariable String numProcesso) {

        Processo response = processoService.realizarRequisicao(numProcesso);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/email")
    public ResponseEntity<NotificacaoEmailResponse> notificacaoEmailProcesso
            (@RequestBody NotificacaoEmailRequest emailRequest) {

        var requestProcesso = processoService.realizarRequisicao(emailRequest.numProcesso());
        var analiseProcesso = processoService.analisarMovimentacao(requestProcesso);

        if (analiseProcesso.isMovimentoRecente()){
        String emailBody =  emailService.createEmailBody("movimentacao-alert",
                "analiseDeMovimento", analiseProcesso);

        var email = new Email(emailRequest.destinatario(), "Movimentação Processo - "
                + emailRequest.numProcesso(), emailBody);
        emailService.sendEmail(email);

        return  ResponseEntity.ok(new  NotificacaoEmailResponse(analiseProcesso, true));
        }

        return  ResponseEntity.ok(new  NotificacaoEmailResponse(analiseProcesso, false));

    }

    @PostMapping("/wpp")
    public ResponseEntity<NotificacaoEmailResponse> notificacaoWppProcesso
            (@RequestBody NotificacaoWppRequest wppRequest) {

        var requestProcesso = processoService.realizarRequisicao(wppRequest.numProcesso());
        var analiseProcesso = processoService.analisarMovimentacao(requestProcesso);

        String messageBody = "Prezado Cliente, segue as informações sobre a movimentação do processo "
                + requestProcesso.getNumeroProcesso() +
                "\nData e hora da movimentação: " + analiseProcesso.getUltimoMovimento().dataHora() +
                "Tipo de Movimentação " +  analiseProcesso.getUltimoMovimento().nome();

        if (analiseProcesso.isMovimentoRecente()){
            wppService.notificacaoWhatsapp(messageBody);

            return  ResponseEntity.ok(new  NotificacaoEmailResponse(analiseProcesso, true));
        }

        return  ResponseEntity.ok(new  NotificacaoEmailResponse(analiseProcesso, false));

    }





}
