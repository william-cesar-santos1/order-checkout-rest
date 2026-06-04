package br.com.will.classes.meli.checkout.core.infrastructure;

import br.com.will.classes.meli.checkout.core.application.CarrinhoFiltro;
import br.com.will.classes.meli.checkout.core.application.ports.CarrinhoRepositoryPort;
import br.com.will.classes.meli.checkout.core.domain.Carrinho;
import br.com.will.classes.meli.checkout.core.domain.ItemCarrinho;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Repository
public class CarrinhoRepository implements CarrinhoRepositoryPort {

    private final Map<String, Carrinho> store = new ConcurrentHashMap<>();

    public CarrinhoRepository() {
        var c1 = new Carrinho(
                "c-1",
                "cliente-alice",
                List.of(
                        new ItemCarrinho("p-100", "eletronicos", new BigDecimal("150.00"), 1),
                        new ItemCarrinho("p-200", "casa", new BigDecimal("100.00"), 1)
                ),
                Set.of("eletronicos", "casa"),
                new BigDecimal("250.00"),
                null,
                new BigDecimal("250.00")
        );
        var c2 = new Carrinho(
                "c-2",
                "cliente-bob",
                List.of(new ItemCarrinho("p-300", "moda", new BigDecimal("80.00"), 1)),
                Set.of("moda"),
                new BigDecimal("80.00"),
                null,
                new BigDecimal("80.00")
        );
        store.put(c1.id(), c1);
        store.put(c2.id(), c2);
    }

    @Override
    public List<Carrinho> buscarTodos(CarrinhoFiltro filtro) {
        Stream<Carrinho> stream = store.values().stream();

        if (filtro.clienteId() != null && !filtro.clienteId().isBlank()) {
            stream = stream.filter(c -> filtro.clienteId().equals(c.clienteId()));
        }
        if (filtro.valorMinimo() != null) {
            stream = stream.filter(c -> c.valorFinal().compareTo(filtro.valorMinimo()) >= 0);
        }

        return stream.toList();
    }

    @Override
    public Optional<Carrinho> buscar(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Carrinho salvar(Carrinho carrinho) {
        store.put(carrinho.id(), carrinho);
        return carrinho;
    }
}