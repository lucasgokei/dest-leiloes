package com.destleiloes.adapter.in.scheduler;

import com.destleiloes.application.port.in.auction.CloseExpiredAuctionsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuctionCloserScheduler {

    private final CloseExpiredAuctionsUseCase closeExpiredAuctionsUseCase;

    public AuctionCloserScheduler(CloseExpiredAuctionsUseCase closeExpiredAuctionsUseCase) {
        this.closeExpiredAuctionsUseCase = closeExpiredAuctionsUseCase;
    }

    @Scheduled(fixedDelay = 5000)
    public void closeExpiredAuctions() {
        closeExpiredAuctionsUseCase.closeExpiredAuctions();
    }
}
