package com.destleiloes.application.service;

import com.destleiloes.application.port.in.bid.PlaceBidCommand;
import com.destleiloes.application.port.in.bid.PlaceBidUseCase;
import com.destleiloes.application.port.out.AuctionEventPublisherPort;
import com.destleiloes.application.port.out.AuctionRepositoryPort;
import com.destleiloes.application.port.out.BidRepositoryPort;
import com.destleiloes.application.port.out.UserRepositoryPort;
import com.destleiloes.domain.exception.BadRequestException;
import com.destleiloes.domain.exception.ConflictException;
import com.destleiloes.domain.exception.ForbiddenException;
import com.destleiloes.domain.exception.NotFoundException;
import com.destleiloes.domain.model.Auction;
import com.destleiloes.domain.model.Bid;
import com.destleiloes.domain.model.User;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * O UPDATE condicional (AuctionRepositoryPort#updateCurrentPriceIfHigher) replica o mesmo
 * mecanismo de proteção contra corrida entre lances simultâneos que existia no lado
 * Next/Prisma: só grava se o leilão ainda estiver ativo, dentro do prazo, e o novo lance for
 * realmente maior que o preço atual no momento do commit.
 */
@Service
public class BidService implements PlaceBidUseCase {

    private final AuctionRepositoryPort auctionRepository;
    private final BidRepositoryPort bidRepository;
    private final UserRepositoryPort userRepository;
    private final AuctionEventPublisherPort eventPublisher;

    public BidService(
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
    public void placeBid(PlaceBidCommand command) {
        BigDecimal amount = command.amount();
        if (amount == null || amount.signum() <= 0) {
            throw new BadRequestException("Valor de lance inválido.");
        }

        Auction auction =
                auctionRepository
                        .findById(command.auctionId())
                        .orElseThrow(() -> new NotFoundException("Leilão não encontrado."));

        if (auction.getSellerId().equals(command.bidderId())) {
            throw new ForbiddenException("Você não pode dar lances no seu próprio leilão.");
        }

        BigDecimal currentPrice =
                auctionRepository
                        .updateCurrentPriceIfHigher(command.auctionId(), amount)
                        .orElseThrow(
                                () ->
                                        new ConflictException(
                                                "Lance recusado: o leilão foi encerrado ou já recebeu um lance maior. Atualize a página."));

        Bid bid = new Bid();
        bid.setAuctionId(command.auctionId());
        bid.setBidderId(command.bidderId());
        bid.setAmount(amount);
        bidRepository.save(bid);

        User bidder =
                userRepository
                        .findById(command.bidderId())
                        .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        eventPublisher.publishNewBid(command.auctionId(), amount, currentPrice, bidder.getName());
    }
}
