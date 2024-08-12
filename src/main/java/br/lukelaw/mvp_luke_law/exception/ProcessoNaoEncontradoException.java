package br.lukelaw.mvp_luke_law.exception;

public class ProcessoNaoEncontradoException extends RuntimeException {
    public ProcessoNaoEncontradoException(String message) {
        super(message);
    }
}
