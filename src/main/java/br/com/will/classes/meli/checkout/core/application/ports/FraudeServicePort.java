package br.com.will.classes.meli.checkout.core.application.ports;

import java.math.BigDecimal;

public interface FraudeServicePort {

    /**
     * Analisa se a operação possui indicativo de fraude.
     *
     * @param carrinhoId  identificador do carrinho
     * @param valorBruto  valor total do carrinho
     * @return {@code true} se fraude detectada, {@code false} caso contrário
     */
    boolean ehFraude(String carrinhoId, BigDecimal valorBruto);
}

