package com.destleiloes.application.port.in.auction;

import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record AuctionSummaryView(
        String id,
        String title,
        String description,
        String imageUrl,
        BigDecimal startingPrice,
        BigDecimal currentPrice,
        AuctionStatus status,
        AuctionCategory category,
        Instant endsAt,
        long bidCount) {}
