package br.com.will.classes.meli.checkout.core.domain;

import java.math.BigDecimal;

public record ValorMinimo(BigDecimal exigido) implements MotivoFalhaCupom {
}

