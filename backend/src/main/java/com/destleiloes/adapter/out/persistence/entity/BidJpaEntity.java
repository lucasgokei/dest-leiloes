package com.destleiloes.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Bid")
@Getter
@Setter
@NoArgsConstructor
public class BidJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "TEXT")
    private String id;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auctionId", nullable = false)
    private AuctionJpaEntity auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidderId", nullable = false)
    private UserJpaEntity bidder;
}
