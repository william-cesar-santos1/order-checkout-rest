package br.com.will.classes.meli.checkout.core.api.filter;

import java.time.Instant;

record EntradaIdempotente(String hashCorpo, int status, String contentType,
                          String corpoResposta, Instant expiraEm) {
    boolean expirado() {
        return Instant.now().isAfter(expiraEm);
    }
}