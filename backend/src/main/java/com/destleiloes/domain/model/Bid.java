package com.destleiloes.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Bid {

    private String id;
    private BigDecimal amount;
    private Instant createdAt = Instant.now();
    private String auctionId;
    private String bidderId;
}
