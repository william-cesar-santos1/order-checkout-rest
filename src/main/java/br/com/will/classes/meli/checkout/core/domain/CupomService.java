package br.com.will.classes.meli.checkout.core.domain;

import br.com.will.classes.meli.checkout.core.domain.regras.RegraCategoria;
import br.com.will.classes.meli.checkout.core.domain.regras.RegraDeCupom;
import br.com.will.classes.meli.checkout.core.domain.regras.RegraValidade;
import br.com.will.classes.meli.checkout.core.domain.regras.RegraValorMinimo;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CupomService {

    private final List<RegraDeCupom> regras;

    public CupomService() {
        this.regras = List.of(new RegraValidade(), new RegraValorMinimo(), new RegraCategoria());
    }

    public List<MotivoFalhaCupom> validar(Cupom cupom, Carrinho carrinho, LocalDate hoje) {
        List<MotivoFalhaCupom> motivos = new ArrayList<>();
        for (var r : regras) {
            var m = r.aplicar(cupom, carrinho, hoje);
            if (m != null) motivos.add(m);
        }
        return List.copyOf(motivos);
    }
}