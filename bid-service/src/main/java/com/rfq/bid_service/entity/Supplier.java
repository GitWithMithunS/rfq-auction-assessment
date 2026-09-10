package com.rfq.bid_service.entity;

import jakarta.persistence.*;

import java.util.UUID;

import lombok.*;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private boolean active = true;
}
