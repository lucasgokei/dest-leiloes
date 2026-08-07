package com.destleiloes.adapter.out.persistence.mapper;

import com.destleiloes.adapter.out.persistence.entity.AuctionJpaEntity;
import com.destleiloes.adapter.out.persistence.entity.UserJpaEntity;
import com.destleiloes.domain.model.Auction;

public final class AuctionEntityMapper {

    private AuctionEntityMapper() {}

    public static Auction toDomain(AuctionJpaEntity entity) {
        Auction auction = new Auction();
        auction.setId(entity.getId());
        auction.setTitle(entity.getTitle());
        auction.setDescription(entity.getDescription());
        auction.setImageUrl(entity.getImageUrl());
        auction.setStartingPrice(entity.getStartingPrice());
        auction.setCurrentPrice(entity.getCurrentPrice());
        auction.setStatus(entity.getStatus());
        auction.setCategory(entity.getCategory());
        auction.setStartsAt(entity.getStartsAt());
        auction.setEndsAt(entity.getEndsAt());
        auction.setCreatedAt(entity.getCreatedAt());
        auction.setSellerId(entity.getSeller().getId());
        auction.setWinnerId(entity.getWinner() != null ? entity.getWinner().getId() : null);
        return auction;
    }

    public static AuctionJpaEntity toEntity(Auction auction, UserJpaEntity sellerRef, UserJpaEntity winnerRef) {
        AuctionJpaEntity entity = new AuctionJpaEntity();
        entity.setId(auction.getId());
        entity.setTitle(auction.getTitle());
        entity.setDescription(auction.getDescription());
        entity.setImageUrl(auction.getImageUrl());
        entity.setStartingPrice(auction.getStartingPrice());
        entity.setCurrentPrice(auction.getCurrentPrice());
        entity.setStatus(auction.getStatus());
        entity.setCategory(auction.getCategory());
        entity.setStartsAt(auction.getStartsAt());
        entity.setEndsAt(auction.getEndsAt());
        entity.setCreatedAt(auction.getCreatedAt());
        entity.setSeller(sellerRef);
        entity.setWinner(winnerRef);
        return entity;
    }
}
