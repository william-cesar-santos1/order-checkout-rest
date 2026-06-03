package br.com.will.classes.meli.checkout.core.application.exceptions;

public class CarrinhoNaoEncontrado extends RuntimeException {
    private final String carrinhoId;

    public CarrinhoNaoEncontrado(String carrinhoId) {
        super("Carrinho %s não encontrado".formatted(carrinhoId));
        this.carrinhoId = carrinhoId;
    }

    public String getCarrinhoId() { return carrinhoId; }
}