package com.rfq.bid_service.controller;

import com.rfq.bid_service.dto.CreateSupplierRequest;
import com.rfq.bid_service.service.BidService;
import jakarta.validation.Valid;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    private final BidService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@Valid @RequestBody CreateSupplierRequest request) {
        return service.createSupplier(request);
    }

    @GetMapping
    public List<Map<String, Object>> list() {
        return service.suppliers();
    }

}
