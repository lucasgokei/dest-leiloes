package com.destleiloes.adapter.in.web.dto.response;

import com.destleiloes.application.port.in.auction.MyAuctionView;
import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record MyAuctionResponse(
        String id,
        String title,
        AuctionStatus status,
        AuctionCategory category,
        BigDecimal currentPrice,
        long bidCount,
        Instant endsAt) {
    public static MyAuctionResponse from(MyAuctionView view) {
        return new MyAuctionResponse(
                view.id(),
                view.title(),
                view.status(),
                view.category(),
                view.currentPrice(),
                view.bidCount(),
                view.endsAt());
    }
}
