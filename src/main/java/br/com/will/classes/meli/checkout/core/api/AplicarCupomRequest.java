package br.com.will.classes.meli.checkout.core.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AplicarCupomRequest(
        @NotBlank
        @Size(max = 20)
        @Pattern(regexp = "^[A-Z0-9-]+$", message = "Código deve conter apenas letras maiúsculas, números e hífen")
        String codigo
) {
}