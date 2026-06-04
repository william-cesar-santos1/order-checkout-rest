package br.com.will.classes.meli.checkout.core.application.ports;

import br.com.will.classes.meli.checkout.core.domain.Cupom;

import java.util.Optional;

public interface CupomRepositoryPort {
    Optional<Cupom> buscar(String codigo);
}

