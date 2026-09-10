package com.rfq.auction_service.repository;

import com.rfq.auction_service.entity.Auction;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, UUID> {
    Optional<Auction> findByRfqId(UUID rfqId);
}
