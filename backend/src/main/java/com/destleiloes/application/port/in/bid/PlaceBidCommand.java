package com.destleiloes.application.port.in.bid;

import java.math.BigDecimal;

public record PlaceBidCommand(String auctionId, String bidderId, BigDecimal amount) {}
