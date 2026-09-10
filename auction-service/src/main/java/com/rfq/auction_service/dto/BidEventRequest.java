package com.rfq.auction_service.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record BidEventRequest(@NotNull UUID eventId, @NotNull Instant occurredAt, boolean bidReceived,
                              boolean rankChanged, boolean l1Changed) {
}
