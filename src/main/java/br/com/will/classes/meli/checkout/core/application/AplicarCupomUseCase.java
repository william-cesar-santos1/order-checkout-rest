package br.com.will.classes.meli.checkout.core.application;

import br.com.will.classes.meli.checkout.core.application.exceptions.CarrinhoNaoEncontrado;
import br.com.will.classes.meli.checkout.core.application.exceptions.CupomInvalido;
import br.com.will.classes.meli.checkout.core.application.exceptions.CupomNaoEncontrado;
import br.com.will.classes.meli.checkout.core.domain.Carrinho;
import br.com.will.classes.meli.checkout.core.domain.Cupom;
import br.com.will.classes.meli.checkout.core.domain.CupomService;
import br.com.will.classes.meli.checkout.core.infrastructure.CarrinhoRepository;
import br.com.will.classes.meli.checkout.core.infrastructure.CupomRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class AplicarCupomUseCase {

    private final CarrinhoRepository carrinhoRepository;
    private final CupomRepository cupomRepository;
    private final CupomService cupomService;
    private final Clock clock;

    public AplicarCupomUseCase(CarrinhoRepository carrinhoRepository,
                               CupomRepository cupomRepository,
                               CupomService cupomService,
                               Clock clock) {
        this.carrinhoRepository = carrinhoRepository;
        this.cupomRepository = cupomRepository;
        this.cupomService = cupomService;
        this.clock = clock;
    }

    public Carrinho aplicar(String carrinhoId, String codigoCupom) {
        Carrinho carrinho = carrinhoRepository.buscar(carrinhoId)
                .orElseThrow(() -> new CarrinhoNaoEncontrado(carrinhoId));

        Cupom cupom = cupomRepository.buscar(codigoCupom)
                .orElseThrow(() -> new CupomNaoEncontrado(codigoCupom));

        if (carrinho.temCupomAplicado() && carrinho.cupomAplicado().codigo().equals(codigoCupom)) {
            return carrinho;
        }

        LocalDate hoje = LocalDate.now(clock);
        var motivos = cupomService.validar(cupom, carrinho, hoje);
        if (!motivos.isEmpty()) {
            throw new CupomInvalido(motivos);
        }

        Carrinho atualizado = carrinho.aplicarCupom(cupom, cupom.desconto());
        return carrinhoRepository.salvar(atualizado);
    }
}