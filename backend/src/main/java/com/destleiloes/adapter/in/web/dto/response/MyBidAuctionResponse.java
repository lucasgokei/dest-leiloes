package com.destleiloes.adapter.in.web.dto.response;

import com.destleiloes.application.port.in.auction.MyBidAuctionView;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;

public record MyBidAuctionResponse(
        String id, String title, AuctionStatus status, BigDecimal currentPrice, String winnerId) {
    public static MyBidAuctionResponse from(MyBidAuctionView view) {
        return new MyBidAuctionResponse(
                view.id(), view.title(), view.status(), view.currentPrice(), view.winnerId());
    }
}
