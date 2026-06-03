package br.com.will.classes.meli.checkout.core.domain;

import java.time.LocalDate;

public record Expirado(LocalDate em) implements MotivoFalhaCupom {
}

