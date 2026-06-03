package br.com.will.classes.meli.checkout.core.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record Carrinho(
        String id,
        String clienteId,
        List<ItemCarrinho> itens,
        Set<String> categorias,
        BigDecimal valorBruto,
        Cupom cupomAplicado,
        BigDecimal valorFinal) {

    public Carrinho aplicarCupom(Cupom cupom, BigDecimal desconto) {
        return new Carrinho(id, clienteId, itens, categorias, valorBruto, cupom,
                valorBruto.subtract(desconto));
    }

    public boolean temCupomAplicado() {
        return cupomAplicado != null;
    }
}