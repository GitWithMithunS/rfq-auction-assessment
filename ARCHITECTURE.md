# Architecture

```mermaid
flowchart LR
  B[Bid service / bid_db] -->|GET bid context; POST bid event| A[Auction service / auction_db]
  A --> E[Eureka]
  B --> E
```

Auction service owns RFQs, timing, extension decisions, state, and activity logs. Bid service owns suppliers, quote history, and rankings. The services never access each other's database.
