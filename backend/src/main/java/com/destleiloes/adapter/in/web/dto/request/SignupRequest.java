package com.destleiloes.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "Nome deve ter ao menos 2 caracteres.")
                @Size(min = 2, message = "Nome deve ter ao menos 2 caracteres.")
                String name,
        @NotBlank(message = "Informe um e-mail válido.")
                @Email(message = "Informe um e-mail válido.")
                String email,
        @NotBlank(message = "Deve ter ao menos 8 caracteres.")
                @Size(min = 8, message = "Deve ter ao menos 8 caracteres.")
                @Pattern(
                        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).*$",
                        message = "Deve conter ao menos uma letra e um número.")
                String password) {}
