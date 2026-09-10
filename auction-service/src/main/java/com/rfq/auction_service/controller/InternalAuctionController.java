package com.rfq.auction_service.controller;

import com.rfq.auction_service.dto.*;
import com.rfq.auction_service.service.AuctionService;
import jakarta.validation.Valid;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/auctions")
@RequiredArgsConstructor
public class InternalAuctionController {
    private final AuctionService service;

    @GetMapping("/{id}/bid-context")
    public AuctionContextResponse context(@PathVariable UUID id) {
        return service.context(id);
    }

    @PostMapping("/{id}/events")
    public Map<String, Object> event(@PathVariable UUID id, @Valid @RequestBody BidEventRequest event) {
        return service.processEvent(id, event);
    }
}
