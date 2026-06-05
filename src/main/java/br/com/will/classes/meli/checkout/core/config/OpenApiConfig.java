package br.com.will.classes.meli.checkout.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI meliCheckoutOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Meli Checkout API")
                        .version("v1")
                        .description("API de carrinho e cupom — projeto da Aula 2 do PE-JV-003.")
                        .contact(new Contact().name("Ada Tech").url("https://ada.tech"))
                        .license(new License().name("Uso educacional")));
    }

    @Bean
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/carrinho/**")
                .build();
    }
}