package com.rfq.bid_service.dto;

import java.time.Instant;
import java.util.UUID;

public record AuctionContextResponse(UUID auctionId,
                                     String state,
                                     Instant bidStartTime,
                                     Instant currentCloseTime,
                                     Instant forcedCloseTime) {
}
