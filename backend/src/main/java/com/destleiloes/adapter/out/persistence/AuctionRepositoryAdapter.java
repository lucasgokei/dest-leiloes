package com.destleiloes.adapter.out.persistence;

import com.destleiloes.adapter.out.persistence.entity.UserJpaEntity;
import com.destleiloes.adapter.out.persistence.mapper.AuctionEntityMapper;
import com.destleiloes.adapter.out.persistence.repository.SpringDataAuctionRepository;
import com.destleiloes.adapter.out.persistence.repository.SpringDataUserRepository;
import com.destleiloes.application.port.out.AuctionRepositoryPort;
import com.destleiloes.domain.model.Auction;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuctionRepositoryAdapter implements AuctionRepositoryPort {

    private final SpringDataAuctionRepository auctionJpaRepository;
    private final SpringDataUserRepository userJpaRepository;
    private final JdbcTemplate jdbcTemplate;

    public AuctionRepositoryAdapter(
            SpringDataAuctionRepository auctionJpaRepository,
            SpringDataUserRepository userJpaRepository,
            JdbcTemplate jdbcTemplate) {
        this.auctionJpaRepository = auctionJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Auction save(Auction auction) {
        UserJpaEntity sellerRef = userJpaRepository.getReferenceById(auction.getSellerId());
        UserJpaEntity winnerRef =
                auction.getWinnerId() != null ? userJpaRepository.getReferenceById(auction.getWinnerId()) : null;
        var entity = AuctionEntityMapper.toEntity(auction, sellerRef, winnerRef);
        return AuctionEntityMapper.toDomain(auctionJpaRepository.save(entity));
    }

    @Override
    public Optional<Auction> findById(String id) {
        return auctionJpaRepository.findById(id).map(AuctionEntityMapper::toDomain);
    }

    @Override
    public List<Auction> findByStatusOrderByEndsAtAsc(AuctionStatus status) {
        return auctionJpaRepository.findByStatusOrderByEndsAtAsc(status).stream()
                .map(AuctionEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Auction> findBySellerIdOrderByCreatedAtDesc(String sellerId) {
        return auctionJpaRepository.findBySellerIdOrderByCreatedAtDesc(sellerId).stream()
                .map(AuctionEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Auction> findAllByOrderByCreatedAtDesc() {
        return auctionJpaRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(AuctionEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Auction> findByStatusAndEndsAtLessThanEqual(AuctionStatus status, Instant now) {
        return auctionJpaRepository.findByStatusAndEndsAtLessThanEqual(status, now).stream()
                .map(AuctionEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Map<String, Long> countBySellerIds(List<String> sellerIds) {
        return auctionJpaRepository.countGroupedBySellerIds(sellerIds).stream()
                .collect(
                        Collectors.toMap(
                                SpringDataAuctionRepository.UserAuctionCount::getUserId,
                                SpringDataAuctionRepository.UserAuctionCount::getAuctionCount));
    }

    @Override
    public void deleteById(String id) {
        auctionJpaRepository.deleteById(id);
    }

    @Override
    public Optional<BigDecimal> updateCurrentPriceIfHigher(String auctionId, BigDecimal newAmount) {
        List<Map<String, Object>> updated =
                jdbcTemplate.queryForList(
                        "UPDATE \"Auction\" SET \"currentPrice\" = ? "
                                + "WHERE id = ? AND status = 'ACTIVE' AND \"endsAt\" > now() AND \"currentPrice\" < ? "
                                + "RETURNING id, \"currentPrice\"",
                        newAmount,
                        auctionId,
                        newAmount);
        if (updated.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of((BigDecimal) updated.get(0).get("currentPrice"));
    }
}
