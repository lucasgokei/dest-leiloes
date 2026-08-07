package com.destleiloes.adapter.out.persistence;

import com.destleiloes.adapter.out.persistence.entity.AuctionJpaEntity;
import com.destleiloes.adapter.out.persistence.entity.UserJpaEntity;
import com.destleiloes.adapter.out.persistence.mapper.BidEntityMapper;
import com.destleiloes.adapter.out.persistence.repository.SpringDataAuctionRepository;
import com.destleiloes.adapter.out.persistence.repository.SpringDataBidRepository;
import com.destleiloes.adapter.out.persistence.repository.SpringDataUserRepository;
import com.destleiloes.application.port.out.BidRepositoryPort;
import com.destleiloes.domain.model.Bid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BidRepositoryAdapter implements BidRepositoryPort {

    private final SpringDataBidRepository bidJpaRepository;
    private final SpringDataAuctionRepository auctionJpaRepository;
    private final SpringDataUserRepository userJpaRepository;

    public BidRepositoryAdapter(
            SpringDataBidRepository bidJpaRepository,
            SpringDataAuctionRepository auctionJpaRepository,
            SpringDataUserRepository userJpaRepository) {
        this.bidJpaRepository = bidJpaRepository;
        this.auctionJpaRepository = auctionJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Bid save(Bid bid) {
        AuctionJpaEntity auctionRef = auctionJpaRepository.getReferenceById(bid.getAuctionId());
        UserJpaEntity bidderRef = userJpaRepository.getReferenceById(bid.getBidderId());
        var entity = BidEntityMapper.toEntity(bid, auctionRef, bidderRef);
        return BidEntityMapper.toDomain(bidJpaRepository.save(entity));
    }

    @Override
    public List<Bid> findByAuctionIdOrderByCreatedAtDesc(String auctionId) {
        return bidJpaRepository.findByAuctionIdOrderByCreatedAtDesc(auctionId).stream()
                .map(BidEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Bid> findByBidderIdOrderByCreatedAtDesc(String bidderId) {
        return bidJpaRepository.findByBidderIdOrderByCreatedAtDesc(bidderId).stream()
                .map(BidEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Bid> findTopByAuctionIdOrderByAmountDesc(String auctionId) {
        return bidJpaRepository.findTop1ByAuctionIdOrderByAmountDesc(auctionId).stream()
                .findFirst()
                .map(BidEntityMapper::toDomain);
    }

    @Override
    public Map<String, Long> countByAuctionIds(List<String> auctionIds) {
        return bidJpaRepository.countGroupedByAuctionIds(auctionIds).stream()
                .collect(
                        Collectors.toMap(
                                SpringDataBidRepository.AuctionBidCount::getAuctionId,
                                SpringDataBidRepository.AuctionBidCount::getBidCount));
    }

    @Override
    public Map<String, Long> countByBidderIds(List<String> bidderIds) {
        return bidJpaRepository.countGroupedByBidderIds(bidderIds).stream()
                .collect(
                        Collectors.toMap(
                                SpringDataBidRepository.UserBidCount::getUserId,
                                SpringDataBidRepository.UserBidCount::getBidCount));
    }
}
