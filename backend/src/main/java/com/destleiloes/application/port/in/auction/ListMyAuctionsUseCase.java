package com.destleiloes.application.port.in.auction;

import java.util.List;

public interface ListMyAuctionsUseCase {

    List<MyAuctionView> listMine(String sellerId);
}
