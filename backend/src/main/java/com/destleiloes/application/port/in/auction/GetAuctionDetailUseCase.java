package com.destleiloes.application.port.in.auction;

public interface GetAuctionDetailUseCase {

    AuctionDetailView getDetail(String auctionId);
}
