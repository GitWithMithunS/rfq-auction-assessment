package com.rfq.auction_service.repository;

import com.rfq.auction_service.entity.AuctionActivityLog;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionActivityLogRepository extends JpaRepository<AuctionActivityLog, UUID> {
    List<AuctionActivityLog> findByAuctionIdOrderByOccurredAtAsc(UUID auctionId);

    boolean existsByEventId(UUID eventId);
}
