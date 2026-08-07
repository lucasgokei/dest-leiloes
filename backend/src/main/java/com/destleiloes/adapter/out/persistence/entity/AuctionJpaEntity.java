package com.destleiloes.adapter.out.persistence.entity;

import com.destleiloes.domain.model.AuctionCategory;
import com.destleiloes.domain.model.AuctionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Auction")
@Getter
@Setter
@NoArgsConstructor
public class AuctionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "TEXT")
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "startingPrice", nullable = false, precision = 12, scale = 2)
    private BigDecimal startingPrice;

    @Column(name = "currentPrice", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "\"AuctionStatus\"", nullable = false)
    private AuctionStatus status = AuctionStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "category", columnDefinition = "\"AuctionCategory\"", nullable = false)
    private AuctionCategory category = AuctionCategory.OTHERS;

    @Column(name = "startsAt", nullable = false)
    private Instant startsAt = Instant.now();

    @Column(name = "endsAt", nullable = false)
    private Instant endsAt;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId", nullable = false)
    private UserJpaEntity seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winnerId")
    private UserJpaEntity winner;
}
