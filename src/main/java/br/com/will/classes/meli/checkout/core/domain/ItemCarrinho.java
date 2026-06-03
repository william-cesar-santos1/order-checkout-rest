package br.com.will.classes.meli.checkout.core.domain;

import java.math.BigDecimal;

public record ItemCarrinho(String produtoId, String categoria, BigDecimal preco, int quantidade) {
}