package br.com.will.classes.meli.checkout.core.domain.regras;

import br.com.will.classes.meli.checkout.core.domain.Carrinho;
import br.com.will.classes.meli.checkout.core.domain.CategoriaInvalida;
import br.com.will.classes.meli.checkout.core.domain.Cupom;
import br.com.will.classes.meli.checkout.core.domain.MotivoFalhaCupom;

import java.time.LocalDate;
import java.util.Set;

public final class RegraCategoria implements RegraDeCupom {

    @Override
    public MotivoFalhaCupom aplicar(Cupom c, Carrinho car, LocalDate hoje) {
        Set<String> cats = car.categorias();
        boolean valida = cats.stream().anyMatch(c.categoriasElegiveis()::contains);
        return valida ? null : new CategoriaInvalida(c.categoriasElegiveis());
    }
}

