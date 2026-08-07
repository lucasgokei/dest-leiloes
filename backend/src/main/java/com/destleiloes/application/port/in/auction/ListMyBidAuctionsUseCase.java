package com.destleiloes.application.port.in.auction;

import java.util.List;

public interface ListMyBidAuctionsUseCase {

    List<MyBidAuctionView> listMyBidAuctions(String bidderId);
}
