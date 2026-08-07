package com.destleiloes.application.port.in.admin;

import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record AdminAuctionView(
        String id,
        String title,
        String sellerName,
        AuctionStatus status,
        AuctionCategory category,
        BigDecimal currentPrice,
        long bidCount,
        Instant endsAt) {}
