package com.rfq.auction_service.controller;

import com.rfq.auction_service.dto.CreateAuctionRequest;
import com.rfq.auction_service.service.AuctionService;
import jakarta.validation.Valid;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@Valid @RequestBody CreateAuctionRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<Map<String, Object>> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable UUID id) {
        return service.detail(id);
    }

    @GetMapping("/{id}/activity")
    public List<Map<String, Object>> activity(@PathVariable UUID id) {
        return service.activity(id);
    }
}
