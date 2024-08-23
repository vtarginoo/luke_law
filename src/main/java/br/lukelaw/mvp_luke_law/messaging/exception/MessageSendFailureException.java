package br.lukelaw.mvp_luke_law.messaging.exception;

public class MessageSendFailureException extends RuntimeException {
    public MessageSendFailureException(String message) {
        super(message);
    }
}
