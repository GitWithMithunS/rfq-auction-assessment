package com.rfq.auction_service.entity;

import com.rfq.auction_service.enums.AuctionState;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Entity
@Table(name = "auctions")
@Getter
@Setter
@NoArgsConstructor
public class Auction {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private UUID rfqId;
    @Column(nullable = false)
    private Instant bidStartTime;
    @Column(nullable = false)
    private Instant currentCloseTime;
    @Column(nullable = false)
    private Instant forcedCloseTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuctionState state;
    @Version
    private long version;
}
