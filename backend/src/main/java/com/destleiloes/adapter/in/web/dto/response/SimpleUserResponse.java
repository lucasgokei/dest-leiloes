package com.destleiloes.adapter.in.web.dto.response;

import com.destleiloes.application.port.in.auction.AuctionParticipantView;

public record SimpleUserResponse(String id, String name) {
    public static SimpleUserResponse from(AuctionParticipantView view) {
        return new SimpleUserResponse(view.id(), view.name());
    }
}
