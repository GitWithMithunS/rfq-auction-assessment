package com.rfq.bid_service;

import com.rfq.bid_service.entity.Bid;
import com.rfq.bid_service.entity.Supplier;
import com.rfq.bid_service.repository.BidRepository;
import com.rfq.bid_service.repository.SupplierRepository;
import com.rfq.bid_service.service.BidService;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RankingRulesTest {
 @Autowired BidRepository bids; @Autowired SupplierRepository suppliers; @Autowired BidService service;
 @Test void ranksOnlyLatestBidPerSupplierByTotal() {
  UUID auction=UUID.randomUUID(); Supplier a=supplier("A"), b=supplier("B"); save(auction,a,100,Instant.now().minusSeconds(20)); save(auction,a,90,Instant.now().minusSeconds(10)); save(auction,b,95,Instant.now());
  List<Map<String,Object>> ranks=service.rankings(auction); assertEquals(a.getId(),ranks.get(0).get("supplierId")); assertEquals(2,ranks.size()); assertEquals(0,new BigDecimal("90").compareTo((BigDecimal)ranks.get(0).get("totalAmount")));
 }
 private Supplier supplier(String name){Supplier s=new Supplier();s.setName(name+UUID.randomUUID());return suppliers.save(s);}
 private void save(UUID auction,Supplier supplier,int value,Instant at){Bid b=new Bid();b.setAuctionId(auction);b.setSupplierId(supplier.getId());b.setFreightCharges(BigDecimal.valueOf(value));b.setOriginCharges(BigDecimal.ZERO);b.setDestinationCharges(BigDecimal.ZERO);b.setTotalAmount(BigDecimal.valueOf(value));b.setTransitTimeDays(1);b.setQuoteValidUntil(at.plusSeconds(3600));b.setSubmittedAt(at);bids.save(b);}
}
