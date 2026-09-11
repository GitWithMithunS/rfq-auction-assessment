package com.rfq.bid_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SubmitBidRequest(@NotNull UUID auctionId,
                               @NotNull UUID supplierId,
                               @NotNull @DecimalMin("0.00") BigDecimal freightCharges,
                               @NotNull @DecimalMin("0.00") BigDecimal originCharges,
                               @NotNull @DecimalMin("0.00") BigDecimal destinationCharges,
                               @NotNull @Positive Integer transitTimeDays,
                               @NotNull Instant quoteValidUntil) {
}
