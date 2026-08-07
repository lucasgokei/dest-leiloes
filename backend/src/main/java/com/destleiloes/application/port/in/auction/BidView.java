package com.destleiloes.application.port.in.auction;

import java.math.BigDecimal;
import java.time.Instant;

public record BidView(String id, BigDecimal amount, Instant createdAt, String bidderName) {}
