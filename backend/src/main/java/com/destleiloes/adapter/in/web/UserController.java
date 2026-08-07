package com.destleiloes.adapter.in.web;

import com.destleiloes.adapter.in.web.dto.response.MyAuctionResponse;
import com.destleiloes.adapter.in.web.dto.response.MyBidAuctionResponse;
import com.destleiloes.adapter.in.web.dto.response.UserResponse;
import com.destleiloes.adapter.in.web.security.AuthenticatedUser;
import com.destleiloes.application.port.in.auction.ListMyAuctionsUseCase;
import com.destleiloes.application.port.in.auction.ListMyBidAuctionsUseCase;
import com.destleiloes.application.port.in.auth.GetUserProfileUseCase;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final ListMyAuctionsUseCase listMyAuctionsUseCase;
    private final ListMyBidAuctionsUseCase listMyBidAuctionsUseCase;

    public UserController(
            GetUserProfileUseCase getUserProfileUseCase,
            ListMyAuctionsUseCase listMyAuctionsUseCase,
            ListMyBidAuctionsUseCase listMyBidAuctionsUseCase) {
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.listMyAuctionsUseCase = listMyAuctionsUseCase;
        this.listMyBidAuctionsUseCase = listMyBidAuctionsUseCase;
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal AuthenticatedUser principal) {
        return UserResponse.from(getUserProfileUseCase.getById(principal.userId()));
    }

    @GetMapping("/me/auctions")
    public List<MyAuctionResponse> myAuctions(@AuthenticationPrincipal AuthenticatedUser principal) {
        return listMyAuctionsUseCase.listMine(principal.userId()).stream().map(MyAuctionResponse::from).toList();
    }

    @GetMapping("/me/bids")
    public List<MyBidAuctionResponse> myBids(@AuthenticationPrincipal AuthenticatedUser principal) {
        return listMyBidAuctionsUseCase.listMyBidAuctions(principal.userId()).stream()
                .map(MyBidAuctionResponse::from)
                .toList();
    }
}
