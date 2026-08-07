package com.destleiloes.application.port.in.auction;

import java.util.List;

public interface ListActiveAuctionsUseCase {

    List<AuctionSummaryView> listActive();
}
