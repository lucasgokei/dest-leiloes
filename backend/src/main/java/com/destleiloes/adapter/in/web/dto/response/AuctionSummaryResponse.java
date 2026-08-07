package com.destleiloes.adapter.in.web.dto.response;

import com.destleiloes.application.port.in.auction.AuctionSummaryView;
import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record AuctionSummaryResponse(
        String id,
        String title,
        String description,
        String imageUrl,
        BigDecimal startingPrice,
        BigDecimal currentPrice,
        AuctionStatus status,
        AuctionCategory category,
        Instant endsAt,
        long bidCount) {
    public static AuctionSummaryResponse from(AuctionSummaryView view) {
        return new AuctionSummaryResponse(
                view.id(),
                view.title(),
                view.description(),
                view.imageUrl(),
                view.startingPrice(),
                view.currentPrice(),
                view.status(),
                view.category(),
                view.endsAt(),
                view.bidCount());
    }
}
