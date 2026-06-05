package br.com.will.classes.meli.checkout.core.api;

import br.com.will.classes.meli.checkout.core.application.exceptions.CarrinhoNaoEncontrado;
import br.com.will.classes.meli.checkout.core.application.exceptions.CupomInvalido;
import br.com.will.classes.meli.checkout.core.application.exceptions.CupomNaoEncontrado;
import br.com.will.classes.meli.checkout.core.domain.CategoriaInvalida;
import br.com.will.classes.meli.checkout.core.domain.Expirado;
import br.com.will.classes.meli.checkout.core.domain.MotivoFalhaCupom;
import br.com.will.classes.meli.checkout.core.domain.ValorMinimo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CarrinhoNaoEncontrado.class)
    public ProblemDetail handleCarrinhoNaoEncontrado(CarrinhoNaoEncontrado ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Carrinho não encontrado");
        pd.setType(URI.create("https://meli.local/erros/carrinho-nao-encontrado"));
        pd.setProperty("carrinhoId", ex.getCarrinhoId());
        pd.setProperty("timestamp", Instant.now().toString());
        return pd;
    }

    @ExceptionHandler(CupomNaoEncontrado.class)
    public ProblemDetail handleCupomNaoEncontrado(CupomNaoEncontrado ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Cupom não encontrado");
        pd.setType(URI.create("https://meli.local/erros/cupom-nao-encontrado"));
        pd.setProperty("codigo", ex.getCodigo());
        pd.setProperty("timestamp", Instant.now().toString());
        return pd;
    }

    @ExceptionHandler(CupomInvalido.class)
    public ProblemDetail handleCupomInvalido(CupomInvalido ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY,
                "Cupom não atende às regras do carrinho");
        pd.setTitle("Cupom inválido");
        pd.setType(URI.create("https://meli.local/erros/cupom-invalido"));
        pd.setProperty("motivos", ex.getMotivos().stream().map(this::descrever).toList());
        pd.setProperty("timestamp", Instant.now().toString());
        return pd;
    }

    private String descrever(MotivoFalhaCupom motivo) {
        return switch (motivo) {
            case Expirado(LocalDate em) -> "Cupom expirado em " + em;
            case ValorMinimo(BigDecimal v) -> "Pedido abaixo do valor mínimo R$ " + v.toPlainString();
            case CategoriaInvalida(Set<String> el) -> "Categorias elegíveis: " + String.join(",", el);
        };
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidacao(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("campo", fe.getField(), "mensagem", fe.getDefaultMessage()))
                .toList();
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Payload com violações de validação");
        pd.setTitle("Requisição inválida");
        pd.setType(URI.create("https://meli.local/erros/validacao"));
        pd.setProperty("errors", errors);
        pd.setProperty("timestamp", Instant.now().toString());
        return pd;
    }
}