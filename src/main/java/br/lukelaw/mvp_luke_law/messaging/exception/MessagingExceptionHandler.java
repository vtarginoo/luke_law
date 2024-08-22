package br.lukelaw.mvp_luke_law.messaging.exception;

import br.lukelaw.mvp_luke_law.messaging.controller.MessagingController;
import br.lukelaw.mvp_luke_law.messaging.service.WhatsappService;
import br.lukelaw.mvp_luke_law.messaging.templates.MessageErrorTemplate;
import br.lukelaw.mvp_luke_law.webscraping.exception.ProcessNotFoundException;
import br.lukelaw.mvp_luke_law.webscraping.exception.ResponseError;
import br.lukelaw.mvp_luke_law.webscraping.exception.WebScrapingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = MessagingController.class)
public class MessagingExceptionHandler {

    @Autowired
    private WhatsappService wppService;

    @Autowired
    private MessageErrorTemplate messageErrorTemplateService;



    @ExceptionHandler(ProcessNotFoundException.class)
    public ResponseEntity<ResponseError> handleProcessNotFoundException(ProcessNotFoundException ex) {
        ResponseError errorResponse = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );

        var messageBody = messageErrorTemplateService.processNotFoundMessage();
        wppService.envioWhatsapp(ex.getCelular(), messageBody);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseError> handleBadRequestException(BadRequestException ex) {
        ResponseError errorResponse = ex.getResponseError();

        var msgBody = messageErrorTemplateService.badRequestMessage();
        wppService.envioWhatsapp(ex.getCelular(), msgBody);

        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(WebScrapingException.class)
    public ResponseEntity<ResponseError> handleWebScrapingException(WebScrapingException ex) {
        ResponseError errorResponse = new ResponseError(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );

        var msgBody = messageErrorTemplateService.webScrapingErrorMessage();
        wppService.envioWhatsapp(ex.getCelular(), msgBody);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MessageSendFailureException.class)
    public ResponseEntity<ResponseError> handleMessageSendFailureException(MessageSendFailureException ex) {
        ResponseError errorResponse = new ResponseError(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleGeneralException(Exception ex) {
        ResponseError errorResponse = new ResponseError(
                "Erro interno ao processar a requisição de mensagem.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

