package com.rfq.bid_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SubmitBidRequest(@NotNull UUID auctionId, @NotNull UUID supplierId,
                               @NotNull @DecimalMin("0.00") BigDecimal freightCharges,
                               @NotNull @DecimalMin("0.00") BigDecimal originCharges,
                               @NotNull @DecimalMin("0.00") BigDecimal destinationCharges,
                               @NotNull @Positive Integer transitTimeDays, @NotNull Instant quoteValidUntil) {
}
