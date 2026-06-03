package br.com.will.classes.meli.checkout.core.api;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(properties = "checkout.fraude.simular-falhas=false")
class CarrinhoControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @PostConstruct
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void getCarrinhoExistenteRetorna200() throws Exception {
        mvc.perform(get("/carrinho/c-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("c-1"))
                .andExpect(jsonPath("$.valorBruto").value(250.00));
    }

    @Test
    void aplicarCupomValidoRetorna200ComDesconto() throws Exception {
        mvc.perform(post("/carrinho/c-1/cupom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"MELI10\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorFinal").value(225.00));
    }

    @Disabled("Habilitar após o Bloco 2 — GlobalExceptionHandler")
    @Test
    void carrinhoInexistenteRetorna404ComProblemDetail() throws Exception {
        mvc.perform(post("/carrinho/c-inexistente/cupom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"MELI10\"}"))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", "application/problem+json"))
                .andExpect(jsonPath("$.title").value("Carrinho não encontrado"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Disabled("Habilitar após o Bloco 2 — GlobalExceptionHandler")
    @Test
    void cupomExpiradoRetorna422ComMotivos() throws Exception {
        mvc.perform(post("/carrinho/c-1/cupom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"EXPIRADO\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", "application/problem+json"))
                .andExpect(jsonPath("$.title").value("Cupom inválido"))
                .andExpect(jsonPath("$.motivos").isArray());
    }

    @Disabled("Habilitar após o Bloco 1 — DTO + @Valid")
    @Test
    void codigoVazioRetorna400ComCampo() throws Exception {
        mvc.perform(post("/carrinho/c-1/cupom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].campo").value("codigo"));
    }

    @Disabled("Habilitar após o Bloco 3 — IdempotencyKeyFilter")
    @Test
    void chamadaRepetidaComMesmaIdempotencyKeyDevolveMesmoResultadoSemReaplicar() throws Exception {
        String key = "11111111-1111-1111-1111-111111111111";

        String primeiraResposta = mvc.perform(post("/carrinho/c-1/cupom")
                        .header("Idempotency-Key", key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"MELI10\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mvc.perform(post("/carrinho/c-1/cupom")
                        .header("Idempotency-Key", key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"MELI10\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(primeiraResposta));
    }
}