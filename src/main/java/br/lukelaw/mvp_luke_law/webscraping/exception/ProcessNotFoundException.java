package br.lukelaw.mvp_luke_law.webscraping.exception;

import lombok.Getter;

@Getter
public class ProcessNotFoundException extends RuntimeException {
    private final String celular;

    public ProcessNotFoundException(String message) {
        super(message);
        this.celular = null;
    }

    public ProcessNotFoundException(String message, String celular) {
        super(message);
        this.celular = celular;
    }
}
