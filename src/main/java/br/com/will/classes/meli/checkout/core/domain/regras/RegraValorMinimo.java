package br.com.will.classes.meli.checkout.core.domain.regras;

import br.com.will.classes.meli.checkout.core.domain.Carrinho;
import br.com.will.classes.meli.checkout.core.domain.Cupom;
import br.com.will.classes.meli.checkout.core.domain.MotivoFalhaCupom;
import br.com.will.classes.meli.checkout.core.domain.ValorMinimo;

import java.time.LocalDate;

public final class RegraValorMinimo implements RegraDeCupom {

    @Override
    public MotivoFalhaCupom aplicar(Cupom c, Carrinho car, LocalDate hoje) {
        return car.valorBruto().compareTo(c.valorMinimo()) < 0
                ? new ValorMinimo(c.valorMinimo())
                : null;
    }
}

