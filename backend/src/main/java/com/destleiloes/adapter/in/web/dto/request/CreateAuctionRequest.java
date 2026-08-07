package com.destleiloes.adapter.in.web.dto.request;

import com.destleiloes.domain.model.AuctionCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.URL;

public record CreateAuctionRequest(
        @NotBlank(message = "Título deve ter ao menos 3 caracteres.")
                @Size(min = 3, max = 120, message = "Título deve ter ao menos 3 caracteres.")
                String title,
        @NotBlank(message = "Descrição deve ter ao menos 10 caracteres.")
                @Size(min = 10, max = 2000, message = "Descrição deve ter ao menos 10 caracteres.")
                String description,
        @URL(message = "URL de imagem inválida.") String imageUrl,
        @NotNull(message = "Informe um valor numérico.")
                @DecimalMin(value = "0.0", inclusive = false, message = "O valor inicial deve ser maior que zero.")
                BigDecimal startingPrice,
        @NotNull(message = "Informe a duração em minutos.")
                @Min(value = 1, message = "A duração mínima é de 1 minuto.")
                @Max(value = 60 * 24 * 30, message = "A duração máxima é de 30 dias.")
                Integer durationMinutes,
        @NotNull(message = "Selecione uma categoria.") AuctionCategory category) {}
