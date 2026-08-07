package com.destleiloes.adapter.in.web.dto.response;

import com.destleiloes.application.port.in.admin.AdminAuctionView;
import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record AdminAuctionResponse(
        String id,
        String title,
        String sellerName,
        AuctionStatus status,
        AuctionCategory category,
        BigDecimal currentPrice,
        long bidCount,
        Instant endsAt) {
    public static AdminAuctionResponse from(AdminAuctionView view) {
        return new AdminAuctionResponse(
                view.id(),
                view.title(),
                view.sellerName(),
                view.status(),
                view.category(),
                view.currentPrice(),
                view.bidCount(),
                view.endsAt());
    }
}
