package com.rfq.bid_service.controller;

import com.rfq.bid_service.dto.SubmitBidRequest;
import com.rfq.bid_service.service.BidService;
import jakarta.validation.Valid;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BidController {
    private final BidService service;

    @PostMapping("/bids")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> submit(@Valid @RequestBody SubmitBidRequest request) {
        return service.submit(request);
    }

    @GetMapping("/auctions/{id}/bids")
    public List<Map<String, Object>> bids(@PathVariable UUID id) {
        return service.bids(id);
    }

    @GetMapping("/auctions/{id}/rankings")
    public List<Map<String, Object>> rankings(@PathVariable UUID id) {
        return service.rankings(id);
    }

    @GetMapping("/auctions/{id}/summary")
    public Map<String, Object> summary(@PathVariable UUID id) {
        return service.summary(id);
    }
}
