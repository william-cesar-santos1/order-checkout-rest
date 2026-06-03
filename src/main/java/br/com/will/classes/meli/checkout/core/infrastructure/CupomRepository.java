package br.com.will.classes.meli.checkout.core.infrastructure;

import br.com.will.classes.meli.checkout.core.domain.Cupom;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CupomRepository {

    private final Map<String, Cupom> store = new ConcurrentHashMap<>();

    public CupomRepository() {
        // Cupom válido para os carrinhos de fixture.
        store.put("MELI10", new Cupom(
                "MELI10",
                LocalDate.of(2026, 12, 31),
                new BigDecimal("100.00"),
                new BigDecimal("25.00"),
                Set.of("eletronicos", "casa")
        ));
        // Cupom expirado, para acionar 422 com Expirado.
        store.put("EXPIRADO", new Cupom(
                "EXPIRADO",
                LocalDate.of(2025, 1, 1),
                new BigDecimal("50.00"),
                new BigDecimal("10.00"),
                Set.of("eletronicos", "casa", "moda")
        ));
        // Cupom só para moda, para acionar CategoriaInvalida com c-1.
        store.put("MODA15", new Cupom(
                "MODA15",
                LocalDate.of(2026, 12, 31),
                new BigDecimal("50.00"),
                new BigDecimal("15.00"),
                Set.of("moda")
        ));
    }

    public Optional<Cupom> buscar(String codigo) {
        return Optional.ofNullable(store.get(codigo));
    }
}