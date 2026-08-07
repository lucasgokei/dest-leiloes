package com.destleiloes.adapter.out.persistence.repository;

import com.destleiloes.adapter.out.persistence.entity.BidJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataBidRepository extends JpaRepository<BidJpaEntity, String> {

    List<BidJpaEntity> findByAuctionIdOrderByCreatedAtDesc(String auctionId);

    List<BidJpaEntity> findByBidderIdOrderByCreatedAtDesc(String bidderId);

    List<BidJpaEntity> findTop1ByAuctionIdOrderByAmountDesc(String auctionId);

    @Query(
            "select b.auction.id as auctionId, count(b) as bidCount "
                    + "from BidJpaEntity b where b.auction.id in :auctionIds group by b.auction.id")
    List<AuctionBidCount> countGroupedByAuctionIds(@Param("auctionIds") List<String> auctionIds);

    interface AuctionBidCount {
        String getAuctionId();

        long getBidCount();
    }

    @Query(
            "select b.bidder.id as userId, count(b) as bidCount "
                    + "from BidJpaEntity b where b.bidder.id in :userIds group by b.bidder.id")
    List<UserBidCount> countGroupedByBidderIds(@Param("userIds") List<String> userIds);

    interface UserBidCount {
        String getUserId();

        long getBidCount();
    }
}
