# Architecture

```mermaid
flowchart LR
  Client --> A[auction-service :8081]
  Client --> B[bid-service :8082]
  A --> ADB[(auction_db)]
  B --> BDB[(bid_db)]
  A --> E[eureka-server :8761]
  B --> E
  B -->|REST context/event| A
```

Auction service owns RFQs, lifecycle (`SCHEDULED`, `ACTIVE`, `CLOSED`, `FORCE_CLOSED`), configuration, timing, extensions, and activity logs. Bid service owns suppliers and bids, derives the latest bid per supplier, and calculates L1/L2/L3. Databases are independent: no cross-service foreign keys or direct cross-service reads. Bid processing and ranking are local to `bid_db`, so bid service can scale independently.

```mermaid
sequenceDiagram
  participant Buyer
  participant Auction
  participant Supplier
  participant Bid
  Buyer->>Auction: Create RFQ/auction with X/Y and triggers
  Supplier->>Bid: Submit quote
  Bid->>Auction: Get auction state/timing
  Bid->>Bid: Validate, persist, recalculate rank
  Bid->>Auction: POST bid/rank/L1 event
  Auction->>Auction: Extend by Y, capped at forced close
```

```mermaid
flowchart TD
  V[Valid bid] --> R[Latest bid per supplier and ranking]
  R --> D[Detect RANK_CHANGE / L1_CHANGE]
  D --> E[POST internal event]
  E --> Q{Inside X window and enabled?}
  Q -->|Yes| X[min current close + Y, forced close]
  Q -->|No| L[Activity log]
  X --> L
```

At forced close the auction resolves to `FORCE_CLOSED`; bids and extensions are rejected. Auction service records bid, rank, L1, and extension activity.
