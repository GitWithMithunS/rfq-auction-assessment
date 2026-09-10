package com.rfq.auction_service.service;

import com.rfq.auction_service.dto.*;
import com.rfq.auction_service.entity.*;
import com.rfq.auction_service.enums.*;
import com.rfq.auction_service.exception.*;
import com.rfq.auction_service.repository.*;

import java.time.*;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final RfqRepository rfqs;
    private final AuctionRepository auctions;
    private final AuctionConfigurationRepository configurations;
    private final AuctionActivityLogRepository logs;
    private final Clock clock = Clock.systemUTC();

    @Transactional
    public Map<String, Object> create(CreateAuctionRequest r) {
        if (!r.forcedBidCloseTime().isAfter(r.bidCloseTime()))
            throw new BadRequestException("Forced close time must be later than bid close time");
        if (!r.bidCloseTime().isAfter(r.bidStartTime()))
            throw new BadRequestException("Bid close time must be later than bid start time");
        if (!r.extendOnBidReceived() && !r.extendOnRankChange() && !r.extendOnL1Change())
            throw new BadRequestException("At least one extension trigger is required");
        if (rfqs.existsByReferenceId(r.referenceId())) throw new BadRequestException("RFQ reference already exists");
        Rfq rfq = new Rfq();
        rfq.setReferenceId(r.referenceId());
        rfq.setName(r.name());
        rfq.setServiceDate(r.serviceDate());
        rfqs.save(rfq);
        Auction a = new Auction();
        a.setRfqId(rfq.getId());
        a.setBidStartTime(r.bidStartTime());
        a.setCurrentCloseTime(r.bidCloseTime());
        a.setForcedCloseTime(r.forcedBidCloseTime());
        a.setState(stateFor(a, Instant.now(clock)));
        auctions.save(a);
        AuctionConfiguration c = new AuctionConfiguration();
        c.setAuctionId(a.getId());
        c.setTriggerWindowMinutes(r.triggerWindowMinutes());
        c.setExtensionDurationMinutes(r.extensionDurationMinutes());
        c.setExtendOnBidReceived(r.extendOnBidReceived());
        c.setExtendOnRankChange(r.extendOnRankChange());
        c.setExtendOnL1Change(r.extendOnL1Change());
        configurations.save(c);
        return view(a, rfq, c);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> list() {
        return auctions.findAll().stream().map(a -> view(a, rfqs.findById(a.getRfqId()).orElseThrow(() -> new NotFoundException("RFQ not found")), config(a))).toList();
    }

    @Transactional
    public Map<String, Object> detail(UUID id) {
        Auction a = auction(id);
        refresh(a);
        return view(a, rfqs.findById(a.getRfqId()).orElseThrow(() -> new NotFoundException("RFQ not found")), config(a));
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> activity(UUID id) {
        auction(id);
        return logs.findByAuctionIdOrderByOccurredAtAsc(id).stream().map(l -> Map.<String, Object>of("type", l.getActivityType(), "occurredAt", l.getOccurredAt(), "resultingCloseTime", String.valueOf(l.getResultingCloseTime()), "reason", l.getReason())).toList();
    }

    @Transactional
    public AuctionContextResponse context(UUID id) {
        Auction a = auction(id);
        refresh(a);
        return new AuctionContextResponse(a.getId(), a.getState(), a.getBidStartTime(), a.getCurrentCloseTime(), a.getForcedCloseTime());
    }

    @Transactional
    public Map<String, Object> processEvent(UUID id, BidEventRequest e) {
        Auction a = auction(id);
        if (logs.existsByEventId(e.eventId())) return Map.of("extended", false, "reason", "duplicate event");
        refresh(a);
        log(id, ActivityType.BID_RECEIVED, e.occurredAt(), null, "Bid submitted", e.eventId());
        AuctionConfiguration c = config(a);
        if (a.getState() != AuctionState.ACTIVE || e.occurredAt().isBefore(a.getBidStartTime()) || !e.occurredAt().isBefore(a.getCurrentCloseTime()))
            return Map.of("extended", false, "reason", "auction not active");
        if (e.rankChanged()) log(id, ActivityType.RANK_CHANGE, e.occurredAt(), null, "Supplier ranking changed", null);
        if (e.l1Changed()) log(id, ActivityType.L1_CHANGE, e.occurredAt(), null, "Lowest bidder changed", null);
        boolean inside = !e.occurredAt().isBefore(a.getCurrentCloseTime().minus(Duration.ofMinutes(c.getTriggerWindowMinutes())));
        boolean qualifies = inside && ((e.bidReceived() && c.isExtendOnBidReceived()) || (e.rankChanged() && c.isExtendOnRankChange()) || (e.l1Changed() && c.isExtendOnL1Change()));
        if (!qualifies)
            return Map.of("extended", false, "reason", inside ? "trigger not configured" : "outside trigger window");
        Instant next = a.getCurrentCloseTime().plus(Duration.ofMinutes(c.getExtensionDurationMinutes()));
        if (next.isAfter(a.getForcedCloseTime())) next = a.getForcedCloseTime();
        if (!next.isAfter(a.getCurrentCloseTime())) return Map.of("extended", false, "reason", "forced close reached");
        a.setCurrentCloseTime(next);
        auctions.save(a);
        log(id, ActivityType.TIME_EXTENDED, e.occurredAt(), next, "Qualifying bidding event", null);
        return Map.of("extended", true, "currentCloseTime", next);
    }

    private Auction auction(UUID id) {
        return auctions.findById(id).orElseThrow(() -> new NotFoundException("Auction not found"));
    }

    private AuctionConfiguration config(Auction a) {
        return configurations.findByAuctionId(a.getId()).orElseThrow(() -> new NotFoundException("Configuration not found"));
    }

    private void refresh(Auction a) {
        AuctionState state = stateFor(a, Instant.now(clock));
        if (state != a.getState()) {
            a.setState(state);
            auctions.save(a);
        }
    }

    private AuctionState stateFor(Auction a, Instant now) {
        if (!now.isBefore(a.getForcedCloseTime())) return AuctionState.FORCE_CLOSED;
        if (!now.isBefore(a.getCurrentCloseTime())) return AuctionState.CLOSED;
        return now.isBefore(a.getBidStartTime()) ? AuctionState.SCHEDULED : AuctionState.ACTIVE;
    }

    private void log(UUID id, ActivityType type, Instant at, Instant close, String reason, UUID eventId) {
        AuctionActivityLog log = new AuctionActivityLog();
        log.setAuctionId(id);
        log.setActivityType(type);
        log.setOccurredAt(at);
        log.setResultingCloseTime(close);
        log.setReason(reason);
        log.setEventId(eventId);
        logs.save(log);
    }

    private Map<String, Object> view(Auction a, Rfq r, AuctionConfiguration c) {
        return Map.of("id", a.getId(), "rfqId", r.getId(), "referenceId", r.getReferenceId(), "name", r.getName(), "state", a.getState(), "bidStartTime", a.getBidStartTime(), "currentCloseTime", a.getCurrentCloseTime(), "forcedCloseTime", a.getForcedCloseTime(), "triggerWindowMinutes", c.getTriggerWindowMinutes(), "extensionDurationMinutes", c.getExtensionDurationMinutes());
    }
}
