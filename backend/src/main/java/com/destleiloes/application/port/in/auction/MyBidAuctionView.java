package com.destleiloes.application.port.in.auction;

import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;

public record MyBidAuctionView(String id, String title, AuctionStatus status, BigDecimal currentPrice, String winnerId) {}
