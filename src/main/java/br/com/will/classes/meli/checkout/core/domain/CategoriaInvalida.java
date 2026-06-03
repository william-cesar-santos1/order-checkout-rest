package br.com.will.classes.meli.checkout.core.domain;

import java.util.Set;

public record CategoriaInvalida(Set<String> elegiveis) implements MotivoFalhaCupom {
}

