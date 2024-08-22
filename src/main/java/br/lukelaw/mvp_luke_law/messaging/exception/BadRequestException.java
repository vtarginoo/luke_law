package br.lukelaw.mvp_luke_law.messaging.exception;

import br.lukelaw.mvp_luke_law.webscraping.exception.ResponseError;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final ResponseError responseError;
    private final String celular;

    public BadRequestException(ResponseError responseError) {
        super(responseError.message());
        this.responseError = responseError;
        this.celular = null;
    }

    public BadRequestException(ResponseError responseError, String celular) {
        super(responseError.message());
        this.responseError = responseError;
        this.celular = celular;
    }
}
