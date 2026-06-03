package br.com.will.classes.meli.checkout.core.api;

import br.com.will.classes.meli.checkout.core.application.AplicarCupomUseCase;
import br.com.will.classes.meli.checkout.core.domain.Carrinho;
import br.com.will.classes.meli.checkout.core.infrastructure.CarrinhoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoRepository carrinhoRepository;
    private final AplicarCupomUseCase aplicarCupom;

    public CarrinhoController(CarrinhoRepository carrinhoRepository,
                              AplicarCupomUseCase aplicarCupom) {
        this.carrinhoRepository = carrinhoRepository;
        this.aplicarCupom = aplicarCupom;
    }

    @GetMapping("/{id}")
    public Carrinho buscar(@PathVariable String id) {
        return carrinhoRepository.buscar(id).orElse(null);
    }

    @PostMapping("/{id}/cupom")
    public Carrinho aplicarCupom(@PathVariable String id,
                                 @RequestBody Map<String, Object> payload) {
        String codigo = (String) payload.get("codigo");
        if (codigo == null) {
            throw new RuntimeException("codigo obrigatório");
        }
        return aplicarCupom.aplicar(id, codigo);
    }
}