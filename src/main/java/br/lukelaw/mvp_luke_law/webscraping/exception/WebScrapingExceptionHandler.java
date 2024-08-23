package br.lukelaw.mvp_luke_law.webscraping.exception;


import br.lukelaw.mvp_luke_law.messaging.exception.BadRequestException;
import br.lukelaw.mvp_luke_law.webscraping.controller.WebScrapingController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(assignableTypes = WebScrapingController.class)
public class WebScrapingExceptionHandler {

    @ExceptionHandler(ProcessNotFoundException.class)
    public ResponseEntity<ResponseError> handleProcessNotFoundException(ProcessNotFoundException ex) {
        ResponseError errorResponse = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ResponseError>> handleValidationErrors400(MethodArgumentNotValidException ex) {
        List<ResponseError> errors = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            errors.add(new ResponseError(fieldName + ": " + errorMessage, HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(WebScrapingException.class)
    public ResponseEntity<ResponseError> handleWebScrapingException(WebScrapingException ex) {
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
                "Erro interno ao processar a requisição de web scraping.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}







