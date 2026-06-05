package br.com.will.classes.meli.checkout.core.application;

import br.com.will.classes.meli.checkout.core.application.exceptions.CarrinhoNaoEncontrado;
import br.com.will.classes.meli.checkout.core.application.exceptions.CupomInvalido;
import br.com.will.classes.meli.checkout.core.application.exceptions.CupomNaoEncontrado;
import br.com.will.classes.meli.checkout.core.application.exceptions.ServicoIndisponivel;
import br.com.will.classes.meli.checkout.core.application.ports.CarrinhoRepositoryPort;
import br.com.will.classes.meli.checkout.core.application.ports.CupomRepositoryPort;
import br.com.will.classes.meli.checkout.core.application.ports.FraudeServicePort;
import br.com.will.classes.meli.checkout.core.domain.Carrinho;
import br.com.will.classes.meli.checkout.core.domain.Cupom;
import br.com.will.classes.meli.checkout.core.domain.CupomService;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class AplicarCupomUseCase {

    private final CarrinhoRepositoryPort carrinhoRepository;
    private final CupomRepositoryPort cupomRepository;
    private final CupomService cupomService;
    private final FraudeServicePort fraudeService;
    private final Clock clock;

    public AplicarCupomUseCase(CarrinhoRepositoryPort carrinhoRepository,
                               CupomRepositoryPort cupomRepository,
                               CupomService cupomService,
                               FraudeServicePort fraudeService,
                               Clock clock) {
        this.carrinhoRepository = carrinhoRepository;
        this.cupomRepository = cupomRepository;
        this.cupomService = cupomService;
        this.fraudeService = fraudeService;
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

        if (fraudeService.ehFraude(carrinhoId, carrinho.valorBruto())) {
            throw new ServicoIndisponivel(
                    "Falha no processamento: serviço de análise de fraude indisponível ou pedido suspeito");
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