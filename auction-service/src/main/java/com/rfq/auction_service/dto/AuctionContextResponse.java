package com.rfq.auction_service.dto;

import com.rfq.auction_service.enums.AuctionState;

import java.time.Instant;
import java.util.UUID;

public record AuctionContextResponse(UUID auctionId, AuctionState state, Instant bidStartTime, Instant currentCloseTime,
                                     Instant forcedCloseTime) {
}
