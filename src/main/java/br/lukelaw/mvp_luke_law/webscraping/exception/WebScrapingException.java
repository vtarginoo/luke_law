package br.lukelaw.mvp_luke_law.webscraping.exception;

import lombok.Getter;

@Getter
public class WebScrapingException extends RuntimeException {
    private final String celular;

    public WebScrapingException(String message) {
        super(message);
        this.celular = null;
    }

    public WebScrapingException(String message, String celular) {
        super(message);
        this.celular = celular;
    }
}
