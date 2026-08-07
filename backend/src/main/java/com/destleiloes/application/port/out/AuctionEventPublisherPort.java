package com.destleiloes.application.port.out;

import java.math.BigDecimal;

public interface AuctionEventPublisherPort {

    void publishNewBid(String auctionId, BigDecimal amount, BigDecimal currentPrice, String bidderName);

    void publishClosed(String auctionId, String winnerId, String winnerName, BigDecimal finalPrice);

    void publishCancelled(String auctionId);
}
