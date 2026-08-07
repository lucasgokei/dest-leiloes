package com.destleiloes.adapter.in.web;

import com.destleiloes.adapter.in.web.dto.request.BidRequest;
import com.destleiloes.adapter.in.web.dto.request.CreateAuctionRequest;
import com.destleiloes.adapter.in.web.dto.response.AuctionDetailResponse;
import com.destleiloes.adapter.in.web.dto.response.AuctionSummaryResponse;
import com.destleiloes.adapter.in.web.security.AuthenticatedUser;
import com.destleiloes.application.port.in.auction.CreateAuctionCommand;
import com.destleiloes.application.port.in.auction.CreateAuctionUseCase;
import com.destleiloes.application.port.in.auction.GetAuctionDetailUseCase;
import com.destleiloes.application.port.in.auction.ListActiveAuctionsUseCase;
import com.destleiloes.application.port.in.bid.PlaceBidCommand;
import com.destleiloes.application.port.in.bid.PlaceBidUseCase;
import com.destleiloes.domain.model.Auction;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final ListActiveAuctionsUseCase listActiveAuctionsUseCase;
    private final GetAuctionDetailUseCase getAuctionDetailUseCase;
    private final CreateAuctionUseCase createAuctionUseCase;
    private final PlaceBidUseCase placeBidUseCase;

    public AuctionController(
            ListActiveAuctionsUseCase listActiveAuctionsUseCase,
            GetAuctionDetailUseCase getAuctionDetailUseCase,
            CreateAuctionUseCase createAuctionUseCase,
            PlaceBidUseCase placeBidUseCase) {
        this.listActiveAuctionsUseCase = listActiveAuctionsUseCase;
        this.getAuctionDetailUseCase = getAuctionDetailUseCase;
        this.createAuctionUseCase = createAuctionUseCase;
        this.placeBidUseCase = placeBidUseCase;
    }

    @GetMapping
    public List<AuctionSummaryResponse> list() {
        return listActiveAuctionsUseCase.listActive().stream().map(AuctionSummaryResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AuctionDetailResponse detail(@PathVariable String id) {
        return AuctionDetailResponse.from(getAuctionDetailUseCase.getDetail(id));
    }

    @PostMapping
    public ResponseEntity<AuctionDetailResponse> create(
            @AuthenticationPrincipal AuthenticatedUser principal,
            @Valid @RequestBody CreateAuctionRequest request) {
        Auction auction =
                createAuctionUseCase.create(
                        new CreateAuctionCommand(
                                principal.userId(),
                                request.title(),
                                request.description(),
                                request.imageUrl(),
                                request.startingPrice(),
                                request.durationMinutes(),
                                request.category()));
        return ResponseEntity.ok(AuctionDetailResponse.from(getAuctionDetailUseCase.getDetail(auction.getId())));
    }

    @PostMapping("/{id}/bids")
    public ResponseEntity<Void> placeBid(
            @AuthenticationPrincipal AuthenticatedUser principal,
            @PathVariable String id,
            @RequestBody BidRequest request) {
        placeBidUseCase.placeBid(new PlaceBidCommand(id, principal.userId(), request.amount()));
        return ResponseEntity.ok().build();
    }
}
