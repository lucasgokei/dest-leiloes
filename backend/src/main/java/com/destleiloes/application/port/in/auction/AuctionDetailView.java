package com.destleiloes.application.port.in.auction;

import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record AuctionDetailView(
        String id,
        String title,
        String description,
        String imageUrl,
        BigDecimal startingPrice,
        BigDecimal currentPrice,
        AuctionStatus status,
        AuctionCategory category,
        Instant endsAt,
        AuctionParticipantView seller,
        AuctionParticipantView winner,
        List<BidView> bids) {}
