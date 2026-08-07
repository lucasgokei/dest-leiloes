package com.destleiloes.application.port.in.auction;

import com.destleiloes.domain.model.Auction;

public interface CreateAuctionUseCase {

    Auction create(CreateAuctionCommand command);
}
