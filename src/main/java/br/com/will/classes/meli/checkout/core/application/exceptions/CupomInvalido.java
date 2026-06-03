package br.com.will.classes.meli.checkout.core.application.exceptions;

import br.com.will.classes.meli.checkout.core.domain.MotivoFalhaCupom;

import java.util.List;

public class CupomInvalido extends RuntimeException {
    private final List<MotivoFalhaCupom> motivos;

    public CupomInvalido(List<MotivoFalhaCupom> motivos) {
        super("Cupom inválido: " + motivos.size() + " motivo(s)");
        this.motivos = List.copyOf(motivos);
    }

    public List<MotivoFalhaCupom> getMotivos() {
        return motivos;
    }
}