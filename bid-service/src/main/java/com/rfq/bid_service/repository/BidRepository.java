package com.rfq.bid_service.repository;

import com.rfq.bid_service.entity.Bid;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, UUID> {
    List<Bid> findByAuctionIdOrderBySubmittedAtDesc(UUID auctionId);

    Optional<Bid> findFirstByAuctionIdAndSupplierIdOrderBySubmittedAtDesc(UUID auctionId, UUID supplierId);
}
