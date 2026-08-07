package com.destleiloes.application.port.in.auction;

import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record MyAuctionView(
        String id,
        String title,
        AuctionStatus status,
        AuctionCategory category,
        BigDecimal currentPrice,
        long bidCount,
        Instant endsAt) {}
