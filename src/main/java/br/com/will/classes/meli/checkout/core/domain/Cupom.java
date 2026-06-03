package br.com.will.classes.meli.checkout.core.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record Cupom(
        String codigo,
        LocalDate validoAte,
        BigDecimal valorMinimo,
        BigDecimal desconto,
        Set<String> categoriasElegiveis) {

    public Cupom {
        categoriasElegiveis = Set.copyOf(categoriasElegiveis);
    }
}