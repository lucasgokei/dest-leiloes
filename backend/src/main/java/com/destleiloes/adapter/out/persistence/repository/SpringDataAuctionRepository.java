package com.destleiloes.adapter.out.persistence.repository;

import com.destleiloes.adapter.out.persistence.entity.AuctionJpaEntity;
import com.destleiloes.domain.model.AuctionStatus;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataAuctionRepository extends JpaRepository<AuctionJpaEntity, String> {

    List<AuctionJpaEntity> findByStatusOrderByEndsAtAsc(AuctionStatus status);

    List<AuctionJpaEntity> findBySellerIdOrderByCreatedAtDesc(String sellerId);

    List<AuctionJpaEntity> findAllByOrderByCreatedAtDesc();

    List<AuctionJpaEntity> findByStatusAndEndsAtLessThanEqual(AuctionStatus status, Instant now);

    @Query(
            "select a.seller.id as userId, count(a) as auctionCount "
                    + "from AuctionJpaEntity a where a.seller.id in :userIds group by a.seller.id")
    List<UserAuctionCount> countGroupedBySellerIds(@Param("userIds") List<String> userIds);

    interface UserAuctionCount {
        String getUserId();

        long getAuctionCount();
    }
}
