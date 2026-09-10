package com.rfq.bid_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Entity
@Table(name = "bids")
@Getter
@Setter
@NoArgsConstructor
public class Bid {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private UUID auctionId;
    @Column(nullable = false)
    private UUID supplierId;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal freightCharges;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal originCharges;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal destinationCharges;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;
    @Column(nullable = false)
    private Integer transitTimeDays;
    @Column(nullable = false)
    private Instant quoteValidUntil;
    @Column(nullable = false)
    private Instant submittedAt;
}
