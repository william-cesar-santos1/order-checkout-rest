package br.com.will.classes.meli.checkout.core.api;

import br.com.will.classes.meli.checkout.core.application.CarrinhoFiltro;

import java.math.BigDecimal;

public record CarrinhoFiltroRequest(
        String clienteId,
        BigDecimal valorMinimo
) {
    public CarrinhoFiltro toFiltro() {
        return new CarrinhoFiltro(clienteId, valorMinimo);
    }
}
