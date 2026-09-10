package com.rfq.auction_service.entity;

import com.rfq.auction_service.enums.ActivityType;

import com.rfq.auction_service.enums.ActivityType;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Entity
@Table(name = "auction_activity_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuctionActivityLog {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private UUID auctionId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType;
    @Column(nullable = false)
    private Instant occurredAt;
    private Instant resultingCloseTime;
    @Column(nullable = false)
    private String reason;
    @Column(unique = true)
    private UUID eventId;
}
