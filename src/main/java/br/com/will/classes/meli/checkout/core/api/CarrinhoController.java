package br.com.will.classes.meli.checkout.core.api;

import br.com.will.classes.meli.checkout.core.application.AplicarCupomUseCase;
import br.com.will.classes.meli.checkout.core.application.exceptions.CarrinhoNaoEncontrado;
import br.com.will.classes.meli.checkout.core.application.ports.CarrinhoRepositoryPort;
import br.com.will.classes.meli.checkout.core.domain.Carrinho;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoRepositoryPort carrinhoRepository;
    private final AplicarCupomUseCase aplicarCupom;

    public CarrinhoController(CarrinhoRepositoryPort carrinhoRepository,
                              AplicarCupomUseCase aplicarCupom) {
        this.carrinhoRepository = carrinhoRepository;
        this.aplicarCupom = aplicarCupom;
    }

    @GetMapping("/{id}")
    public Carrinho buscar(@PathVariable String id) {
        return carrinhoRepository.buscar(id)
                .orElseThrow(() -> new CarrinhoNaoEncontrado(id));
    }

    @GetMapping
    public List<Carrinho> buscar(@ModelAttribute CarrinhoFiltroRequest filtro) {
        return carrinhoRepository.buscarTodos(filtro.toFiltro());
    }

    @PostMapping("/{id}/cupom")
    public Carrinho aplicarCupom(@PathVariable String id,
                                 @RequestBody @Valid AplicarCupomRequest request) {
        return aplicarCupom.aplicar(id, request.codigo());
    }

}

