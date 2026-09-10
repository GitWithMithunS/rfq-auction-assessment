package com.rfq.bid_service.repository;

import com.rfq.bid_service.entity.Supplier;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    boolean existsByName(String name);
}
