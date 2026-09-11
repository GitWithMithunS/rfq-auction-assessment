package com.rfq.bid_service.service;

import com.rfq.bid_service.dto.*;
import com.rfq.bid_service.entity.*;
import com.rfq.bid_service.repository.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class BidService {
    private final SupplierRepository suppliers;
    private final BidRepository bids;
    private final RestClient.Builder restClientBuilder;
    @Value("${auction-service.base-url}")
    private String auctionBaseUrl;
    private final Clock clock = Clock.systemUTC();

    @Transactional
    public Map<String, Object> createSupplier(CreateSupplierRequest r) {
        if (suppliers.existsByName(r.name())) throw new IllegalArgumentException("Supplier name already exists");
        Supplier s = new Supplier();
        s.setName(r.name());
        suppliers.save(s);
        return Map.of("id", s.getId(), "name", s.getName(), "active", s.isActive());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> suppliers() {
        return suppliers.findAll().stream()
                .map(s -> {
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("id", s.getId());
                    result.put("name", s.getName());
                    result.put("active", s.isActive());
                    return result;
                })
                .toList();
    }

    @Transactional
    public Map<String, Object> submit(SubmitBidRequest r) {
        Supplier s = suppliers.findById(r.supplierId()).orElseThrow(() -> new NoSuchElementException("Supplier not found"));
        if (!s.isActive()) throw new IllegalArgumentException("Supplier is inactive");
        AuctionContextResponse c = context(r.auctionId());
        Instant now = Instant.now(clock);
        if (!"ACTIVE".equals(c.state()) || now.isBefore(c.bidStartTime()) || !now.isBefore(c.currentCloseTime()) || !now.isBefore(c.forcedCloseTime()))
            throw new IllegalArgumentException("Auction is not accepting bids");
        BigDecimal total = r.freightCharges().add(r.originCharges()).add(r.destinationCharges());
        if (total.signum() <= 0) throw new IllegalArgumentException("Total bid amount must be greater than zero");
        Optional<Bid> prior = bids.findFirstByAuctionIdAndSupplierIdOrderBySubmittedAtDesc(r.auctionId(), r.supplierId());
        if (prior.isPresent() && total.compareTo(prior.get().getTotalAmount()) >= 0)
            throw new IllegalArgumentException("A bid must strictly improve the supplier's latest bid");
        List<Bid> before = latest(r.auctionId());
        Bid b = new Bid();
        b.setAuctionId(r.auctionId());
        b.setSupplierId(r.supplierId());
        b.setFreightCharges(r.freightCharges());
        b.setOriginCharges(r.originCharges());
        b.setDestinationCharges(r.destinationCharges());
        b.setTotalAmount(total);
        b.setTransitTimeDays(r.transitTimeDays());
        b.setQuoteValidUntil(r.quoteValidUntil());
        b.setSubmittedAt(now);
        bids.save(b);
        List<Bid> after = latest(r.auctionId());
        boolean rank = !rankOf(before, r.supplierId()).equals(rankOf(after, r.supplierId()));
        UUID oldL1 = before.isEmpty() ? null : before.get(0).getSupplierId();
        UUID newL1 = after.isEmpty() ? null : after.get(0).getSupplierId();
        notifyAuction(r.auctionId(), now, rank, !Objects.equals(oldL1, newL1));
        return view(b, rankOf(after, r.supplierId()));
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> bids(UUID id) {
        return bids.findByAuctionIdOrderBySubmittedAtDesc(id).stream().sorted(Comparator.comparing(Bid::getTotalAmount).thenComparing(Bid::getSubmittedAt)).map(b -> view(b, null)).toList();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> rankings(UUID id) {
        List<Bid> values = latest(id);
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) result.add(view(values.get(i), i + 1));
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> summary(UUID id) {
        List<Bid> values = latest(id);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("auctionId", id);
        result.put("currentLowestBid", values.isEmpty() ? null : values.get(0).getTotalAmount());
        if (!values.isEmpty()) result.put("supplierId", values.get(0).getSupplierId());
        return result;
    }

    private AuctionContextResponse context(UUID id) {
        try {
            return restClientBuilder.build().get().uri(auctionBaseUrl + "/internal/auctions/{id}/bid-context", id).retrieve().body(AuctionContextResponse.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Auction is unavailable or does not exist");
        }
    }

    private void notifyAuction(UUID id, Instant at, boolean rank, boolean l1) {
        Map<String, Object> event = Map.of("eventId", UUID.randomUUID(), "occurredAt", at, "bidReceived", true, "rankChanged", rank, "l1Changed", l1);
        try {
            restClientBuilder.build().post().uri(auctionBaseUrl + "/internal/auctions/{id}/events", id).contentType(MediaType.APPLICATION_JSON).body(event).retrieve().toBodilessEntity();
        } catch (Exception e) {
            throw new IllegalStateException("Bid stored but auction notification failed", e);
        }
    }

    private List<Bid> latest(UUID id) {
        Map<UUID, Bid> latest = new HashMap<>();
        for (Bid bid : bids.findByAuctionIdOrderBySubmittedAtDesc(id)) latest.putIfAbsent(bid.getSupplierId(), bid);
        return latest.values().stream().sorted(Comparator.comparing(Bid::getTotalAmount).thenComparing(Bid::getSubmittedAt).thenComparing(Bid::getId)).toList();
    }

    private Integer rankOf(List<Bid> values, UUID supplier) {
        for (int i = 0; i < values.size(); i++) if (values.get(i).getSupplierId().equals(supplier)) return i + 1;
        return 0;
    }

    private Map<String, Object> view(Bid b, Integer rank) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", b.getId());
        result.put("auctionId", b.getAuctionId());
        result.put("supplierId", b.getSupplierId());
        result.put("freightCharges", b.getFreightCharges());
        result.put("originCharges", b.getOriginCharges());
        result.put("destinationCharges", b.getDestinationCharges());
        result.put("totalAmount", b.getTotalAmount());
        result.put("transitTimeDays", b.getTransitTimeDays());
        result.put("quoteValidUntil", b.getQuoteValidUntil());
        result.put("submittedAt", b.getSubmittedAt());
        if (rank != null) result.put("rank", rank);
        return result;
    }
}
