package com.rfq.auction_service.dto;

import jakarta.validation.constraints.*;

import java.time.Instant;

public record CreateAuctionRequest(@NotBlank String referenceId, @NotBlank String name, @NotNull Instant serviceDate,
                                   @NotNull Instant bidStartTime, @NotNull Instant bidCloseTime,
                                   @NotNull Instant forcedBidCloseTime, @Positive int triggerWindowMinutes,
                                   @Positive int extensionDurationMinutes, boolean extendOnBidReceived,
                                   boolean extendOnRankChange, boolean extendOnL1Change) {
}
