package com.destleiloes.application.service;

import com.destleiloes.application.port.in.auction.CloseExpiredAuctionsUseCase;
import com.destleiloes.application.port.out.AuctionEventPublisherPort;
import com.destleiloes.application.port.out.AuctionRepositoryPort;
import com.destleiloes.application.port.out.BidRepositoryPort;
import com.destleiloes.application.port.out.UserRepositoryPort;
import com.destleiloes.domain.model.Auction;
import com.destleiloes.domain.model.AuctionStatus;
import com.destleiloes.domain.model.Bid;
import com.destleiloes.domain.model.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuctionCloserService implements CloseExpiredAuctionsUseCase {

    private static final Logger log = LoggerFactory.getLogger(AuctionCloserService.class);

    private final AuctionRepositoryPort auctionRepository;
    private final BidRepositoryPort bidRepository;
    private final UserRepositoryPort userRepository;
    private final AuctionEventPublisherPort eventPublisher;

    public AuctionCloserService(
            AuctionRepositoryPort auctionRepository,
            BidRepositoryPort bidRepository,
            UserRepositoryPort userRepository,
            AuctionEventPublisherPort eventPublisher) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void closeExpiredAuctions() {
        List<Auction> expired =
                auctionRepository.findByStatusAndEndsAtLessThanEqual(AuctionStatus.ACTIVE, Instant.now());

        for (Auction auction : expired) {
            try {
                closeAuction(auction);
            } catch (Exception e) {
                log.error("[auction-closer] falha ao fechar leilão {}", auction.getId(), e);
            }
        }
    }

    private void closeAuction(Auction auction) {
        Optional<Bid> topBid = bidRepository.findTopByAuctionIdOrderByAmountDesc(auction.getId());

        auction.setStatus(AuctionStatus.CLOSED);
        auction.setWinnerId(topBid.map(Bid::getBidderId).orElse(null));
        auctionRepository.save(auction);

        String winnerName =
                topBid.map(b -> userRepository.findById(b.getBidderId()).map(User::getName).orElse(null))
                        .orElse(null);

        eventPublisher.publishClosed(
                auction.getId(), topBid.map(Bid::getBidderId).orElse(null), winnerName, auction.getCurrentPrice());
    }
}
