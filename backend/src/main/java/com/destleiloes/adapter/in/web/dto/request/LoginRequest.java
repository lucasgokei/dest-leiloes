package com.destleiloes.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Informe um e-mail válido.") @Email(message = "Informe um e-mail válido.")
                String email,
        @NotBlank(message = "Informe a senha.") String password) {}
