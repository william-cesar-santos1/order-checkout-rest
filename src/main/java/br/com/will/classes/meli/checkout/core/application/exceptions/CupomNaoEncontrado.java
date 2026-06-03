package br.com.will.classes.meli.checkout.core.application.exceptions;

public class CupomNaoEncontrado extends RuntimeException {
    private final String codigo;

    public CupomNaoEncontrado(String codigo) {
        super("Cupom %s não encontrado".formatted(codigo));
        this.codigo = codigo;
    }

    public String getCodigo() { return codigo; }
}