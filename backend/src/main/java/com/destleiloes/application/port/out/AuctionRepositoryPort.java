package com.destleiloes.application.port.out;

import com.destleiloes.domain.model.Auction;
import com.destleiloes.domain.model.AuctionStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuctionRepositoryPort {

    Auction save(Auction auction);

    Optional<Auction> findById(String id);

    List<Auction> findByStatusOrderByEndsAtAsc(AuctionStatus status);

    List<Auction> findBySellerIdOrderByCreatedAtDesc(String sellerId);

    List<Auction> findAllByOrderByCreatedAtDesc();

    List<Auction> findByStatusAndEndsAtLessThanEqual(AuctionStatus status, Instant now);

    Map<String, Long> countBySellerIds(List<String> sellerIds);

    void deleteById(String id);

    /**
     * Atualiza currentPrice apenas se o leilão ainda estiver ACTIVE, dentro do prazo, e o novo
     * valor for maior que o preço atual no momento do commit — protege contra lances concorrentes.
     * Retorna o novo preço quando a condição é satisfeita, vazio caso contrário.
     */
    Optional<BigDecimal> updateCurrentPriceIfHigher(String auctionId, BigDecimal newAmount);
}
