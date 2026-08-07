package com.destleiloes.application.service;

import com.destleiloes.application.port.in.auction.AuctionDetailView;
import com.destleiloes.application.port.in.auction.AuctionParticipantView;
import com.destleiloes.application.port.in.auction.AuctionSummaryView;
import com.destleiloes.application.port.in.auction.BidView;
import com.destleiloes.application.port.in.auction.CreateAuctionCommand;
import com.destleiloes.application.port.in.auction.CreateAuctionUseCase;
import com.destleiloes.application.port.in.auction.GetAuctionDetailUseCase;
import com.destleiloes.application.port.in.auction.ListActiveAuctionsUseCase;
import com.destleiloes.application.port.in.auction.ListMyAuctionsUseCase;
import com.destleiloes.application.port.in.auction.ListMyBidAuctionsUseCase;
import com.destleiloes.application.port.in.auction.MyAuctionView;
import com.destleiloes.application.port.in.auction.MyBidAuctionView;
import com.destleiloes.application.port.out.AuctionRepositoryPort;
import com.destleiloes.application.port.out.BidRepositoryPort;
import com.destleiloes.application.port.out.UserRepositoryPort;
import com.destleiloes.domain.exception.NotFoundException;
import com.destleiloes.domain.model.Auction;
import com.destleiloes.domain.model.AuctionStatus;
import com.destleiloes.domain.model.Bid;
import com.destleiloes.domain.model.User;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuctionService
        implements ListActiveAuctionsUseCase,
                GetAuctionDetailUseCase,
                CreateAuctionUseCase,
                ListMyAuctionsUseCase,
                ListMyBidAuctionsUseCase {

    private final AuctionRepositoryPort auctionRepository;
    private final BidRepositoryPort bidRepository;
    private final UserRepositoryPort userRepository;

    public AuctionService(
            AuctionRepositoryPort auctionRepository,
            BidRepositoryPort bidRepository,
            UserRepositoryPort userRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionSummaryView> listActive() {
        List<Auction> auctions = auctionRepository.findByStatusOrderByEndsAtAsc(AuctionStatus.ACTIVE);
        Map<String, Long> counts = bidCounts(auctions);
        return auctions.stream()
                .map(a -> new AuctionSummaryView(
                        a.getId(),
                        a.getTitle(),
                        a.getDescription(),
                        a.getImageUrl(),
                        a.getStartingPrice(),
                        a.getCurrentPrice(),
                        a.getStatus(),
                        a.getCategory(),
                        a.getEndsAt(),
                        counts.getOrDefault(a.getId(), 0L)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuctionDetailView getDetail(String id) {
        Auction auction = findAuctionOrThrow(id);
        List<Bid> bids = bidRepository.findByAuctionIdOrderByCreatedAtDesc(id);
        Map<String, User> biddersById = usersById(bids.stream().map(Bid::getBidderId).toList());
        List<BidView> bidViews =
                bids.stream()
                        .map(b -> new BidView(
                                b.getId(), b.getAmount(), b.getCreatedAt(), biddersById.get(b.getBidderId()).getName()))
                        .toList();

        AuctionParticipantView sellerView = toParticipantView(userOrThrow(auction.getSellerId(), "Vendedor não encontrado."));
        AuctionParticipantView winnerView =
                auction.getWinnerId() != null
                        ? toParticipantView(userOrThrow(auction.getWinnerId(), "Vencedor não encontrado."))
                        : null;

        return new AuctionDetailView(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getImageUrl(),
                auction.getStartingPrice(),
                auction.getCurrentPrice(),
                auction.getStatus(),
                auction.getCategory(),
                auction.getEndsAt(),
                sellerView,
                winnerView,
                bidViews);
    }

    @Override
    @Transactional
    public Auction create(CreateAuctionCommand command) {
        Instant startsAt = Instant.now();
        Instant endsAt = startsAt.plus(Duration.ofMinutes(command.durationMinutes()));

        Auction auction = new Auction();
        auction.setTitle(command.title());
        auction.setDescription(command.description());
        auction.setImageUrl(
                command.imageUrl() == null || command.imageUrl().isBlank() ? null : command.imageUrl());
        auction.setStartingPrice(command.startingPrice());
        auction.setCurrentPrice(command.startingPrice());
        auction.setStartsAt(startsAt);
        auction.setEndsAt(endsAt);
        auction.setSellerId(command.sellerId());
        auction.setCategory(command.category());

        return auctionRepository.save(auction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyAuctionView> listMine(String sellerId) {
        List<Auction> auctions = auctionRepository.findBySellerIdOrderByCreatedAtDesc(sellerId);
        Map<String, Long> counts = bidCounts(auctions);
        return auctions.stream()
                .map(a -> new MyAuctionView(
                        a.getId(),
                        a.getTitle(),
                        a.getStatus(),
                        a.getCategory(),
                        a.getCurrentPrice(),
                        counts.getOrDefault(a.getId(), 0L),
                        a.getEndsAt()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyBidAuctionView> listMyBidAuctions(String bidderId) {
        List<Bid> bids = bidRepository.findByBidderIdOrderByCreatedAtDesc(bidderId);
        Set<String> auctionIdsInOrder = new LinkedHashSet<>();
        for (Bid bid : bids) {
            auctionIdsInOrder.add(bid.getAuctionId());
        }
        return auctionIdsInOrder.stream()
                .map(this::findAuctionOrThrow)
                .map(a -> new MyBidAuctionView(a.getId(), a.getTitle(), a.getStatus(), a.getCurrentPrice(), a.getWinnerId()))
                .toList();
    }

    private Auction findAuctionOrThrow(String id) {
        return auctionRepository.findById(id).orElseThrow(() -> new NotFoundException("Leilão não encontrado."));
    }

    private User userOrThrow(String id, String message) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(message));
    }

    private AuctionParticipantView toParticipantView(User user) {
        return new AuctionParticipantView(user.getId(), user.getName());
    }

    private Map<String, Long> bidCounts(List<Auction> auctions) {
        if (auctions.isEmpty()) {
            return Map.of();
        }
        List<String> ids = auctions.stream().map(Auction::getId).toList();
        return bidRepository.countByAuctionIds(ids);
    }

    private Map<String, User> usersById(List<String> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }
        return userRepository.findAllById(ids.stream().distinct().toList()).stream()
                .collect(Collectors.toMap(User::getId, u -> u));
    }
}
