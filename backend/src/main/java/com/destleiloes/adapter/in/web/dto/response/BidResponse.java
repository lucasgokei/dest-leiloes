package com.destleiloes.adapter.in.web.dto.response;

import com.destleiloes.application.port.in.auction.BidView;
import java.math.BigDecimal;
import java.time.Instant;

public record BidResponse(String id, BigDecimal amount, Instant createdAt, String bidderName) {
    public static BidResponse from(BidView view) {
        return new BidResponse(view.id(), view.amount(), view.createdAt(), view.bidderName());
    }
}
