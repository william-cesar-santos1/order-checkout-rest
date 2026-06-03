package br.com.will.classes.meli.checkout.core.domain.regras;

import br.com.will.classes.meli.checkout.core.domain.Carrinho;
import br.com.will.classes.meli.checkout.core.domain.Cupom;
import br.com.will.classes.meli.checkout.core.domain.MotivoFalhaCupom;

import java.time.LocalDate;

@FunctionalInterface
public interface RegraDeCupom {
    MotivoFalhaCupom aplicar(Cupom cupom, Carrinho carrinho, LocalDate hoje);
}

