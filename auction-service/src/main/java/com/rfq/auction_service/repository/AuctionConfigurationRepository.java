package com.rfq.auction_service.repository;

import com.rfq.auction_service.entity.AuctionConfiguration;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionConfigurationRepository extends JpaRepository<AuctionConfiguration, UUID> {
    Optional<AuctionConfiguration> findByAuctionId(UUID auctionId);
}
