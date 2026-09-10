package com.rfq.auction_service;

import static org.junit.jupiter.api.Assertions.*;

import com.rfq.auction_service.dto.BidEventRequest;
import com.rfq.auction_service.dto.CreateAuctionRequest;
import com.rfq.auction_service.service.AuctionService;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuctionExtensionRulesTest {
    @Autowired AuctionService service;

    @Test void eachConfiguredEventTypeExtendsInsideWindow() {
        assertExtended(true, false, false, true, false, false);
        assertExtended(false, true, false, false, true, false);
        assertExtended(false, false, true, false, false, true);
    }

    @Test void triggerWindowStartsInclusivelyAndCloseIsExclusive() {
        Instant now = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MILLIS);
        UUID id = create(now, 5, 2, true, false, false, 20, 60);
        Map<String, Object> atBoundary = service.processEvent(id, new BidEventRequest(UUID.randomUUID(), now.plusSeconds(15), true, false, false));
        assertEquals(true, atBoundary.get("extended"));
        UUID closeId = create(now, 5, 2, true, false, false, 20, 60);
        Map<String, Object> atClose = service.processEvent(closeId, new BidEventRequest(UUID.randomUUID(), now.plusSeconds(20), true, false, false));
        assertEquals(false, atClose.get("extended"));
    }

    @Test void qualifyingEventsCanExtendRepeatedlyButNeverPastForcedClose() {
        Instant now = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MILLIS);
        UUID id = create(now, 5, 1, true, false, false, 20, 300);
        assertEquals(true, service.processEvent(id, new BidEventRequest(UUID.randomUUID(), now.plusSeconds(15), true, false, false)).get("extended"));
        Map<String, Object> second = service.processEvent(id, new BidEventRequest(UUID.randomUUID(), now.plusSeconds(70), true, false, false));
        assertEquals(true, second.get("extended"));
        assertEquals(now.plusSeconds(140), second.get("currentCloseTime"));
    }

    private void assertExtended(boolean onBid, boolean onRank, boolean onL1, boolean bid, boolean rank, boolean l1) {
        Instant now = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MILLIS);
        UUID id = create(now, 5, 2, onBid, onRank, onL1, 20, 60);
        assertEquals(true, service.processEvent(id, new BidEventRequest(UUID.randomUUID(), now.plusSeconds(15), bid, rank, l1)).get("extended"));
    }

    private UUID create(Instant now, int window, int extension, boolean bid, boolean rank, boolean l1, long closeSeconds, long forcedSeconds) {
        return (UUID) service.create(new CreateAuctionRequest("RFQ-" + UUID.randomUUID(), "Auction", now.plus(Duration.ofDays(1)), now.minusSeconds(5), now.plusSeconds(closeSeconds), now.plusSeconds(forcedSeconds), window, extension, bid, rank, l1)).get("id");
    }
}
