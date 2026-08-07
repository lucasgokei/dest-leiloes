package com.destleiloes.application.port.in.auction;

import com.destleiloes.domain.model.AuctionCategory;
import java.math.BigDecimal;

public record CreateAuctionCommand(
        String sellerId,
        String title,
        String description,
        String imageUrl,
        BigDecimal startingPrice,
        Integer durationMinutes,
        AuctionCategory category) {}
