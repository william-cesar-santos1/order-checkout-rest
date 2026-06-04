package br.com.will.classes.meli.checkout.core.application.ports;

import br.com.will.classes.meli.checkout.core.application.CarrinhoFiltro;
import br.com.will.classes.meli.checkout.core.domain.Carrinho;

import java.util.List;
import java.util.Optional;

public interface CarrinhoRepositoryPort {
    Optional<Carrinho> buscar(String id);
    List<Carrinho> buscarTodos(CarrinhoFiltro filtro);
    Carrinho salvar(Carrinho carrinho);
}

