package com.destleiloes.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Auction {

    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private BigDecimal startingPrice;
    private BigDecimal currentPrice;
    private AuctionStatus status = AuctionStatus.ACTIVE;
    private AuctionCategory category = AuctionCategory.OTHERS;
    private Instant startsAt = Instant.now();
    private Instant endsAt;
    private Instant createdAt = Instant.now();
    private String sellerId;
    private String winnerId;
}
