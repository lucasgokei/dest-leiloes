package com.destleiloes.adapter.out.messaging;

import com.destleiloes.application.port.out.AuctionEventPublisherPort;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuctionEventPublisherAdapter implements AuctionEventPublisherPort {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketAuctionEventPublisherAdapter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void publishNewBid(
            String auctionId, BigDecimal amount, BigDecimal currentPrice, String bidderName) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("type", "BID_NEW");
        event.put("auctionId", auctionId);
        event.put("amount", amount);
        event.put("currentPrice", currentPrice);
        event.put("bidderName", bidderName);
        event.put("createdAt", Instant.now().toString());
        send(auctionId, event);
    }

    @Override
    public void publishClosed(
            String auctionId, String winnerId, String winnerName, BigDecimal finalPrice) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("type", "AUCTION_CLOSED");
        event.put("auctionId", auctionId);
        event.put("winnerId", winnerId);
        event.put("winnerName", winnerName);
        event.put("finalPrice", finalPrice);
        send(auctionId, event);
    }

    @Override
    public void publishCancelled(String auctionId) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("type", "AUCTION_CANCELLED");
        event.put("auctionId", auctionId);
        send(auctionId, event);
    }

    private void send(String auctionId, Map<String, Object> event) {
        messagingTemplate.convertAndSend("/topic/auctions/" + auctionId, event);
    }
}
