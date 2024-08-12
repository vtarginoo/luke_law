package br.lukelaw.mvp_luke_law.controller;


import br.lukelaw.mvp_luke_law.canais.email.Email;
import br.lukelaw.mvp_luke_law.canais.email.EmailService;
import br.lukelaw.mvp_luke_law.canais.email.dto.NotificacaoEmailRequest;
import br.lukelaw.mvp_luke_law.canais.email.dto.NotificacaoEmailResponse;
import br.lukelaw.mvp_luke_law.canais.whatsapp.WhatsappService;
import br.lukelaw.mvp_luke_law.canais.whatsapp.dto.NotificacaoWppRequest;
import br.lukelaw.mvp_luke_law.entity.Processo;
import br.lukelaw.mvp_luke_law.service.ProcessoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<Processo> capturarInfoProcesso(
            @PathVariable
            @Valid
            @NotBlank(message = "O número do processo é obrigatório")
            @Pattern(regexp = "^\\d{20}$", message = "O número do processo deve conter exatamente 20 dígitos numéricos")
            String numProcesso) throws JsonProcessingException {

        Processo response = processoService.reqDataJud(numProcesso);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/email")
    public ResponseEntity<NotificacaoEmailResponse> notificacaoEmailProcesso
            (@Valid @NotNull @RequestBody NotificacaoEmailRequest emailRequest) throws JsonProcessingException {

        var requestProcesso = processoService.reqDataJud(emailRequest.numProcesso());
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
            (@Valid @NotNull @RequestBody NotificacaoWppRequest wppRequest) throws JsonProcessingException {

        var requestProcesso = processoService.reqDataJud(wppRequest.numProcesso());
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
