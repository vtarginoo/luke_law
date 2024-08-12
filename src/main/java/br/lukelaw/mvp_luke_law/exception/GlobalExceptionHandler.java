package br.lukelaw.mvp_luke_law.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

   //// Tratamento de Erro 400
    @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class})
    public ResponseEntity<List<ResponseError>> handleValidationErrors400(Exception ex) {
        List<ResponseError> errors = new ArrayList<>();

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationException = (MethodArgumentNotValidException) ex;

            for (FieldError fieldError : validationException.getFieldErrors()) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                errors.add(new ResponseError(errorMessage, HttpStatus.BAD_REQUEST, LocalDateTime.now()));
            }
        } else if (ex instanceof HandlerMethodValidationException) {

            errors.add(new ResponseError("Validation failed", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }

        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(ProcessoNaoEncontradoException.class)
    public ResponseEntity<List<ResponseError>> handleProcessoNaoEncontrado404(ProcessoNaoEncontradoException ex) {
        String message = "Processo Não Encontrado";
        List<ResponseError> errors = Collections.singletonList(
                new ResponseError(message, HttpStatus.NOT_FOUND, LocalDateTime.now())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }


    // Erro 500 Genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> tratamentoExceptionGenerico(Exception ex) {

        ResponseError response = new ResponseError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }











}
