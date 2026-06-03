package br.com.will.classes.meli.checkout.core.domain.regras;

import br.com.will.classes.meli.checkout.core.domain.Carrinho;
import br.com.will.classes.meli.checkout.core.domain.Cupom;
import br.com.will.classes.meli.checkout.core.domain.Expirado;
import br.com.will.classes.meli.checkout.core.domain.MotivoFalhaCupom;

import java.time.LocalDate;

public final class RegraValidade implements RegraDeCupom {

    @Override
    public MotivoFalhaCupom aplicar(Cupom c, Carrinho car, LocalDate hoje) {
        return hoje.isAfter(c.validoAte()) ? new Expirado(c.validoAte()) : null;
    }
}

