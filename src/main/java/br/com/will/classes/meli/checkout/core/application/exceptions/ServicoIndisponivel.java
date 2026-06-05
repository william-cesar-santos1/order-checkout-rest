package br.com.will.classes.meli.checkout.core.application.exceptions;

public class ServicoIndisponivel extends RuntimeException {

    public ServicoIndisponivel(String mensagem) {
        super(mensagem);
    }
}

