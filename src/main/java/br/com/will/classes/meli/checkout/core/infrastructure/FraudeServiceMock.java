package br.com.will.classes.meli.checkout.core.infrastructure;

import br.com.will.classes.meli.checkout.core.application.ports.FraudeServicePort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FraudeServiceMock implements FraudeServicePort {

    private static final Logger log = LoggerFactory.getLogger(FraudeServiceMock.class);
    private static final BigDecimal LIMITE_FALLBACK = new BigDecimal("200");

    @Override
    @CircuitBreaker(name = "fraudeService", fallbackMethod = "ehFraudeFallback")
    public boolean ehFraude(String carrinhoId, BigDecimal valorBruto) {
        double aleatorio = Math.random();

        // Simula instabilidade do serviço externo: 30% de chance de falha
        if (aleatorio < 0.3) {
            log.warn("[FraudeService] Falha simulada para carrinho={}", carrinhoId);
            throw new RuntimeException("Serviço de análise de fraude indisponível");
        }

        // Condição aleatória para indicar fraude
        boolean fraude = aleatorio < 0.5;
        log.info("[FraudeService] Análise concluída: carrinho={}, fraude={}", carrinhoId, fraude);
        return fraude;
    }

    /**
     * Fallback acionado quando o circuit breaker está aberto ou quando o serviço falha.
     * Estratégia conservadora: considera fraude para pedidos acima de R$ 200,00.
     */
    public boolean ehFraudeFallback(String carrinhoId, BigDecimal valorBruto, Throwable cause) {
        log.warn("[FraudeService] Fallback acionado: carrinho={}, causa={}", carrinhoId, cause.getMessage());
        boolean fraude = valorBruto.compareTo(LIMITE_FALLBACK) > 0;
        log.warn("[FraudeService] Fallback resultado: carrinho={}, valorBruto={}, fraude={}", carrinhoId, valorBruto, fraude);
        return fraude;
    }
}

