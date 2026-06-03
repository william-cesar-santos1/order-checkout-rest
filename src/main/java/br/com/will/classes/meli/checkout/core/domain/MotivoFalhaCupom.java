package br.com.will.classes.meli.checkout.core.domain;

public sealed interface MotivoFalhaCupom
        permits Expirado, ValorMinimo, CategoriaInvalida {
}