package br.lukelaw.mvp_luke_law.messaging.templates;

import org.springframework.stereotype.Component;

@Component
public class MessageErrorTemplate {

    public String processNotFoundMessage() {
        return "Prezado Cliente, o processo solicitado não foi encontrado no sistema do PJE." +
                " Por favor, verifique o número do processo e tente novamente.";
    }

    public String badRequestMessage() {
        return "Prezado Cliente, houve um problema com os dados enviados." +
                " Por favor, verifique as informações e tente novamente.";
    }

    public String webScrapingErrorMessage() {
        return "Prezado Cliente, ocorreu um erro durante o processamento do web scraping." +
                " Tente novamente mais tarde.";
    }

    public String messageSendFailureMessage() {
        return "Prezado Cliente, ocorreu uma falha ao enviar a mensagem via WhatsApp." +
                " Estamos investigando o problema.";
    }

    public String generalErrorMessage() {
        return "Prezado Cliente, ocorreu um erro interno. Estamos investigando o problema.";
    }
}

