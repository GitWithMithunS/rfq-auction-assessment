package com.rfq.auction_service.repository;

import com.rfq.auction_service.entity.Rfq;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RfqRepository extends JpaRepository<Rfq, UUID> {
    boolean existsByReferenceId(String referenceId);
}
