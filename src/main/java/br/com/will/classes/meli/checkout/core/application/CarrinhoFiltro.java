package br.com.will.classes.meli.checkout.core.application;

import java.math.BigDecimal;

public record CarrinhoFiltro(
        String clienteId,
        BigDecimal valorMinimo
) {
}

