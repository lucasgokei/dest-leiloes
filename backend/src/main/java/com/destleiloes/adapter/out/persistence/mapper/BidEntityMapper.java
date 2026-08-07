package com.destleiloes.adapter.out.persistence.mapper;

import com.destleiloes.adapter.out.persistence.entity.AuctionJpaEntity;
import com.destleiloes.adapter.out.persistence.entity.BidJpaEntity;
import com.destleiloes.adapter.out.persistence.entity.UserJpaEntity;
import com.destleiloes.domain.model.Bid;

public final class BidEntityMapper {

    private BidEntityMapper() {}

    public static Bid toDomain(BidJpaEntity entity) {
        Bid bid = new Bid();
        bid.setId(entity.getId());
        bid.setAmount(entity.getAmount());
        bid.setCreatedAt(entity.getCreatedAt());
        bid.setAuctionId(entity.getAuction().getId());
        bid.setBidderId(entity.getBidder().getId());
        return bid;
    }

    public static BidJpaEntity toEntity(Bid bid, AuctionJpaEntity auctionRef, UserJpaEntity bidderRef) {
        BidJpaEntity entity = new BidJpaEntity();
        entity.setId(bid.getId());
        entity.setAmount(bid.getAmount());
        entity.setCreatedAt(bid.getCreatedAt());
        entity.setAuction(auctionRef);
        entity.setBidder(bidderRef);
        return entity;
    }
}
