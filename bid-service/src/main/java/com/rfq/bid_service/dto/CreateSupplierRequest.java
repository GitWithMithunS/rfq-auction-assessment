package com.rfq.bid_service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSupplierRequest(@NotBlank String name) {
}
