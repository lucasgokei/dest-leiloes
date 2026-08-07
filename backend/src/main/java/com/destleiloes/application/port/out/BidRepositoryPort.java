package com.destleiloes.application.port.out;

import com.destleiloes.domain.model.Bid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BidRepositoryPort {

    Bid save(Bid bid);

    List<Bid> findByAuctionIdOrderByCreatedAtDesc(String auctionId);

    List<Bid> findByBidderIdOrderByCreatedAtDesc(String bidderId);

    Optional<Bid> findTopByAuctionIdOrderByAmountDesc(String auctionId);

    Map<String, Long> countByAuctionIds(List<String> auctionIds);

    Map<String, Long> countByBidderIds(List<String> bidderIds);
}
