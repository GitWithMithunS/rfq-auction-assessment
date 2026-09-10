package com.rfq.auction_service;

import com.rfq.auction_service.dto.BidEventRequest;
import com.rfq.auction_service.dto.CreateAuctionRequest;
import com.rfq.auction_service.exception.BadRequestException;
import com.rfq.auction_service.service.AuctionService;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuctionRulesTest {
    @Autowired
    AuctionService service;

    @Test
    void capsExtensionAtForcedClose() {
        Instant now = Instant.now();
        Map<String, Object> created = service.create(new CreateAuctionRequest("RFQ-" + UUID.randomUUID(), "Freight", now.plus(Duration.ofDays(1)), now.minusSeconds(60), now.plusSeconds(30), now.plusSeconds(45), 5, 5, true, false, false));
        UUID id = (UUID) created.get("id");
        Map<String, Object> outcome = service.processEvent(id, new BidEventRequest(UUID.randomUUID(), now, true, false, false));
        assertEquals(Boolean.TRUE, outcome.get("extended"));
        assertEquals(now.plusSeconds(45).getEpochSecond(), ((Instant) outcome.get("currentCloseTime")).getEpochSecond());
    }

    @Test
    void rejectsInvalidForcedClose() {
        Instant now = Instant.now();
        assertThrows(BadRequestException.class, () -> service.create(new CreateAuctionRequest("RFQ-" + UUID.randomUUID(), "Freight", now, now, now.plusSeconds(60), now.plusSeconds(60), 1, 1, true, false, false)));
    }
}
