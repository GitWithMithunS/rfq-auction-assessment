package com.rfq.auction_service.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Entity
@Table(name = "rfqs")
@Getter
@Setter
@NoArgsConstructor
public class Rfq {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private String referenceId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Instant serviceDate;
    @Column(nullable = false)
    private boolean britishAuctionEnabled = true;
}
