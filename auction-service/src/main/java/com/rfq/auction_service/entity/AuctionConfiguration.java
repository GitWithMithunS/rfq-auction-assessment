package com.rfq.auction_service.entity;

import jakarta.persistence.*;

import java.util.UUID;

import lombok.*;

@Entity
@Table(name = "auction_configurations")
@Getter
@Setter
@NoArgsConstructor
public class AuctionConfiguration {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private UUID auctionId;
    @Column(nullable = false)
    private int triggerWindowMinutes;
    @Column(nullable = false)
    private int extensionDurationMinutes;
    @Column(nullable = false)
    private boolean extendOnBidReceived;
    @Column(nullable = false)
    private boolean extendOnRankChange;
    @Column(nullable = false)
    private boolean extendOnL1Change;
}
