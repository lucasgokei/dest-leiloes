package com.destleiloes.adapter.in.web.dto.response;

import com.destleiloes.application.port.in.auction.AuctionDetailView;
import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record AuctionDetailResponse(
        String id,
        String title,
        String description,
        String imageUrl,
        BigDecimal startingPrice,
        BigDecimal currentPrice,
        AuctionStatus status,
        AuctionCategory category,
        Instant endsAt,
        SimpleUserResponse seller,
        SimpleUserResponse winner,
        List<BidResponse> bids) {
    public static AuctionDetailResponse from(AuctionDetailView view) {
        return new AuctionDetailResponse(
                view.id(),
                view.title(),
                view.description(),
                view.imageUrl(),
                view.startingPrice(),
                view.currentPrice(),
                view.status(),
                view.category(),
                view.endsAt(),
                SimpleUserResponse.from(view.seller()),
                view.winner() != null ? SimpleUserResponse.from(view.winner()) : null,
                view.bids().stream().map(BidResponse::from).toList());
    }
}
